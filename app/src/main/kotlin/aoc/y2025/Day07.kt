package aoc.y2025

import aoc.core.Map2D
import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations
import aoc.core.toMap2D

class Day07 : Puzzle {
    override val id = PuzzleId(2025, 7)

    override val sampleExpectations = SampleExpectations(
        part1 = "21",
        part2 = "",
    )

    override fun part1(input: List<String>): String {
        val map = Map2D(input.map { it.split(" ").filter { it.isNotBlank() } })
        map.allCoordinates().forEach {
            println(it)
        }
        return ""
    }

    override fun part2(input: List<String>): String {
        return ""
    }
}
