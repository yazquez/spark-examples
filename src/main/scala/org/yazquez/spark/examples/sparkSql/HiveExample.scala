package org.yazquez.spark.examples.sparkSql

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.reflect.runtime.universe

object HiveExample extends App {
  val sc = new SparkContext( new SparkConf().setMaster("local[2]").setAppName("Hive Example"))
  
  val hc = new HiveContext(sc) 
  import hc._
  

  // Creación de tabla a partir de fichero JSON
  
  val input = hc.jsonFile("file:///c:/tmp/files/json/employees.json")
  
  input.registerTempTable("employees")
  
  val internalEmployees = hc.sql("select name, role from employees where internal = true")

  val names = internalEmployees.map(row=>row.getString(0) )
  
  println(names.collect().toList)
  
  
//  // Creación de una función de usuario
//   
//  hc.registerFunction("strlen", (_:String).length)
//    
//  val employeesLenNames = hc.sql("select name, strlen(name) namelength from employees")
// 
//  val namesAndLen = employeesLenNames.map(row=>(row.getString(0), row.getInt(1)) )
//  
//  println(namesAndLen.collect().toList)    
//
//
//  // Creación de tabla a partir de un RDD  
//  
//  val cars = sc.parallelize(List(("Suzuki", "Japan", "1960"),("Honda", "Japan", "1960"),("Ford","USA", "1920"), ("Porsche","Germany","1932"))) 
//  
//  cars.registerTempTable("cars")
//
//  val japanCars = hc.sql("select * from cars where  _2 =='Japan'")
//  
//  val brands = japanCars.map(row=>row.getString(0))
//  
//  println(brands.collect().toList)
  
}