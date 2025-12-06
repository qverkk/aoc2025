package aoc

import aoc.core.InputFlavor
import aoc.core.InputRepository
import aoc.core.PuzzleId
import aoc.core.PuzzleLibrary

fun main(args: Array<String>) {
    val year = args.getOrNull(0)?.toIntOrNull() ?: 2025
    val day = args.getOrNull(1)?.toIntOrNull() ?: 6
    val flavor = InputFlavor.fromOption(args.getOrNull(2))

    val id = PuzzleId(year, day)
    val puzzle = PuzzleLibrary.build(id)
    val input = InputRepository.readLines(id, flavor)

    println("=== ${id.displayName} (${flavor.name}) ===")
    println()

    val part1Start = System.currentTimeMillis()
    val part1Result = puzzle.part1(input)
    val part1Time = System.currentTimeMillis() - part1Start
    println("Part 1: $part1Result  (${part1Time}ms)")

    val part2Start = System.currentTimeMillis()
    val part2Result = puzzle.part2(input)
    val part2Time = System.currentTimeMillis() - part2Start
    println("Part 2: $part2Result  (${part2Time}ms)")
}
