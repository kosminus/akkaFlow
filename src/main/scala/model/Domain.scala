package model

import akka.actor.ActorRef

object Domain {
  case class ProcessFile(filename: String)
  case class ProcessLine[T](data: T, aggregator: ActorRef) extends MySerializable
  case class ProcessLineResult(count:Int) extends MySerializable
  case class GetInputFiles()
  case class Initialize()
}
