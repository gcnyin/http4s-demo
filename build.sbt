val http4sVersion = "0.23.22"
val tapirVersion = "1.6.0"
val circeVersion = "0.14.5"

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    name := "http4s-demo",
    organization := "com.github.gcnyin",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.11",
    libraryDependencies ++= Seq(
      /* tapir */
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
      /* http4s */
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % circeVersion,

      /* logging */
      "ch.qos.logback" % "logback-classic" % "1.4.8"
    )
  )
