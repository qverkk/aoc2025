package aoc.core

object PuzzleLibrary {
    private val registrations: List<PuzzleRegistration> = listOf(
        PuzzleRegistration(PuzzleId(2025, 1)) { aoc.y2025.Day01() },
        PuzzleRegistration(PuzzleId(2025, 2)) { aoc.y2025.Day02() },
        PuzzleRegistration(PuzzleId(2025, 3)) { aoc.y2025.Day03() },
    )

    fun availableIds(): List<PuzzleId> = registrations.map { it.id }.sorted()

    fun build(id: PuzzleId): Puzzle =
        registrations.firstOrNull { it.id == id }?.factory?.invoke()
            ?: error("Puzzle not registered: ${id.displayName}")

    fun factories(): Sequence<Puzzle> = registrations.asSequence().map { it.factory.invoke() }
}
