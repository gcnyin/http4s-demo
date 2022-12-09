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
      /* tracing */
      "io.janstenpickle" %% "trace4cats-core" % "0.14.1",
      "io.janstenpickle" %% "trace4cats-avro-exporter" % "0.14.0",
      "io.janstenpickle" %% "trace4cats-http4s-client" % "0.14.0",
      "io.janstenpickle" %% "trace4cats-http4s-server" % "0.14.0",
      "io.janstenpickle" %% "trace4cats-jaeger-thrift-exporter" % "0.14.0",
      "io.janstenpickle" %% "trace4cats-jaeger-thrift-http-exporter" % "0.14.0",
      /* json */
      "dev.zio" %% "zio-json" % "0.4.2",
      /* http4s */
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      /* logging */
      "ch.qos.logback" % "logback-classic" % "1.4.5"
    )
  )
