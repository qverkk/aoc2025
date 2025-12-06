package aoc.y2025

import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations

class Day06 : Puzzle {
    override val id = PuzzleId(2025, 6)

    override val sampleExpectations = SampleExpectations(
        part1 = "4277556",
        part2 = "3263827",
    )

    override fun part1(input: List<String>): String {
        val map = parseToColumnMap(input)
        val result = map.values.sumOf { calculateColumn(it) }

        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val operations = input.takeLast(1)
            .flatMap { it.split(" ") }
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val verticalNumbers = getVerticalNumbers(input)
        val blocks = parseToBlocks(verticalNumbers)

        var index = 0
        return blocks.windowed(3, 3).sumOf {
            val operator = operations[index++]
            val blockResult = when (operator) {
                "*" -> it.reduce { acc, i -> acc * i }
                "+" -> it.reduce { acc, i -> acc + i }
                else -> 0L
            }
            blockResult
        }.toString()
        // alternative implementation
//        val maxWidth = input.maxOfOrNull { it.length } ?: 0
//        val paddedRows = input.map { it.padEnd(maxWidth, ' ') }
//
//        val allBlockResults = mutableListOf<Long>()
//        var currentVerticalNumbers = mutableListOf<Long>()
//        var currentOperator: Char? = null
//
//        for (colIndex in 0..maxWidth) {
//            val verticalSlice = if (colIndex < maxWidth) {
//                paddedRows.map { it[colIndex] }
//            } else {
//                List(input.size) { ' ' }
//            }
//
//            val digits = verticalSlice.filter { it.isDigit() }
//            val operatorInColumn = verticalSlice.firstOrNull { it == '*' || it == '+' }
//
//            if (digits.isNotEmpty()) {
//                val numberString = digits.joinToString("")
//                currentVerticalNumbers.add(numberString.toLong())
//
//                if (operatorInColumn != null && currentOperator == null) {
//                    currentOperator = operatorInColumn
//                }
//
//            } else if (verticalSlice.all { it.isWhitespace() || it == '*' || it == '+' } && currentVerticalNumbers.isNotEmpty()) {
//                if (currentOperator != null) {
//                    val result = when (currentOperator) {
//                        '*' -> currentVerticalNumbers.reduce { acc, i -> acc * i }
//                        '+' -> currentVerticalNumbers.reduce { acc, i -> acc + i }
//                        else -> 0L
//                    }
//                    allBlockResults.add(result)
//                }
//
//                currentVerticalNumbers = mutableListOf()
//                currentOperator = null
//            }
//        }
//
//        return allBlockResults.sum().toString()
    }

    fun parseToColumnMap(rows: List<String>): Map<Int, List<String>> {
        val matrix = rows.map { rowString ->
            rowString.trim().split("\\s+".toRegex())
        }

        val map = mutableMapOf<Int, MutableList<String>>()
        val numCols = matrix.firstOrNull()?.size ?: 0

        for (i in 0 until numCols) {
            val colList = mutableListOf<String>()
            for (row in matrix) {
                if (i < row.size) {
                    colList.add(row[i])
                }
            }
            map[i] = colList
        }

        return map
    }

    fun calculateColumn(data: List<String>): Long {
        val numbers = data.dropLast(1).map { it.toLong() }
        val operator = data.last()

        return when (operator) {
            "*" -> numbers.reduce { acc, i -> acc * i }
            "+" -> numbers.reduce { acc, i -> acc + i }
            else -> 0L
        }
    }

    fun parseToBlocks(rows: List<String>): List<Long> {
        val matrix = rows.map { it.trim().split("\\s+".toRegex()) }

        val numCols = matrix[0].size
        val colList = mutableListOf<Long>()
        for (i in 0 until numCols) {
            for (row in matrix) {
                if (i < row.size) {
                    val str = row[i]
                    if (str.isEmpty()) continue
                    colList.add(str.toLong())
                }
            }
        }
        return colList
    }

    fun getVerticalNumbers(numbers: List<String>): List<String> {
        val maxLength = numbers.maxOf { it.length }

        val verticalNumbers = mutableListOf<String>()

        for (col in 0 until maxLength) {
            val sb = StringBuilder()

            var started = true
            for (rowStr in numbers) {
                val char = if (col < rowStr.length) rowStr[col] else ' '
                if (char.isWhitespace()) {
                    if (started) {
                        sb.append(char)
                    } else {
                        started = true
                    }
                } else if (char.isDigit()) {
                    sb.append(char)
                    started = false
                }
            }

            if (sb.isNotEmpty()) {
                verticalNumbers.add(sb.toString())
            }
        }

        return verticalNumbers
    }
}
