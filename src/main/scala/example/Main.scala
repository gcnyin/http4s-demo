package example

import cats.data.Kleisli
import cats.effect._
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.{GZip, Logger}
import org.http4s.server.{Router, Server}
import org.http4s.{Http, HttpRoutes}
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import trace4cats._
import trace4cats.http4s.common.Http4sRequestFilter
import trace4cats.http4s.server.syntax._
import trace4cats.http4s.client.syntax._
import trace4cats.jaeger.JaegerSpanCompleter


object Main extends IOApp {
  type G[x] = Kleisli[IO, Span[IO], x]

  def entryPoint[F[_]: Async](process: TraceProcess): Resource[F, EntryPoint[F]] = {
    JaegerSpanCompleter[F](process = process).map { completer =>
      EntryPoint[F](SpanSampler.always, completer)
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      counterRef <- Ref[IO].of(0)

      serverOptions: Http4sServerOptions[G] = Http4sServerOptions.default[G]
      serverLogic = new ServerLogic[G](counterRef)
      resource: Resource[IO, Server] = for {
        ep <- entryPoint[IO](TraceProcess("http4s-demo"))
        r = Http4sServerInterpreter[G](serverOptions).toRoutes(serverLogic.all)
        routes = r.inject(ep, requestFilter = Http4sRequestFilter.kubernetesPrometheus)
        app: Http[IO, IO] = Router("/" -> routes).orNotFound
        finalApp = Logger.httpApp(logHeaders = true, logBody = false)(GZip(app))
        s <- EmberServerBuilder
          .default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalApp)
          .build >> Resource.eval(Async[IO].never)
      } yield s
      s <- Stream
        .resource(resource)
        .drain
        .compile
        .drain
        .as(ExitCode.Success)
    } yield s
  }
}
