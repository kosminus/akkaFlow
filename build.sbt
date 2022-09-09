ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.16"

lazy val root = (project in file("."))
  .settings(
    name := "akkaFlow"
  )

lazy val akkaVersion = "2.6.19"
lazy val alpakkaVersion = "3.0.4"
lazy val hadoopVersion = "3.3.4"
lazy val aeronVersion = "1.39.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-file" % alpakkaVersion,
  "io.aeron" % "aeron-driver" %  aeronVersion,
  "io.aeron" % "aeron-client" % aeronVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-avroparquet" % "3.0.4",
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % hadoopVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.4",
  "com.google.protobuf" % "protobuf-java" % "3.9.2",
  "com.github.romix.akka" %% "akka-kryo-serialization" % "0.5.2"
)