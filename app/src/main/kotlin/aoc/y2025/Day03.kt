package aoc.y2025

import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations

class Day03 : Puzzle {
    override val id = PuzzleId(2025, 3)

    override val sampleExpectations = SampleExpectations(
        part1 = "357",
        part2 = "3121910778619",
    )

    override fun part1(input: List<String>): String {
        return input
            .sumOf { line ->
                result(2, line)
            }.toString()
    }

    override fun part2(input: List<String>): String {
        return input
            .sumOf { line ->
                result(12, line)
            }.toString()
    }

    private fun result(initialRemainder: Int, line: String): Long {
        var result = ""
        var remaining = initialRemainder
        var currentLine = line

        while (remaining > 0) {
            val selection = currentLine.take(currentLine.length - remaining + 1)
            val max = selection.map { it.digitToInt() }.max()
            val index = selection.indexOf(max.toString())
            currentLine = currentLine.drop(index + 1)
            result += max

            remaining--
        }

        return result.toLong()
    }
}
