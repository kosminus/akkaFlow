package cluster

import akka.actor.{Actor, ActorLogging, ReceiveTimeout}
import model.Domain.ProcessLineResult

import scala.concurrent.duration.{Duration, DurationInt}

class Aggregator extends Actor with ActorLogging {
  context.setReceiveTimeout(10 seconds)

  override def receive: Receive = online(0)

  def online(totalCount: Int): Receive = {
    case ProcessLineResult(count) =>
      context.become(online(totalCount + count))
    case ReceiveTimeout =>
      log.info(s"TOTAL COUNT: $totalCount")
      context.setReceiveTimeout(Duration.Undefined)
  }
}

