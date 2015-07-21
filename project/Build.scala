import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object CassandraAccessBuild extends Build {
  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1-SNAPSHOT",
    organization := "org.jzhou",
    scalaVersion := "2.10.4"
  )

  lazy val app = Project(
    "ProcessPosts",
    file("."),
    settings = buildSettings ++ assemblySettings ++ Seq(
      parallelExecution in Test := false,
      libraryDependencies ++= Seq(
        ("org.apache.spark" %% "spark-sql" % "1.3.1").
        exclude("com.twitter", "parquet-format").
        exclude("com.esotericsoftware.minlog", "minlog").
        exclude("commons-beanutils", "commons-beanutils").
        exclude("commons-collections", "commons-collections").
        exclude("org.apache.spark", "spark-network-shuffle_2.10").
        exclude("org.spark-project.spark", "unused").
        exclude("org.apache.hadoop", "hadoop-yarn-api").
        exclude("org.apache.spark", "spark-network-common_2.10").
        exclude("org.slf4j", "jcl-over-slf4j").
        exclude("io.dropwizard.metrics", "metrics-core"),
        "com.datastax.spark" %% "spark-cassandra-connector" % "1.3.0-M2",
        ("com.quantifind" %% "wisp" % "0.0.4").
        exclude("org.eclipse.jetty", "jetty-continuation").      
        exclude("org.eclipse.jetty", "jetty-security").      
        exclude("org.eclipse.jetty", "jetty-servlet").      
        exclude("org.eclipse.jetty", "jetty-util").      
        exclude("org.eclipse.jetty", "jetty-http"),      
        ("org.apache.spark"%%"spark-core"%"1.3.1").
        exclude("io.dropwizard.metrics", "metrics-core").
        exclude("com.esotericsoftware.minlog", "minlog").
        exclude("commons-beanutils", "commons-beanutils").
        exclude("commons-collections", "commons-collections").
        exclude("org.slf4j", "jcl-over-slf4j").
        exclude("org.apache.hadoop", "hadoop-yarn-api").
        exclude("org.apache.spark", "spark-network-shuffle_2.10").
        exclude("org.spark-project.spark", "unused").
        exclude("org.slf4j", "slf4j-api").
        exclude("org.apache.spark", "spark-network-common_2.10")
      )
    )
  )

}
