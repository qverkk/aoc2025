package aoc.y2025

import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations

class Day01 : Puzzle {
    override val id = PuzzleId(2025, 1)

    override val sampleExpectations = SampleExpectations(
        part1 = "3",
        part2 = "6",
    )

    override fun part1(input: List<String>): String {
        var start = 50
        var result = 0
        input.forEach {
            start = when (it.contains("L")) {
                true -> (start - it.substring(1).toInt() + 100) % 100
                false -> (start + it.substring(1).toInt()) % 100
            }

            if (start == 0) {
                result++
            }
        }
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        var position = 50
        var hits = 0

        input.forEach { instruction ->
            val steps = instruction.drop(1).toInt()
            val fullRevolutions = steps / 100
            val partial = steps % 100
            hits += fullRevolutions

            val isLeftTurn = instruction.first() == 'L'
            val newPosition = if (isLeftTurn) {
                (position - partial + 100) % 100
            } else {
                (position + partial) % 100
            }

            val wrappedZero = if (isLeftTurn) {
                (newPosition > position || newPosition == 0) && position != 0
            } else {
                newPosition < position
            }

            if (wrappedZero) hits++
            position = newPosition
        }

        return hits.toString()
    }
}
