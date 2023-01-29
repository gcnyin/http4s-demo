package example

import cats.effect.kernel.{Async, Ref}
import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.Http
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.{GZip, Logger}
import org.http4s.server.{Router, Server}
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      counterRef <- Ref[IO].of(0)
      prometheusMetrics = PrometheusMetrics.default[IO]()
      metricsEndpoint = prometheusMetrics.metricsEndpoint
      serverOptions: Http4sServerOptions[IO] =
        Http4sServerOptions
          .customiseInterceptors[IO]
          .metricsInterceptor(prometheusMetrics.metricsInterceptor())
          .options
      serverLogic = new ServerLogic[IO](counterRef)
      routes = Http4sServerInterpreter[IO](serverOptions).toRoutes(serverLogic.all :+ metricsEndpoint)
      app: Http[IO, IO] = Router("/" -> routes).orNotFound
      finalApp = Logger.httpApp(logHeaders = true, logBody = false)(GZip(app))
      resource: Resource[IO, Server] = EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(finalApp)
        .build >> Resource.eval(Async[IO].never)
      s <- Stream
        .resource(resource)
        .drain
        .compile
        .drain
        .as(ExitCode.Success)
    } yield s
  }
}
