import org.testng.Assert.assertTrue
import org.testng.annotations.Test

fun main(args: Array<String>) {
	TestSudokuBoard().solvePuzzle()
}

fun initBoard(board:SudokuBoard)
{
	var rows= arrayOf(
		" 9    2 4",
		" 6 42    ",
		"     561 ",
		"3   67   ",
		"2       5",
		"   23   8",
		" 2 1     ",
		"    76 3 ",
		"1 7    4 "
	)
//	var rows= arrayOf(
//		" 3 5    2",
//		"5 93  8  ",
//		"46       ",
//		"  5  9 1 ",
//		"    6    ",
//		" 1 4  7  ",
//		"       75",
//		"  6  73 9",
//		"8    5 6 "
//	)

	for (j in 0..2) {
		for (y in 0..2) {
			var line = rows[j*3+y]
			for (i in 0..2) {
				for (x in 0..2) {
					if (line != null && line.length > i * 3 + x) {
						val char = line[i * 3 + x]
						if (char != ' ')
							board.numbers[i][j][x][y].setValue(char.digitToInt())
					}
				}
			}
		}
	}
}

class TestSudokuBoard {
	@Test fun solvePuzzle() {
		var board = SudokuBoard()
		var boardConsole = BoardConsole(board)

		initBoard(board)
		var iterations=0
		while (!board.isComplete() && iterations++ < 30) {
			boardConsole.displayBoard()
			board.fillCells()
		}
		boardConsole.displayBoard()

		assertTrue(board.isComplete())
	}
}