fun main(args: Array<String>) {
	var board = SudokuBoard()
	var boardConsole = BoardConsole(board)
	boardConsole.inputBoard()
	boardConsole.displayBoard()
	var iterations=0
	var uniqueKey=board.uniqueString()
	while (!board.isComplete() && iterations < 30) {
		println("Iteration " + ++iterations)
		board.fillCells()
		if (board.uniqueString()==uniqueKey) // no change
			break
		else
			uniqueKey= board.uniqueString()
		boardConsole.displayBoard()
	}
	println((if (board.isComplete()) "Succeeded" else "Failed") + " after $iterations iterations")
}