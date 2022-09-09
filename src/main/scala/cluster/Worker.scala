package cluster

import akka.actor.{Actor, ActorLogging}
import model.Domain.{ProcessLine, ProcessLineResult}

class Worker extends Actor with ActorLogging {
  override def receive: Receive = {
    case ProcessLine(rawmodel, aggregator) =>
      log.info(s"processing + $rawmodel")
      aggregator ! ProcessLineResult(rawmodel.data.split(" ").length)
  }
}
