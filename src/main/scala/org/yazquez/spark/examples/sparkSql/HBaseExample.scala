package org.yazquez.spark.examples.sparkSql

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable

object HBaseExample extends App {
  val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("HBaseExample Example"))

  val conf = HBaseConfiguration.create()
  conf.set(TableInputFormat.INPUT_TABLE, "tablename") // which table to scan
  val rdd = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
}