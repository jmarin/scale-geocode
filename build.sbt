name := "scale-geocode"

version := "0.0.1"

scalaVersion := "2.11.4"

javaOptions += "-Xmx4G"

libraryDependencies ++= {
  val parserCombinators = "1.0.2"
  val akka = "2.3.7"
  val akkaStreams = "1.0-M1"
  val spray = "1.3.2"
  val sprayJson = "1.3.1"
  val elastic4s = "1.4.0"
  val scale = "0.0.1-SNAPSHOT"
  val specs2 = "2.4.2"
  Seq(
    "org.scala-lang.modules" %% "scala-parser-combinators" % parserCombinators,
    "com.typesafe.akka" %% "akka-actor" % akka,
    "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreams,
    "io.spray" %% "spray-can" % spray,
    "io.spray" %% "spray-routing" % spray,
    "io.spray" %%  "spray-json" % sprayJson,
    "com.sksamuel.elastic4s" %% "elastic4s" % elastic4s,
    "scale" %% "scale-core" % scale,
    "scale" %% "scale-serialization" % scale,
    "com.typesafe.akka" %% "akka-testkit" % akka % "test",
    "org.specs2" %% "specs2" % specs2 % "test"
  )
}

scalariformSettings

Revolver.settings
