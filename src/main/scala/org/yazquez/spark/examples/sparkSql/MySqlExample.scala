package org.yazquez.spark.examples.sparkSql

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import java.sql.DriverManager
import java.sql.ResultSet
import org.apache.spark.rdd.JdbcRDD

object MySqlExample extends App {
  def createConnection() = {
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql://localhost/test?user=test&password=test")
  }

  def extractValues(r: ResultSet) = {
    (r.getInt(1), r.getString(2))
  }

  val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("MySql Example"))


  val data = new JdbcRDD(sc,
    createConnection, "SELECT id, name, role FROM users WHERE ? <= id AND id <= ?",
    lowerBound = 1, upperBound = 3, numPartitions = 2, mapRow = extractValues)
  
  println(data.collect().toList)

}