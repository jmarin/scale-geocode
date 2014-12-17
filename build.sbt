name := "scale-geocode"

version := "0.0.1"

scalaVersion := "2.11.4"

libraryDependencies ++= {
  val akka = "2.3.7"
  val spray = "1.3.2"
  val sprayJson = "1.3.1"
  val elastic4s = "1.4.0"
  val scale = "0.0.1-SNAPSHOT"
  val specs2 = "2.4.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akka,
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
