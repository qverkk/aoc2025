# Advent of Code

Kotlin solutions for [Advent of Code](https://adventofcode.com/).

## Project Structure

```
app/src/main/
├── kotlin/aoc/
│   ├── core/           # Framework code
│   │   ├── Puzzle.kt         # Puzzle interface and models
│   │   ├── PuzzleLibrary.kt  # Puzzle registry
│   │   └── InputRepository.kt # Input file loading
│   ├── y2025/          # 2025 solutions
│   │   └── Day01.kt
│   └── Main.kt         # CLI entry point
└── resources/inputs/
    └── 2025/day01/
        ├── sample.txt  # Sample input for testing
        └── input.txt   # Your personal puzzle input

app/src/test/kotlin/aoc/
└── PuzzleSampleTest.kt # Kotest tests against sample expectations

scripts/
└── fetch_puzzle.py     # Fetch puzzle descriptions
```

## Running Solutions

```bash
# Run a specific day with actual input
./gradlew run --args="2025 1"

# Run with sample input
./gradlew run --args="2025 1 sample"
```

## Testing

Tests verify your solutions against the sample inputs with expected results.

```bash
# Run all tests
./gradlew test

# Run with verbose output
./gradlew test --info
```

## Adding a New Puzzle

1. **Fetch the puzzle description:**
   ```bash
   python scripts/fetch_puzzle.py 2025 1
   # Or with authentication for Part 2:
   python scripts/fetch_puzzle.py 2025 1 --cookie "session=YOUR_SESSION_COOKIE"
   ```

2. **Create input files:**
   - `app/src/main/resources/inputs/2025/day01/sample.txt` - Sample input from puzzle
   - `app/src/main/resources/inputs/2025/day01/input.txt` - Your puzzle input

3. **Create solution class** in `app/src/main/kotlin/aoc/y2025/Day01.kt`:
   ```kotlin
   package aoc.y2025

   import aoc.core.Puzzle
   import aoc.core.PuzzleId
   import aoc.core.SampleExpectations

   class Day01 : Puzzle {
       override val id = PuzzleId(2025, 1)

       override val sampleExpectations = SampleExpectations(
           part1 = "expected_part1_answer",
           part2 = "expected_part2_answer"  // null if not yet known
       )

       override fun part1(input: List<String>): String {
           // Your solution here
           return "answer"
       }

       override fun part2(input: List<String>): String {
           // Your solution here
           return "answer"
       }
   }
   ```

4. **Register the puzzle** in `PuzzleLibrary.kt`:
   ```kotlin
   private val registrations: List<PuzzleRegistration> = listOf(
       PuzzleRegistration(PuzzleId(2025, 1)) { aoc.y2025.Day01() },
       // Add more puzzles here
   )
   ```

5. **Run tests** to verify against sample:
   ```bash
   ./gradlew test
   ```

## Fetching Puzzle Descriptions

The `fetch_puzzle.py` script downloads puzzle descriptions and converts them to markdown.

```bash
# Basic usage (Part 1 only)
python scripts/fetch_puzzle.py https://adventofcode.com/2025/day/1
python scripts/fetch_puzzle.py 2025 1

# With authentication (to get Part 2)
python scripts/fetch_puzzle.py 2025 1 --cookie "session=YOUR_SESSION"

# Using environment variable
export AOC_SESSION="your_session_cookie_value"
python scripts/fetch_puzzle.py 2025 1

# Custom output path
python scripts/fetch_puzzle.py 2025 1 -o my_puzzle.md
```

To get your session cookie:
1. Log in to [adventofcode.com](https://adventofcode.com)
2. Open browser dev tools (F12)
3. Go to Application/Storage → Cookies
4. Copy the `session` cookie value
