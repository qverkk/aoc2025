package aoc.core

class Map2D<T> {
    val rows: Int
    val cols: Int
    private val fields: HashMap<Position, T>

    constructor(input: List<List<T>>) {
        if (input.isEmpty()) throw IllegalArgumentException("Input must not be empty")
        rows = input.size
        cols = input[0].size
        val mappedFields = input.flatMapIndexed { row, rowData ->
            if (rowData.size != cols) throw IllegalArgumentException("All rows must have the same size")
            rowData.mapIndexed { col, value -> Position(row, col) to value }
        }.toMap()
        fields = HashMap(mappedFields)
    }

    private constructor(map2D: Map2D<T>) {
        rows = map2D.rows
        cols = map2D.cols
        fields = HashMap(map2D.fields)
    }

    operator fun get(row: Int, col: Int): T? = fields[Position(row, col)]
    
    operator fun get(position: Position): T? = fields[position]
    
    fun getOrNull(row: Int, col: Int): T? = fields[Position(row, col)]
    
    fun getOrNull(position: Position): T? = fields[position]
    
    fun isValid(row: Int, col: Int): Boolean = 
        row in 0 until rows && col in 0 until cols
    
    fun isValid(position: Position): Boolean = isValid(position.row, position.col)
    
    fun find(value: T): Position? = fields.entries.find { it.value == value }?.key
    
    fun allCoordinates(): List<Position> = 
        (0 until rows).flatMap { row -> (0 until cols).map { col -> Position(row, col) } }
    
    fun values(): Collection<T> = fields.values
    
    fun columns(): List<List<T>> = 
        (0 until cols).map { col -> (0 until rows).map { row -> get(row, col)!! } }
    
    fun replace(position: Position, newValue: T) {
        fields[position] = newValue
    }
    
    fun replace(row: Int, col: Int, newValue: T) {
        replace(Position(row, col), newValue)
    }
    
    fun copy(): Map2D<T> = Map2D(this)
    
    fun getNeighbors(row: Int, col: Int, includeDiagonal: Boolean = false): List<Pair<Position, T>> {
        return Position(row, col)
            .neighbors(includeDiagonal)
            .filter { isValid(it) }
            .mapNotNull { pos -> get(pos)?.let { pos to it } }
    }
    
    fun getNeighborsInRange(row: Int, col: Int, range: Int, includeDiagonal: Boolean = false): List<Pair<Position, T>> {
        return Position(row, col)
            .neighborsInRange(range, includeDiagonal)
            .filter { isValid(it) }
            .mapNotNull { pos -> get(pos)?.let { pos to it } }
    }
    
    fun forEachIndexed(block: (row: Int, col: Int, value: T) -> Unit) {
        fields.forEach { (position, value) ->
            block(position.row, position.col, value)
        }
    }
    
    fun mapIndexed(transform: (row: Int, col: Int, value: T) -> T): Map2D<T> {
        val newFields = fields.mapValues { (position, value) ->
            transform(position.row, position.col, value)
        }
        return Map2D<T>(this).also { it.fields.clear(); it.fields.putAll(newFields) }
    }
    
    fun toList(): List<List<T>> = 
        (0 until rows).map { row -> (0 until cols).map { col -> get(row, col)!! } }
    
    fun findPositions(predicate: (T) -> Boolean): List<Position> =
        fields.filter { (_, value) -> predicate(value) }.keys.toList()
    
    override fun toString(): String =
        (0 until rows).joinToString("\n") { row ->
            (0 until cols).joinToString(" ") { col -> get(row, col).toString() }
        }
}

fun <T> List<List<T>>.toMap2D(): Map2D<T> = Map2D(this)

