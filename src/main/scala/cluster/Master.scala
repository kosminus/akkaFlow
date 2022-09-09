package cluster

import akka.actor.{Actor, ActorLogging, ActorRef, Address, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Sink, Source}
import akka.util.{ByteString, Timeout}
import model.Domain.{GetInputFiles, Initialize, ProcessFile, ProcessLine}
import parser.{Parser, TextParser, actorSystem}

import java.io.File
import java.nio.file.Paths
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}

class Master extends Actor with ActorLogging  {
  import context.dispatcher
  implicit val timeout: Timeout = Timeout(3 seconds)

  val cluster: Cluster = Cluster(context.system)
  var workers: Map[Address, ActorRef] = Map()

  override def preStart(): Unit = {
    cluster.subscribe(self,
      initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent],
      classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive(): Receive =
    handleCustomEvents()
      .orElse(handleWorkerRegistration())
      .orElse(handleJob())
      .orElse(Init())

  def handleWorkerRegistration(): Receive = {
    case pair: (Address, ActorRef) =>
      log.info(s"registering worker $pair")
      workers = workers + pair
  }

  def handleCustomEvents(): Receive = {
    case MemberJoined(member) if member.hasRole("worker") =>
      log.info(s"new worker is up ${member.address}")
      val workerSelection = context.actorSelection(s"${member.address}/user/worker")
      workerSelection.resolveOne().map(ref => (member.address, ref)).pipeTo(self)

    case MemberUp(node) =>
      log.info(s"welcome node ${node.address}")
    case MemberRemoved(node, previousStatus) =>
      log.info(s"node ${node.address} it was removed from $previousStatus")
    case UnreachableMember(node) =>
      log.info(s" node ${node.address} is unreachable")
    case m: MemberEvent =>
      log.info(s" node event $m")
  }

  def handleJob(): Receive = {
    case ProcessFile(filename) => {
      val aggregator = context.actorOf(Props[Aggregator], s"aggregator${filename.split("/").last}")


      //val aggregator = context.actorOf(Props[Aggregator], "aggregator")
      // scala.io.Source.fromFile(filename).getLines().foreach { line =>
//       val line = Source(1 to 100).runForeach(line=>
//       self ! ProcessLine(line.toString , aggregator ))
//      TextParser.parse(filename)
//              .runWith(Sink.foreach( line=>
//                                self ! ProcessLine(line,aggregator)))
      val parser = Parser(filename)
      parser.parse(filename).onComplete{
        case Success(rawmodelList) =>
          rawmodelList.foreach(line =>
            self ! ProcessLine(line.data,aggregator))
        case Failure(exception) => exception.printStackTrace()

      }

    }

    case ProcessLine(line, aggregator) =>
      val workerIndex = Random.nextInt(workers.size)
      val worker: ActorRef = workers.values.toSeq(workerIndex)
      worker ! ProcessLine(line, aggregator)

  }

  def Init() : Receive = {
    case Initialize() =>
      val inputActor = context.actorOf(Props[InputActor], "inputFactory")
      inputActor ! GetInputFiles()
  }

}
