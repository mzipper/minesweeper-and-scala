package model

import java.awt.Point
import java.util
import java.util.Random

import scala.jdk.CollectionConverters.IterableHasAsJava


class MineSweeperModel {
  private val MaxRowCount = 20
  private val MaxColCount = 24

  private val board =
    Array.tabulate(MaxRowCount, MaxColCount)((i, j) => new Cell(new Point(j, i)))

  private var numberOfBombs: Int = 0
  private var rowCount: Int = 0
  private var colCount: Int = 0

  private var listOfBombs = Vector.empty[Cell]
  private var numRemainingBombsFlagged: Int = 0

  private var numSpacesClicked: Int = 0
  private var numSpacesClickedSet: Set[Cell] = Set.empty[Cell]

  private val numOfRightClickOptions: Int = 2 //2 for Google. 3 for Microsoft.

  private val dr8 = Vector(-1, +1, 0, 0, -1, -1, +1, +1)
  private val dc8 = Vector(0, 0, -1, +1, -1, +1, -1, +1)


  def SetupNewBoard(gameLevel: GameLevel): Unit = {
    ClearBoard()
    gameLevel match {
      case GameLevel.NONE =>
        throw new NotImplementedError()

      case GameLevel.EASY =>
        numberOfBombs = 10
        rowCount = 8
        colCount = 10

      case GameLevel.MEDIUM =>
        numberOfBombs = 40
        rowCount = 14
        colCount = 18

      case GameLevel.HARD =>
        numberOfBombs = 99
        rowCount = 20
        colCount = 24

    }
    setNumRemainingBombsFlagged(numberOfBombs)
    AddBombs()
    AddNumbers()
  }

  private def ClearBoard() =
    for {
      i <- 0 until MaxRowCount
      j <- 0 until MaxColCount
    } {
      val cell = board(i)(j)
      cell.setCellStatus(CellStatus.BLANK)
      cell.setNumber(0)
      cell.setRightClickStatus(RightClickStatus.BLANK)
    }

  private def AddBombs() = {
    val random = new Random
    var numOfPlacedBombs = 0
    var cell: Cell = null
    while (numOfPlacedBombs < numberOfBombs) {
      do {
        cell = board(random.nextInt(rowCount))(random.nextInt(colCount))
      } while (cell.getCellStatus == CellStatus.BOMB)

      cell.setCellStatus(CellStatus.BOMB)
      numOfPlacedBombs += 1
      listOfBombs :+= cell
    }

    System.err.println(s"num of bombs: $numOfPlacedBombs")
  }

  private def AddNumbers() = {
    for (item <- listOfBombs) {
      val r = item.getLocation.y
      val c = item.getLocation.x
      for (i <- dr8.indices) {
        val row = r + dr8(i)
        val col = c + dc8(i)
        if (row >= 0 && col >= 0 && row < rowCount && col < colCount) {
          val cell = board(row)(col)
          if (cell.getCellStatus != CellStatus.BOMB) {
            cell.setCellStatus(CellStatus.NUMBER)
            cell.setNumber(cell.getNumber + 1)
          }
        }
      }
    }
  }

  def IsGameWon =
    numSpacesClicked + numberOfBombs == rowCount * colCount

  def LeftClick(location: Point): util.List[Cell] = {
    val list: util.List[Cell] = new util.LinkedList[Cell]
    var cell: Cell = null
    cell = board(location.y)(location.x)
    if (cell.getRightClickStatus == RightClickStatus.BLANK) {
      if (cell.getCellStatus == CellStatus.BOMB)
        list.add(cell)
      else if (cell.getCellStatus == CellStatus.NUMBER) {
        list.add(cell)
        numSpacesClicked += list.size
      } else {
        val coloredSet: util.Set[Cell] = new util.HashSet[Cell]
        val queue: util.Queue[Cell] = new util.LinkedList[Cell]

        queue.add(cell)
        coloredSet.add(cell)

        while (queue.size != 0) {
          cell = queue.poll()

          if (cell.getRightClickStatus == RightClickStatus.BLANK) {
            if (!numSpacesClickedSet.contains(cell)) {
              list.add(cell)
              numSpacesClickedSet += cell
            }

            if (cell.getCellStatus == CellStatus.BLANK) {
              val r = cell.getLocation.y
              val c = cell.getLocation.x

              for (i <- dr8.indices) {
                val row = r + dr8(i)
                val col = c + dc8(i)

                if (row >= 0 && col >= 0 && row < rowCount && col < colCount) {
                  cell = board(row)(col)
                  if (!coloredSet.contains(cell)) {
                    coloredSet.add(cell)
                    queue.add(cell)
                  }
                }
              }
            }
          }
        }
        numSpacesClicked += list.size
      }
    }
    list
  }

  def RightClick(location: Point) = {

    var rCS: RightClickStatus = null
    val cell = board(location.y)(location.x)

    rCS = cell.getRightClickStatus

    if (rCS == RightClickStatus.BLANK) setNumRemainingBombsFlagged(getNumRemainingBombsFlagged - 1)
    if (rCS == RightClickStatus.FLAG) setNumRemainingBombsFlagged(getNumRemainingBombsFlagged + 1)

    rCS = RightClickStatus.intToEnum((rCS.getNum + 1) % numOfRightClickOptions)
    cell.setRightClickStatus(rCS)

    rCS
  }

  def getBoard = board

  def getListOfBombs = listOfBombs.asJava

  def getNumRemainingBombsFlagged = numRemainingBombsFlagged

  def setNumRemainingBombsFlagged(numRemainingBombsFlagged: Int) =
    this.numRemainingBombsFlagged = numRemainingBombsFlagged
}
