package parser

import akka.stream.scaladsl.{FileIO, Flow, Framing, Sink}
import akka.util.ByteString
import model.RawModel

import java.nio.file.Paths
import scala.concurrent.Future

class TextParser extends Parser[RawModel[String]] {

  def parse(file: String):Future[List[RawModel[String]]] = {
    val source = FileIO.fromPath(Paths.get(file))
     val rawModelFlow = Flow[String].map(line=>RawModel(line))
    val flow = Framing.delimiter(ByteString("\n"), 1024, true)
      .map(_.utf8String)
     val result: Future[List[RawModel[String]]]=
       source.via(flow)
         .via(rawModelFlow).async
         .runWith(Sink.collection[RawModel[String], List[RawModel[String]]])
    result
  }

}

