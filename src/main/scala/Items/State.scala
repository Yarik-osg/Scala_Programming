package Items

sealed trait State {
  self =>
  def isComputerTurn: Boolean = self match {
    case State.Ongoing(_, whoIsCross, turn) =>
      (turn == Piece.X && whoIsCross == Player.Computer) || (turn == Piece.O && whoIsCross == Player.Human)
    case State.Over(_) => false
  }
}

object State {
  final case class Ongoing(desk: Desk, whoIsCross: Player, turn: Piece) extends State

  final case class Over(desk: Desk) extends State
}