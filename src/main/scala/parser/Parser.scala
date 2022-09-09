package parser

import akka.stream.scaladsl.Source
import model.RawModel
import org.apache.avro.generic.GenericRecord

import scala.concurrent.Future

trait Parser[T] {
  def parse(file:String): Future[List[T]]
}

object Parser {
  def apply(filename:String)=
    filename match {
      case f if f.endsWith(".txt") => new TextParser
      case f if f.endsWith(".parquet") => new ParquetParser
      case f => throw new RuntimeException(s"Unknow format $f")
    }
}
