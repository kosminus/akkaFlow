import akka.actor.ActorSystem

package object parser {
  implicit val actorSystem = ActorSystem("akkaPolyFlowCluster")
}
