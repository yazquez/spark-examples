package org.yazquez.spark.examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.HashPartitioner

object PageRank extends App {
  val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("PageRank"))

  val linksList = List(
    ("wikipedia", List("youtube", "facebook", "microsoft")),
    ("oracle", List("microsoft")),
    ("youtube", List("wikipedia", "facebook")),
    ("facebook", List("wikipedia", "youtube")),
    ("microsoft", List("wikipedia", "oracle", "youtube")))

  val links = sc.parallelize(linksList).partitionBy(new HashPartitioner(100)).persist()
  
  var ranks = links.mapValues(v => 1.0)
  //Array[(String, Int)] = Array((ucla,1.0), (youtube,1.0), (pc,1.0), (facebook,1.0), (wikipedia,1.0)
  
  for( i<-0 until 10){
     val contributions = links.join(ranks).flatMap {
       case (pageId, (pageLinks,pageRank)) =>  {
         pageLinks.map( pageLink => (pageLink, pageRank/pageLinks.size))
       }
     }
     // Nos habrá generado un RDD con las listas de PageId y su pageRank relativo (contributionsReceived)
     // A continuación los sumamos (para cada PageId) y le aplicamos la formula  0.15 + 0.85 * contributionsReceived
     
     ranks = contributions.reduceByKey((acc,v)=>acc+v).mapValues { 0.15 + 0.85 * _}
  }
  
  ranks.saveAsTextFile("file:///c:/tmp/ranks")
}