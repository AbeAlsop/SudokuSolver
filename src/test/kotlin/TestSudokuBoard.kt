import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class TestSudokuBoard {
	@Test fun easyPuzzle() {
		var board = SudokuBoard()
		var boardConsole = BoardConsole(board)

		board.setValue(0,0,0,0,6)
		board.setValue(0,0,1, 0, 1)
		board.setValue(0,0,0,1,2)
		board.setValue(0,0,1,2,3)
		board.setValue(1,0,0,0,2)
		board.setValue(1,2,2,0,3)
		board.setValue(1,0,0,1,7)
		board.setValue(1,0,1,1,6)
		board.setValue(1,0,2,2,9)
		board.setValue(2,0,2,0,7)
		board.setValue(2,0,0,2,2)
		board.setValue(2,0,1,2,5)
		board.setValue(0,1,1,0,8)
		board.setValue(0,1,2,0,7)
		board.setValue(0,1,1,1,2)
		board.setValue(0,1,0,2,4)
		board.setValue(0,1,2,2,3)
		board.setValue(1,1,0,0,5)
		board.setValue(1,1,2,2,1)
		board.setValue(2,1,0,0,1)
		board.setValue(2,1,2,0,3)
		board.setValue(2,1,1,1,4)
		board.setValue(2,1,0,2,8)
		board.setValue(2,1,1,2,7)
		board.setValue(0,2,1,0,7)
		board.setValue(0,2,2,0,6)
		board.setValue(0,2,0,2,5)
		board.setValue(1,2,0,0,4)
		board.setValue(1,2,1,1,1)
		board.setValue(1,2,2,1,7)
		board.setValue(1,2,0,2,8)
		board.setValue(1,2,2,2,2)
		board.setValue(2,2,1,0,1)
		board.setValue(2,2,2,1,8)
		board.setValue(2,2,1,2,6)
		board.setValue(2,2,2,2,4)

		var iterations=0
		while (!board.isComplete() && iterations++ < 30) {
			boardConsole.displayBoard()
			board.fillCells()
		}

		assertTrue(board.isComplete())
	}
}