package cluster

import akka.actor.{Actor, ActorLogging}
import model.Domain.{GetInputFiles, ProcessFile}
import utils.InputFactory

class InputActor extends Actor with ActorLogging{
  override def receive: Receive = {
    case GetInputFiles() =>
      InputFactory.getInputList().foreach( file =>
        sender() ! ProcessFile(file.toString))
  }
}
