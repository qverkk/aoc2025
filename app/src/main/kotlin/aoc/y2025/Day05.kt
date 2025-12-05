package aoc.y2025

import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations

class Day05 : Puzzle {
    override val id = PuzzleId(2025, 5)

    override val sampleExpectations = SampleExpectations(
        part1 = "3",
        part2 = "14",
    )

    private fun List<Pair<Long, Long>>.inRange(value: Long): Boolean {
        forEach {
            if (value in it.first..it.second) {
                return true
            }
        }
        return false
    }

    override fun part1(input: List<String>): String {
        val ranges = mutableListOf<Pair<Long, Long>>()
        val numbers = mutableListOf<Long>()

        for (line in input) {
            if (line.isEmpty()) {
                continue
            }

            if (line.contains("-")) {
                val (start, end) = line.split("-")
                ranges.add(Pair(start.toLong(), end.toLong()))
            } else {
                numbers.add(line.toLong())
            }
        }

        return numbers.count { ranges.inRange(it) }.toString()
    }

    override fun part2(input: List<String>): String {
        val ranges = mutableListOf<Pair<Long, Long>>()

        for (line in input) {
            if (line.isEmpty()) {
                continue
            }

            if (line.contains("-")) {
                val (start, end) = line.split("-")
                ranges.add(Pair(start.toLong(), end.toLong()))
            }
        }

        return count(ranges)
    }

    private fun count(ranges: List<Pair<Long, Long>>): String {
        val merged = mergeRanges(ranges)
        val total = merged.sumOf { (start, end) -> end - start + 1 }
        return total.toString()
    }

    private fun mergeRanges(ranges: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
        if (ranges.isEmpty()) return emptyList()

        val sorted = ranges.sortedBy { it.first }
        val merged = mutableListOf(sorted.first())

        for ((start, end) in sorted.drop(1)) {
            val last = merged.last()
            if (start <= last.second + 1) {
                merged[merged.lastIndex] = Pair(last.first, maxOf(last.second, end))
            } else {
                merged.add(Pair(start, end))
            }
        }

        return merged
    }
}
