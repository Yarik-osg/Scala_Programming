package Items

sealed abstract class Cell(val value: Int)

object Cell {
  final case object NorthWest extends Cell(1)

  final case object North extends Cell(2)

  final case object NorthEast extends Cell(3)

  final case object West extends Cell(4)

  final case object Center extends Cell(5)

  final case object East extends Cell(6)

  final case object SouthWest extends Cell(7)

  final case object South extends Cell(8)

  final case object SouthEast extends Cell(9)

  def make(value: String): Option[Cell] = value match {
    case "1" => Some(NorthWest)
    case "2" => Some(North)
    case "3" => Some(NorthEast)
    case "4" => Some(West)
    case "5" => Some(Center)
    case "6" => Some(East)
    case "7" => Some(SouthWest)
    case "8" => Some(South)
    case "9" => Some(SouthEast)
    case _ => None
  }

  val All: List[Cell] = List(
    NorthWest,
    North,
    NorthEast,
    West,
    Center,
    East,
    SouthWest,
    South,
    SouthEast
  )
}
