package aoc.y2025

import aoc.core.*

class Day07 : Puzzle {
    override val id = PuzzleId(2025, 7)

    override val sampleExpectations = SampleExpectations(
        part1 = "21",
        part2 = "",
    )

    override fun part1(input: List<String>): String {
        val map = input.map { it.toList() }.let(::Map2D)
        val queue = ArrayDeque<Position>().apply {
            add(map.find('S')!! + Direction.SOUTH.asPosition())
        }

        return generateSequence { queue.removeFirstOrNull() }
            .count { pos ->
                when (map[pos]) {
                    '.' -> {
                        map.replace(pos, '|')
                        queue.add(pos + Direction.SOUTH.asPosition())
                        false
                    }

                    '^' -> {
                        queue.add(pos + Direction.WEST.asPosition())
                        queue.add(pos + Direction.EAST.asPosition())
                        true
                    }

                    else -> false
                }
            }
            .toString()
    }

    override fun part2(input: List<String>): String {
        return ""
    }
}
