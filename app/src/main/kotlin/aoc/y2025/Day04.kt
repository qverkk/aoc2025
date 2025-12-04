package aoc.y2025

import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations

class Day04 : Puzzle {
    override val id = PuzzleId(2025, 4)

    override val sampleExpectations = SampleExpectations(
        part1 = "13",
        part2 = "43",
    )

    override fun part1(input: List<String>): String {
        val res = input.map { it.map { char -> char } }
        return countRollsOfPaper(res).first.toString()
    }

    data class Element(
        var char: Char,
        var row: Long,
        var col: Long,
    )

    private fun countRollsOfPaper(res: List<List<Char>>): Pair<Long, List<Pair<Int, Int>>> {
        val limit = 4
        val range = 1
        val cols = res.size
        val rows = res[0].size
        var result = 0L
        var replaceIndices = listOf<Pair<Int, Int>>()
        for (i in 0..<rows) {
            for (j in 0..<cols) {
                val hasPaper = res[i][j] == '@'
                if (!hasPaper) {
                    continue
                }

                val adajecentRow = i - range..i + range
                val adajecentCol = j - range..j + range
                var chars = listOf<Element>()
                adajecentRow.forEach { row ->
                    if (row in 0..<rows) {
                        adajecentCol.forEach { col ->
                            if ((row != i || col != j) && col in 0..<cols) {
                                chars = chars.plus(Element(res[row][col], row.toLong(), col.toLong()))
                            }
                        }
                    }
                }
                if (chars.count { it.char == '@' } < limit) {
                    result++
                    replaceIndices = replaceIndices.plus(Pair(i, j))
                }
            }
        }

        return result to replaceIndices
    }

    override fun part2(input: List<String>): String {
        var res = input.map { it.map { char -> char } }
        var papers = 0L
        while (true) {
            val (count, replacements) = countRollsOfPaper(res)
            if (count == 0L) {
                return papers.toString()
            }
            papers += count

            replacements.forEach {
                res = res
                    .mapIndexed { rowIndex, row ->
                        row.mapIndexed { colIndex, col ->
                            if (rowIndex == it.first && colIndex == it.second)
                                '.'
                            else
                                col
                        }
                    }
            }

        }
    }
}
