val http4sVersion = "0.23.16"
val tapirVersion = "1.2.3"

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    name := "http4s-demo",
    organization := "com.github.gcnyin",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      /* tapir */
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
      /* http4s */
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      /* logging */
      "ch.qos.logback" % "logback-classic" % "1.4.5"
    )
  )
