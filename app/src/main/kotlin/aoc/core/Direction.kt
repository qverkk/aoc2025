package aoc.core

enum class Direction(val rowDelta: Int, val colDelta: Int) {
    NORTH(-1, 0),
    SOUTH(1, 0),
    EAST(0, 1),
    WEST(0, -1),
    NORTH_EAST(-1, 1),
    NORTH_WEST(-1, -1),
    SOUTH_EAST(1, 1),
    SOUTH_WEST(1, -1);

    fun opposite(): Direction = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        EAST -> WEST
        WEST -> EAST
        NORTH_EAST -> SOUTH_WEST
        NORTH_WEST -> SOUTH_EAST
        SOUTH_EAST -> NORTH_WEST
        SOUTH_WEST -> NORTH_EAST
    }

    fun rotateClockwise(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
        NORTH_EAST -> SOUTH_EAST
        SOUTH_EAST -> SOUTH_WEST
        SOUTH_WEST -> NORTH_WEST
        NORTH_WEST -> NORTH_EAST
    }

    fun rotateCounterClockwise(): Direction = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
        NORTH_EAST -> NORTH_WEST
        NORTH_WEST -> SOUTH_WEST
        SOUTH_WEST -> SOUTH_EAST
        SOUTH_EAST -> NORTH_EAST
    }

    fun asPosition(): Position = Position(rowDelta, colDelta)

    companion object {
        val CARDINAL = listOf(NORTH, SOUTH, EAST, WEST)
        val DIAGONAL = listOf(NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST)
        val ALL = values().toList()
        
        fun allDirections(includeDiagonal: Boolean = true): List<Direction> = 
            if (includeDiagonal) ALL else CARDINAL
    }
}

data class Position(val row: Int, val col: Int) {
    fun move(direction: Direction): Position = Position(row + direction.rowDelta, col + direction.colDelta)
    
    fun move(direction: Direction, steps: Int): Position = Position(row + direction.rowDelta * steps, col + direction.colDelta * steps)
    
    fun neighbors(includeDiagonal: Boolean = false): List<Position> {
        val directions = if (includeDiagonal) Direction.ALL else Direction.CARDINAL
        return directions.map { move(it) }
    }
    
    fun neighborsInRange(range: Int, includeDiagonal: Boolean = false): List<Position> {
        val result = mutableListOf<Position>()
        for (dr in -range..range) {
            for (dc in -range..range) {
                if (dr == 0 && dc == 0) continue
                if (!includeDiagonal && dr != 0 && dc != 0) continue
                result.add(Position(row + dr, col + dc))
            }
        }
        return result
    }
    
    operator fun plus(other: Position): Position = Position(row + other.row, col + other.col)
    
    operator fun minus(other: Position): Position = Position(row - other.row, col - other.col)
}

