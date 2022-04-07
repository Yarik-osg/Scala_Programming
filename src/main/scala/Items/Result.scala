package Items

sealed trait Result {
  self =>
  val show: String = self match {
    case Result.Win(Piece.X) => "Cross wins!"
    case Result.Win(Piece.O) => "Nought wins!"
    case Result.Draw => "It's a draw!"
  }
}

object Result {
   case class Win(piece: Piece) extends Result

   case object Draw extends Result
}

