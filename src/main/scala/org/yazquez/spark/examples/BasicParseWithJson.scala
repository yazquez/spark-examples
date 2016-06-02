package org.yazquez.spark.examples

import org.apache.spark._
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature



case class Person(name: String, lovesPandas: Boolean) // Note: must be a top level class

object BasicParseWithJson extends App {

    val master = "local"
    val inputFileName = "file:///c:/tmp/files/json/persons.json"
    val outputFileName = "file:///c:/tmp/files/json/persons-out"

    val sc = new SparkContext(master, "BasicParseJsonWithJackson", System.getenv("SPARK_HOME"))
    val input = sc.textFile(inputFileName)

    // Parse it into a specific case class. We use mapPartitions beacuse:
    // (a) ObjectMapper is not serializable so we either create a singleton object encapsulating ObjectMapper
    //     on the driver and have to send data back to the driver to go through the singleton object.
    //     Alternatively we can let each node create its own ObjectMapper but that's expensive in a map
    // (b) To solve for creating an ObjectMapper on each node without being too expensive we create one per
    //     partition with mapPartitions. Solves serialization and object creation performance hit.
    val result = input.mapPartitions(records => {
        // mapper object created on each executor node
        val mapper = new ObjectMapper with ScalaObjectMapper
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.registerModule(DefaultScalaModule)
        // We use flatMap to handle errors
        // by returning an empty list (None) if we encounter an issue and a
        // list with one element if everything is ok (Some(_)).
        records.flatMap(record => {
          try {
            Some(mapper.readValue(record, classOf[Person]))
          } catch {
            case e: Exception => None
          }
        })
    }, true)
    
    result.filter(_.lovesPandas).mapPartitions(records => {
      val mapper = new ObjectMapper with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      records.map(mapper.writeValueAsString(_))
    }).saveAsTextFile(outputFileName)
}