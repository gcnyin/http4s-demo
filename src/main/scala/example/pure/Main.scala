package example.pure

import cats.effect._
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.multipart.Multipart
import org.http4s.server.middleware.{GZip, Logger}
import org.http4s.server.{Router, Server}

object Main extends IOApp {
  def uploadFileService[F[_]: Async]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] { case request @ POST -> Root / "upload" =>
      request.decode[Multipart[F]] { f => Ok(f.parts.size.toString) }
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val httpApp = Router("/" -> uploadFileService[IO]).orNotFound
    val finalApp = Logger.httpApp(logHeaders = true, logBody = false)(GZip(httpApp))
    val resource: Resource[IO, Server] = EmberServerBuilder
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
