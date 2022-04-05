package Items

sealed abstract class Cell(val value: Int)

object Cell {
  final case object upLeft extends Cell(1)

  final case object Up extends Cell(2)

  final case object upRight extends Cell(3)

  final case object Left extends Cell(4)

  final case object Middle extends Cell(5)

  final case object Right extends Cell(6)

  final case object downLeft extends Cell(7)

  final case object Down extends Cell(8)

  final case object downRight extends Cell(9)

  def make(value: String): Option[Cell] = value match {
    case "1" => Some(upLeft)
    case "2" => Some(Up)
    case "3" => Some(upRight)
    case "4" => Some(Left)
    case "5" => Some(Middle)
    case "6" => Some(Right)
    case "7" => Some(downLeft)
    case "8" => Some(Down)
    case "9" => Some(downRight)
    case _ => None
  }

  val All: List[Cell] = List(
    upLeft,
    Up,
    upRight,
    Left,
    Middle,
    Right,
    downLeft,
    Down,
    downRight
  )
}
