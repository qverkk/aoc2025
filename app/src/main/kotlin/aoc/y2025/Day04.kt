package aoc.y2025

import aoc.core.Direction
import aoc.core.Map2D
import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations
import aoc.core.toMap2D

class Day04 : Puzzle {
    override val id = PuzzleId(2025, 4)

    override val sampleExpectations = SampleExpectations(
        part1 = "13",
        part2 = "43",
    )

    private val map = lazy { input.map { it.toList() }.toMap2D() }
    private lateinit var input: List<String>

    override fun part1(input: List<String>): String {
        this.input = input
        return map.value.copy().runCleanupIteration().toString()
    }

    override fun part2(input: List<String>): String {
        this.input = input
        val mapCopy = map.value.copy()
        var papers = 0
        while (true) {
            val cleanedRolls = mapCopy.runCleanupIteration()
            if (cleanedRolls == 0) break
            papers += cleanedRolls
        }
        return papers.toString()
    }

    private fun Map2D<Char>.runCleanupIteration(): Int = allCoordinates()
        .filter { this.get(it) == '@' }
        .filter { paperPoint ->
            Direction.allDirections()
                .map { it.asPosition() + paperPoint }
                .map { this.get(it) }
                .count { it == '@' } < 4
        }
        .onEach { replace(it, '.') }
        .count()
}
