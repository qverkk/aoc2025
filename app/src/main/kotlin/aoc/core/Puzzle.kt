package aoc.core

data class PuzzleId(val year: Int, val day: Int) : Comparable<PuzzleId> {
    init {
        require(year >= 2015) { "Advent of Code started in 2015" }
        require(day in 1..25) { "Day must be between 1 and 25" }
    }

    val displayName: String = "Day %02d (%d)".format(day, year)
    val folderSegment: String = "day" + day.toString().padStart(2, '0')

    override fun compareTo(other: PuzzleId): Int =
        compareValuesBy(this, other, PuzzleId::year, PuzzleId::day)
}

data class SampleExpectations(val part1: String, val part2: String? = null)

interface Puzzle {
    val id: PuzzleId

    val sampleExpectations: SampleExpectations?
        get() = null

    fun part1(input: List<String>): String

    fun part2(input: List<String>): String
}

enum class InputFlavor(val fileName: String) {
    SAMPLE("sample.txt"),
    ACTUAL("input.txt");

    companion object {
        fun fromOption(value: String?): InputFlavor = when (value?.lowercase()) {
            null, "actual" -> ACTUAL
            "sample", "test", "example" -> SAMPLE
            else -> error("Unknown input flavor '$value'")
        }
    }
}

typealias PuzzleFactory = () -> Puzzle

data class PuzzleRegistration(val id: PuzzleId, val factory: PuzzleFactory)
