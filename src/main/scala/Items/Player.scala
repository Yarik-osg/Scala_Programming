package Items

sealed trait Player

object Player {
   case object Computer extends Player

   case object Human extends Player
}