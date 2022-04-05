package Items

import zio._

final case class Desk(cells: Map[Cell, Piece]) {
  self =>
  def cellIsNotFree(cell: Cell): Boolean = self.cells.contains(cell)

  def cellsOccupiedByPiece(piece: Piece): Set[Cell] =
    self.cells.collect {
      case (cell, `piece`) => cell
    }.toSet

  val isFull: Boolean = self.cells.size == 9

  val unoccupiedCells: List[Cell] = (Cell.All.toSet -- self.cells.keySet).toList.sortBy(_.value)

  def updated(cell: Cell, piece: Piece): Desk = Desk(self.cells.updated(cell, piece))
}

object Desk {
  val empty: Desk = Desk(Map.empty)

  val winnerCombinations: UIO[Set[Set[Cell]]] = {
    val horizontalWins = Set(
      Set(1, 2, 3),
      Set(4, 5, 6),
      Set(7, 8, 9)
    )

    val verticalWins = Set(
      Set(1, 4, 7),
      Set(2, 5, 8),
      Set(3, 6, 9)
    )

    val diagonalWins = Set(
      Set(1, 5, 9),
      Set(3, 5, 7)
    )

    ZIO {
      (horizontalWins ++ verticalWins ++ diagonalWins).map(_.map(i => Cell.make(i.toString).get))
    }.orDieWith(_ => new IllegalStateException)
  }
}