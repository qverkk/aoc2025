package aoc

import aoc.core.InputFlavor
import aoc.core.InputRepository
import aoc.core.PuzzleLibrary
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class PuzzleSampleTest : FunSpec({

    val puzzlesWithSamples = PuzzleLibrary.factories()
        .filter { it.sampleExpectations != null }
        .toList()

    context("Part 1 sample tests") {
        withData(
            nameFn = { it.id.displayName },
            puzzlesWithSamples.filter { it.sampleExpectations!!.part1.isNotBlank() }
        ) { puzzle ->
            val input = InputRepository.readLines(puzzle.id, InputFlavor.SAMPLE)
            val result = puzzle.part1(input)
            result shouldBe puzzle.sampleExpectations!!.part1
        }
    }

    context("Part 2 sample tests") {
        withData(
            nameFn = { it.id.displayName },
            puzzlesWithSamples.filter { it.sampleExpectations!!.part2 != null }
        ) { puzzle ->
            val input = InputRepository.readLines(puzzle.id, InputFlavor.SAMPLE)
            val result = puzzle.part2(input)
            result shouldBe puzzle.sampleExpectations!!.part2
        }
    }
})
