import model.Domain.{Initialize, ProcessFile}
import cluster.NodesInit.createNode
import akka.actor.Props
import cluster.{Master, Worker}

object Launcher extends App {

  val master = createNode(2551, "master", Props[Master], "master")
  createNode(2552, "worker", Props[Worker], "worker")
  createNode(2553, "worker", Props[Worker], "worker")
  Thread.sleep(10000)

  master ! Initialize()
}
