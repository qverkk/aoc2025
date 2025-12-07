package aoc.y2025

import aoc.core.Map2D
import aoc.core.Puzzle
import aoc.core.PuzzleId
import aoc.core.SampleExpectations
import aoc.core.toMap2D

class Day06 : Puzzle {
    override val id = PuzzleId(2025, 6)

    override val sampleExpectations = SampleExpectations(
        part1 = "4277556",
        part2 = "3263827",
    )

    override fun part1(input: List<String>): String {
        val map = Map2D(input.map { it.split(" ").filter { it.isNotBlank() } })
        return map.columns()
            .map { solveExpression(operator(it.last()), it.take(it.size - 1).map { it.toLong() }) }
            .sum()
            .toString()
    }

    override fun part2(input: List<String>): String {
        val operators = input.last()
            .split(" ")
            .filter { it.isNotBlank() }
            .map { operator(it) }

        val map = Map2D(input.map { it.padEnd(input.maxOf { it.length }, ' ').map { it } })
        
        return map.columns()
            .joinToString("|") {
                it.filter { it != ' ' }
                    .joinToString("")
                    .replace("[+*]".toRegex(), "")
            }
            .split("||")
            .zip(operators)
            .sumOf { (line, op) ->
                solveExpression(op, line.split("|").map { it.toLong() })
            }
            .toString()
    }

    private fun operator(str: String): Operator = when (str) {
        "+" -> Operator.ADD
        "*" -> Operator.MULTIPLY
        else -> throw IllegalArgumentException("Invalid operator: $str")
    }

    private fun solveExpression(operator: Operator, numbers: List<Long>): Long =
        numbers.reduce(operator.operation)

    enum class Operator(val operation: (Long, Long) -> Long) {
        ADD({ a, b -> a + b }),
        MULTIPLY({ a, b -> a * b })
    }
}
