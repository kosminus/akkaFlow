package parser

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Flow, Framing, Sink, Source}
import akka.util.ByteString
import model.Domain.ProcessLine
import model.RawModel
import parser.Parser

import java.nio.file.Paths
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

class TextParser extends Parser[RawModel] {

  def parse(file: String):Future[List[RawModel]] = {
    val source = FileIO.fromPath(Paths.get(file))
     val rawModelFlow = Flow[String].map(line=>RawModel(line))
    val flow = Framing.delimiter(ByteString("\n"), 1024, true)
      .map(_.utf8String)
     val result: Future[List[RawModel]]=
       source.via(flow)
         .via(rawModelFlow)
         .runWith(Sink.collection[RawModel, List[RawModel]])
    result
  }

}

