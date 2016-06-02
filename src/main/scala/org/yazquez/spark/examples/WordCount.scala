package org.yazquez.spark.examples

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object WordCounter {
  
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Word Counter").setMaster("local"))
   
    val textFile = sc.textFile(raw"c:/README.md")
    val tokenizedFileData = textFile.flatMap(_.split(" "))
    val counterPrep = tokenizedFileData.map(word=>(word,1))
    val counts = counterPrep.reduceByKey(_ + _)
    val sortedCounts = counts.sortBy(kv=>kv._2, false) // Ordenamos por numero de ocurrencias
    
    sortedCounts.saveAsTextFile(raw"c:/Tmp/WordCounterResults")
  } 
}