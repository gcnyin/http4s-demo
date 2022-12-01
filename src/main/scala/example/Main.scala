package example

import cats.effect.kernel.Async
import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.Http
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.{GZip, Logger}
import org.http4s.server.{Router, Server}
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val serverOptions: Http4sServerOptions[IO] = Http4sServerOptions.default[IO]

    val routes = Http4sServerInterpreter[IO](serverOptions)
      .toRoutes(Endpoints.all)

    val app: Http[IO, IO] = Router("/" -> routes).orNotFound

    val finalApp = Logger.httpApp(logHeaders = true, logBody = false)(GZip(app))

    val resource: Resource[IO, Server] =
      EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(finalApp)
        .build >> Resource.eval(Async[IO].never)

    Stream
      .resource(resource)
      .drain
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
