package Items

sealed trait State {
  self =>
  val isComputerTurn: Boolean = self match {
    case State.Ongoing(_, whoIsCross, turn) =>
      (turn == Piece.X && whoIsCross == Player.Computer) || (turn == Piece.O && whoIsCross == Player.Human)
    case State.Over(_) => false
  }
}

object State {
   case class Ongoing(desk: Desk, whoIsCross: Player, turn: Piece) extends State

   case class Over(desk: Desk) extends State
}