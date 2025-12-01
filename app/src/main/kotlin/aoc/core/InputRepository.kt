package aoc.core

object InputRepository {
    private const val BASE_PATH = "inputs"

    fun readLines(id: PuzzleId, flavor: InputFlavor): List<String> {
        val path = resourcePath(id, flavor)
        val stream = InputRepository::class.java.classLoader.getResourceAsStream(path)
            ?: error("Missing input file: $path")

        return stream.bufferedReader().use { reader ->
            reader.readLines()
        }
    }

    fun resourcePath(id: PuzzleId, flavor: InputFlavor): String =
        "$BASE_PATH/${id.year}/${id.folderSegment}/${flavor.fileName}"
}
