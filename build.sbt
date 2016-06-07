name := "Spark Examples"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.0"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

libraryDependencies += "org.apache.hbase" % "hbase-server" % "0.99.2"

libraryDependencies += "org.apache.hbase" % "hbase-common" % "0.99.2"

libraryDependencies += "org.apache.hbase" % "hbase-client" % "0.99.2"

libraryDependencies += "org.apache.spark" %% "spark-hive" % "1.2.0"