#!/usr/bin/env python3
"""
Fetch Advent of Code puzzle content and convert to markdown.

Usage:
    python fetch_puzzle.py https://adventofcode.com/2025/day/1
    python fetch_puzzle.py https://adventofcode.com/2025/day/1 --cookie "session=abc123..."
    python fetch_puzzle.py 2025 1  # shorthand for year and day
    python fetch_puzzle.py 2025 1 --cookie "session=abc123..."

The cookie can also be set via the AOC_SESSION environment variable.
"""

import argparse
import os
import re
import sys
from pathlib import Path
from urllib.request import Request, urlopen
from urllib.error import HTTPError
from html.parser import HTMLParser


class ArticleExtractor(HTMLParser):
    """Extract article content from AoC puzzle page."""
    
    def __init__(self):
        super().__init__()
        self.in_article = False
        self.depth = 0
        self.articles: list[str] = []
        self.current_article: list[str] = []
        
    def handle_starttag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        if tag == "article":
            self.in_article = True
            self.depth = 1
            self.current_article = []
        elif self.in_article:
            self.depth += 1
            attr_str = ""
            for name, value in attrs:
                if value:
                    attr_str += f' {name}="{value}"'
                else:
                    attr_str += f' {name}'
            self.current_article.append(f"<{tag}{attr_str}>")
    
    def handle_endtag(self, tag: str) -> None:
        if self.in_article:
            if tag == "article" and self.depth == 1:
                self.articles.append("".join(self.current_article))
                self.in_article = False
            else:
                self.current_article.append(f"</{tag}>")
            self.depth -= 1
    
    def handle_data(self, data: str) -> None:
        if self.in_article:
            self.current_article.append(data)


def html_to_markdown(html: str) -> str:
    """Convert HTML to markdown with simple rules."""
    # Handle headers
    html = re.sub(r'<h2[^>]*>--- (.*?) ---</h2>', r'## \1\n', html)
    html = re.sub(r'<h2[^>]*>(.*?)</h2>', r'## \1\n', html)
    
    # Handle emphasis
    html = re.sub(r'<em[^>]*>(.*?)</em>', r'**\1**', html, flags=re.DOTALL)
    html = re.sub(r'<strong[^>]*>(.*?)</strong>', r'**\1**', html, flags=re.DOTALL)
    
    # Handle code
    html = re.sub(r'<code[^>]*>(.*?)</code>', r'`\1`', html, flags=re.DOTALL)
    
    # Handle preformatted blocks
    def handle_pre(match: re.Match) -> str:
        content = match.group(1)
        # Remove any inline code backticks we added
        content = content.replace('`', '')
        return f"\n```\n{content}\n```\n"
    
    html = re.sub(r'<pre[^>]*>(.*?)</pre>', handle_pre, html, flags=re.DOTALL)
    
    # Handle links
    html = re.sub(r'<a[^>]*href="([^"]*)"[^>]*>(.*?)</a>', r'[\2](\1)', html, flags=re.DOTALL)
    
    # Handle lists
    html = re.sub(r'<ul[^>]*>', '\n', html)
    html = re.sub(r'</ul>', '\n', html)
    html = re.sub(r'<li[^>]*>(.*?)</li>', r'- \1\n', html, flags=re.DOTALL)
    
    # Handle paragraphs
    html = re.sub(r'<p[^>]*>(.*?)</p>', r'\1\n\n', html, flags=re.DOTALL)
    
    # Handle spans (just remove them)
    html = re.sub(r'<span[^>]*>(.*?)</span>', r'\1', html, flags=re.DOTALL)
    
    # Remove remaining tags
    html = re.sub(r'<[^>]+>', '', html)
    
    # Clean up whitespace
    html = re.sub(r'\n{3,}', '\n\n', html)
    html = html.strip()
    
    return html


def fetch_puzzle(url: str, cookie: str | None = None) -> str:
    """Fetch puzzle HTML from adventofcode.com."""
    headers = {
        "User-Agent": "github.com/qverkk/aoc puzzle fetcher"
    }
    
    if cookie:
        headers["Cookie"] = cookie
    
    request = Request(url, headers=headers)
    
    try:
        with urlopen(request) as response:
            return response.read().decode("utf-8")
    except HTTPError as e:
        print(f"Error fetching puzzle: {e.code} {e.reason}", file=sys.stderr)
        sys.exit(1)


def parse_url(url_or_year: str, day: str | None = None) -> tuple[str, int, int]:
    """Parse URL or year/day arguments into (url, year, day)."""
    if day is not None:
        # Arguments are year and day
        year = int(url_or_year)
        day_num = int(day)
        url = f"https://adventofcode.com/{year}/day/{day_num}"
    else:
        # Argument is a URL
        url = url_or_year
        match = re.match(r"https://adventofcode\.com/(\d{4})/day/(\d+)", url)
        if not match:
            print(f"Invalid URL format: {url}", file=sys.stderr)
            print("Expected: https://adventofcode.com/YYYY/day/D", file=sys.stderr)
            sys.exit(1)
        year = int(match.group(1))
        day_num = int(match.group(2))
    
    return url, year, day_num


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Fetch Advent of Code puzzle and convert to markdown"
    )
    parser.add_argument(
        "url_or_year",
        help="Puzzle URL (https://adventofcode.com/YYYY/day/D) or year"
    )
    parser.add_argument(
        "day",
        nargs="?",
        help="Day number (required if first argument is year)"
    )
    parser.add_argument(
        "--cookie", "-c",
        help="Session cookie for authentication (or set AOC_SESSION env var)"
    )
    parser.add_argument(
        "--output", "-o",
        help="Output file path (default: puzzles/YYYY/dayDD.md)"
    )
    
    args = parser.parse_args()
    
    url, year, day = parse_url(args.url_or_year, args.day)
    
    # Get cookie from argument or environment
    cookie = args.cookie or os.environ.get("AOC_SESSION")
    if cookie and not cookie.startswith("session="):
        cookie = f"session={cookie}"
    
    print(f"Fetching puzzle: {url}")
    html = fetch_puzzle(url, cookie)
    
    # Extract article content
    extractor = ArticleExtractor()
    extractor.feed(html)
    
    if not extractor.articles:
        print("No puzzle content found!", file=sys.stderr)
        sys.exit(1)
    
    # Convert to markdown
    markdown_parts = []
    markdown_parts.append(f"# Advent of Code {year} - Day {day}\n")
    markdown_parts.append(f"[Puzzle Link]({url})\n")
    
    for i, article in enumerate(extractor.articles, 1):
        md = html_to_markdown(article)
        markdown_parts.append(md)
    
    markdown = "\n\n".join(markdown_parts)
    
    # Determine output path
    if args.output:
        output_path = Path(args.output)
    else:
        script_dir = Path(__file__).parent.parent
        output_path = script_dir / "puzzles" / str(year) / f"day{day:02d}.md"
    
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text(markdown)
    
    print(f"Saved to: {output_path}")
    
    # Show summary
    part_count = len(extractor.articles)
    if part_count == 1:
        print("Note: Only Part 1 found. Pass --cookie to fetch Part 2 after solving Part 1.")
    else:
        print(f"Found {part_count} parts.")


if __name__ == "__main__":
    main()
