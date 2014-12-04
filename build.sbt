name := "scale-geocode"

version := "0.0.1"

scalaVersion := "2.11.4"

libraryDependencies ++= {
  val akka = "2.3.7"
  val scale = "0.0.1-SNAPSHOT"
  val specs2 = "2.4.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akka,
    "scale" %% "scale-core" % scale,
    "com.typesafe.akka" %% "akka-testkit" % akka % "test",
    "org.specs2" %% "specs2" % specs2 % "test"
  )
}

scalariformSettings

Revolver.settings
