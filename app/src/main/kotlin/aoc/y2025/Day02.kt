package aoc.y2025

import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations

class Day02 : Puzzle {
    override val id = PuzzleId(2025, 2)

    override val sampleExpectations = SampleExpectations(
        part1 = "1227775554",
        part2 = "4174379265",
//        part2 = "33",
    )

    override fun part1(input: List<String>): String {
        val line = input.first()
        return line.split(",")
            .map { it.split("-") }
            .sumOf { (min, max) ->
                (min.toLong()..max.toLong())
                    .filter { matchesModulo(it.toString()) }
                    .sum()
            }
            .toString()
    }

    fun matchesModulo(number: String): Boolean {
        val len = number.length
        if (len % 2 != 0) return false
        val firstPart = number.take(len / 2)
        val secondPart = number.substring(len / 2)
        return firstPart == secondPart
    }

    override fun part2(input: List<String>): String {
        val line = input.first()
        return line.split(",")
            .map { it.split("-") }
            .sumOf { (min, max) ->
                (min.toLong()..max.toLong())
                    .filter {
                        matchesModulo(it.toString())
                                || matchesTri(it.toString())
                                || allMatch(it.toString())
                                || allDoubles(it.toString())
                    }
                    .also { println("Sum $it") }
                    .sum()
            }
            .toString()
    }

    fun allMatch(number: String): Boolean {
        if (number.length < 2) return false
        val first = number.first()
        return number.all { it == first }
    }

    fun allDoubles(number: String): Boolean {
        val length = number.length
        if (length % 2 != 0 || length < 3) return false
        val pair = number.take(2)
        return number.substring(2)
            .windowed(2, 2)
            .all { it == pair }
    }

    fun matchesTri(number: String): Boolean {
        val len = number.length
        if (len % 3 != 0) return false
        val splitBy = len / 3
        val firstPart = number.take(splitBy)
        val secondPart = number.substring(splitBy, 2 * splitBy)
        val thirdPart = number.substring(2 * splitBy)

        return firstPart == secondPart && secondPart == thirdPart
    }
}
