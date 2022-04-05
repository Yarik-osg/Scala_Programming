import Items.State.{Ongoing, Over}
import Items._
import zio._
import zio.console._
import zio.random._

import java.io.IOException

object lab_1 extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] = (
    for {
      playerPiece <- choosePlayerPiece
      pieceThatGoesFirst <- whichPieceGoesFirst.tap(piece => putStrLn(s"$piece goes first"))
      initialState = State.Ongoing(
        Desk.empty,
        if (playerPiece == Piece.X) Player.Human else Player.Computer,
        pieceThatGoesFirst
      )
      _ <- programLoop(initialState)
    } yield ()).exitCode

  val choosePlayerPiece: ZIO[Console, IOException, Piece] =
//  : URIO[Console, Piece]
    for {
      input <- putStr("Do you want be X or O?: ") *> getStrLn.orDie
      piece <- ZIO.fromOption(Piece.make(input)) <> (putStrLn("Invalid input") *> choosePlayerPiece)
    } yield piece

  val whichPieceGoesFirst: URIO[Random, Piece] = nextBoolean.map {
    case true => Piece.X
    case false => Piece.O
  }

  def programLoop(state: State): ZIO[Random with Console, IOException, Unit] =
//  : URIO[Random with Console, Unit] =
    state match {
      case state@Ongoing(desk, _, _) => drawDesk(desk) *> step(state).flatMap(programLoop)
      case Over(desk) => drawDesk(desk)
    }

  def drawDesk(desk: Desk): ZIO[Console, IOException, Unit] =
//  : URIO[Console, Unit]
    putStrLn {
      Cell.All
        .map(cell => desk.cells.get(cell) -> cell.value)
        .map {
          case (Some(piece), _) => piece.toString
          case (None, value) => value.toString
        }
        .sliding(3, 3)
        .map(cells => s""" ${cells.mkString(" ║ ")} """)
        .mkString("\n═══╬═══╬═══\n")
    }

  def step(state: State.Ongoing): ZIO[Random with Console, IOException, State] =
//  : URIO[Random with Console, State] =
    for {
      nextMove <- if (state.isComputerTurn) getComputerMove(state.desk) else getPlayerMove(state.desk)
      nextState <- takeField(state, nextMove)
    } yield nextState

  def getComputerMove(desk: Desk):  ZIO[Console with Random, IOException, Cell] =
//  : URIO[Random with Console, Cell] =
    nextIntBounded(desk.unoccupiedcells.size)
      .map(desk.unoccupiedcells(_)).zipLeft(putStrLn("Waiting for computer`s move, please press Enter...") <* getStrLn.orDie)

  def getPlayerMove(desk: Desk): ZIO[Console, IOException, Cell] =
//  : URIO[Console, Cell] =
    for{
      input <- putStr("What`s your next move? (1-9): " ) *> getStrLn.orDie
      tmpCell <- ZIO.fromOption(Cell.make(input)) <> ( putStrLn("Invalid input") *> getPlayerMove(desk))
      cell <- if (desk.cellIsNotFree(tmpCell)) putStrLn("That cell has already been used") *> getPlayerMove(desk)
      else ZIO.succeed(tmpCell)
    } yield cell

  def takeField(state: State.Ongoing, cell: Cell): ZIO[Console, IOException, State] =
//  : URIO[Console, State] =
    for {
      updatedDesk <- ZIO.succeed(state.desk.updated(cell, state.turn))
      updatedTurn <- ZIO.succeed(state.turn.next)
      gameResult <- getGameResult(updatedDesk)
      nextState <- gameResult match {
        case Some(gameResult) => putStrLn(gameResult.show).as(State.Over(updatedDesk))
        case None => ZIO.succeed(state.copy(desk = updatedDesk, turn = updatedTurn))
      }
    } yield nextState

  def getGameResult(desk: Desk): UIO[Option[Result]] =
    for {
      crossWin <- isWinner(desk, Piece.X)
      noughtWin <- isWinner(desk, Piece.O)
      gameResult <- if (crossWin && noughtWin)
        ZIO.die(new IllegalStateException("It should not be possible for both players to win!"))
      else if (crossWin) UIO.succeed(Result.Win(Piece.X)).asSome
      else if (noughtWin) UIO.succeed(Result.Win(Piece.O)).asSome
      else if (desk.isFull) UIO.succeed(Result.Draw).asSome
      else UIO.none
    } yield gameResult

  def isWinner(desk: Desk, piece: Piece): UIO[Boolean] =
    Desk.winnerCombinations.map(combinations => combinations.exists(_ subsetOf desk.cellsOccupiedByPiece(piece)))
}