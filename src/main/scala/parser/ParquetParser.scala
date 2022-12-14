package parser

import akka.NotUsed
import akka.stream.alpakka.avroparquet.scaladsl.AvroParquetSource
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.conf.Configuration
import org.apache.parquet.avro.{AvroParquetReader, AvroReadSupport}
import org.apache.parquet.hadoop.ParquetReader
import org.apache.parquet.hadoop.util.HadoopInputFile
import org.apache.hadoop.fs.Path
import akka.actor.ActorSystem
import model.RawModel

import scala.concurrent.Future

class ParquetParser extends Parser[RawModel[GenericRecord]] {
  def parse (file: String): Future[List[RawModel[GenericRecord]]] = {
    val conf: Configuration = new Configuration()
     val rawModelFlow = Flow[GenericRecord].map(line=>RawModel(line))
     conf.setBoolean(AvroReadSupport.AVRO_COMPATIBILITY, true)
    val reader: ParquetReader[GenericRecord] =
      AvroParquetReader.builder[GenericRecord](HadoopInputFile.fromPath(new Path(file), conf)).withConf(conf).build()

    val source = AvroParquetSource(reader)
     val result: Future[List[RawModel[GenericRecord]]]=
       source.via(rawModelFlow).async
         .runWith(Sink.collection[RawModel[GenericRecord], List[RawModel[GenericRecord]]])
     result
  }

}
