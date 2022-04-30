class BoardConsole (private val board:SudokuBoard) {
	fun inputBoard() {
		println("Enter numbers, one row at a time:")
		for (j in 0..2) {
			for (y in 0..2) {
				var line= readLine()
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
			if (j < 2) println("---------") else println()
		}
	}
	fun displayBoard() {
		for (j in 0..2) {
			for (y in 0..2) {
				for (i in 0..2) {
					for (x in 0..2) {
						val num= board.numbers[i][j][x][y].value
						print(if (num==0) ' ' else num)
					}
					if (i < 2) print("|")
				}
				println("")
			}
			if (j < 2) println("---|---|---") else println()
		}
	}
}