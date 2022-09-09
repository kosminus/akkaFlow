package parser

import akka.stream.scaladsl.Source
import model.RawModel

import scala.concurrent.Future

trait Parser[T] {
  def parse(file:String): Future[List[RawModel]]
}

object Parser {
  def apply(filename:String):Parser[RawModel]=
    filename match {
      case f if f.endsWith(".txt") => new TextParser
      case f if f.endsWith(".parquet") => new ParquetParser
      case f => throw new RuntimeException(s"Unknow format $f")
    }
}
