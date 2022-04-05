package Items

sealed trait Result {
  self =>
  def show: String = self match {
    case Result.Win(Piece.X) => "Cross wins!"
    case Result.Win(Piece.O) => "Nought wins!"
    case Result.Draw => "It's a draw!"
  }
}

object Result {
  final case class Win(piece: Piece) extends Result

  final case object Draw extends Result
}

