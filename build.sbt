val http4sVersion = "0.23.16"
val tapirVersion = "1.2.3"
val circeVersion = "0.14.3"

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    name := "blog-demo",
    organization := "com.github.gcnyin",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      /* tapir */
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      /* http4s */
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      /* circe */
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      /* logging */
      "ch.qos.logback" % "logback-classic" % "1.4.5",
      /* test */
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "com.softwaremill.sttp.client3" %% "circe" % "3.8.3" % Test,
      "org.scalatest" %% "scalatest" % "3.2.14" % Test
    )
  )
