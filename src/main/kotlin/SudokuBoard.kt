class Cell {
	var possibles = mutableSetOf<Int>(1,2,3,4,5,6,7,8,9)
	val valueIsKnown: Boolean
		get() {
			return (possibles.size == 1)
		}
	val value: Int
		get() {
			if (valueIsKnown)
				return possibles.toIntArray()[0]
			return 0
		}
	fun setValue(n:Int) {
		possibles = mutableSetOf(n)
	}
	fun notPossible(n:Int) {
		possibles.remove(n)
		if (possibles.isEmpty())
			assert(false)
	}
}

class ExclusiveNine(private val nineCells : Array<Cell>) {
	fun reducePossibles() {
		for (cell in nineCells) {
			if (cell.valueIsKnown) {
				removeFromAllExcept(setOf(cell.value), setOf(cell))
			}
		}
	}
	fun findExclusivePairs() {
		for (i in 0..7) {
			if (nineCells[i].possibles.size==2) {
				for (j in i+1..8) {
					if (nineCells[i].possibles == nineCells[j].possibles) {
						removeFromAllExcept(nineCells[i].possibles, setOf(nineCells[i],nineCells[j]))
					}
				}
			}
		}
	}
	fun findExclusiveTriples() {
		for (i in 0..6) {
			if (!nineCells[i].valueIsKnown) {
				for (j in i+1..7) {
					if (!nineCells[j].valueIsKnown) {
						for (k in j+1..8) {
							if (!nineCells[k].valueIsKnown) {
								val allPossibles = nineCells[i].possibles union nineCells[j].possibles union nineCells[k].possibles
								if (allPossibles.size == 3) {
									removeFromAllExcept(allPossibles, setOf(nineCells[i], nineCells[j], nineCells[k]))
								}
							}
						}
					}
				}
			}
		}
	}
	fun forceValues() {
		for (n in 1..9) {
			var possibleCells = findPossibleCells(n)
			if (possibleCells.size==1)
				possibleCells[0].setValue(n)
		}
	}

	fun forcePairs() {
		for (n in 1..8) {
			var nPossibleCells = findPossibleCells(n)
			if (nPossibleCells.size==2) {
				for (m in nPossibleCells[0].possibles) {
					if (m!=n) {
						var mPossibleCells = findPossibleCells(m)
						if (mPossibleCells.size==2 && nPossibleCells.contains(mPossibleCells[0]) && nPossibleCells.contains(mPossibleCells[1])) {
							mPossibleCells[0].possibles.removeIf{ it != m && it != n }
							mPossibleCells[1].possibles.removeIf{ it != m && it != n }
							break
						}
					}
				}
			}
		}
	}

	private fun findPossibleCells(n: Int): Array<Cell> {
		var possibleCells = arrayOf<Cell>()
		for (cell in nineCells) {
			if (n in cell.possibles) {
				possibleCells += cell
			}
		}
		return possibleCells
	}

	private fun removeFromAllExcept(values:Set<Int>, cells:Set<Cell>) {
		for (otherCell in nineCells) {
			if (!cells.contains(otherCell)) {
				for (v in values) {
					otherCell.notPossible(v)
				}
			}
		}
	}
}

class SudokuBoard {
	var numbers = arrayOf<Array<Array<Array<Cell>>>>()
	private var allNines : Set<ExclusiveNine>

	init {
		for (i in 0..2) {
			var iArr = arrayOf<Array<Array<Cell>>>()
			for (j in 0..2) {
				var jArr = arrayOf<Array<Cell>>()
				for (x in 0..2) {
					var xArr = arrayOf<Cell>()
					for (y in 0..2) {
						xArr += Cell()
					}
					jArr += xArr
				}
				iArr += jArr
			}
			numbers += iArr
		}
		allNines = getAllNines()
	}

	fun setValue(i:Int, j:Int, x:Int, y:Int, value:Int) {
		numbers[i][j][x][y].setValue(value)
	}
	fun isComplete() : Boolean {
		for (j in 0..2) {
			for (y in 0..2) {
				for (i in 0..2) {
					for (x in 0..2) {
						if (!numbers[i][j][x][y].valueIsKnown)
							return false
					}
				}
			}
		}
		return true
	}
	private fun getAllNines() : Set<ExclusiveNine> {
		var allNines = setOf<ExclusiveNine>()
		//rows
		var cellSet : Array<Cell>
		for (row:Int in 0..8) {
			cellSet = arrayOf<Cell>()
			for (col:Int in 0..8) {
				cellSet += numbers[col/3][row/3][col%3][row%3]
			}
			allNines += ExclusiveNine(cellSet)
		}
		//columns
		for (col:Int in 0..8) {
			cellSet = arrayOf<Cell>()
			for (row:Int in 0..8) {
				cellSet += numbers[col/3][row/3][col%3][row%3]
			}
			allNines += ExclusiveNine(cellSet)
		}
		//boxes
		for (box:Int in 0..8) {
			cellSet = arrayOf<Cell>()
			for (pos:Int in 0..8) {
				cellSet += numbers[box%3][box/3][pos%3][pos/3]
			}
			allNines += ExclusiveNine(cellSet)
		}

		return allNines
	}
	fun fillCells() {
		for (nine in allNines) {
			nine.reducePossibles()
			nine.forcePairs()
			nine.findExclusivePairs()
			nine.findExclusiveTriples()
			nine.forceValues()
		}
		extendRestrictedNumbers()
	}
	fun extendRestrictedNumbers() {
		for (n in 1..9) {
			for (i in 0..2) {
				for (j in 0..2) {
					var row= numConstrainedInBox(n, i, j, true)
					if (row != -1) {
						for (newI in 0..2) {
							if (newI != i) {
								removeNumFromRow(n,newI,j,row)
							}
						}
					}
					var col= numConstrainedInBox(n, i, j, false)
					if (col != -1) {
						for (newJ in 0..2) {
							if (newJ != j) {
								removeNumFromCol(n,i,newJ,col)
							}
						}
					}
				}
			}
		}
	}
	fun removeNumFromRow(n:Int, i:Int, j:Int, y:Int) {
		for (x in 0..2) {
			numbers[i][j][x][y].notPossible(n)
		}
	}
	fun removeNumFromCol(n:Int, i:Int, j:Int, x:Int) {
		for (y in 0..2) {
			numbers[i][j][x][y].notPossible(n)
		}
	}
	fun numConstrainedInBox(n:Int, i:Int, j:Int, rowOrColumn:Boolean) : Int {
		var row= -1
		for (x in 0..2) {
			for (y in 0..2) {
				var curRowVal= if (rowOrColumn) y else x
				var cell= numbers[i][j][x][y]
				if (cell.valueIsKnown) {
					if (cell.value == n)
						return -1
				}
				else
				{
					if (cell.possibles.contains(n)) {
						if (row == -1)
							row = curRowVal
						else if (row != curRowVal)
							return -1
					}
				}
			}
		}
		return row
	}
	fun uniqueString() : String {
		var retVal = ""
		for (j in 0..2) {
			for (y in 0..2) {
				for (i in 0..2) {
					for (x in 0..2) {
						for (n in numbers[i][j][x][y].possibles)
							retVal += n
						retVal += "|"
					}
				}
			}
		}
		return retVal
	}
}
