#!/usr/bin/env python3
"""
Recursively replace occurrences of 'ResourceLocation' with 'Identifier'
in all .java files under a given directory.

Usage:
  python replace_rl.py /path/to/project
  python replace_rl.py /path/to/project --dry-run
"""

from __future__ import annotations

import argparse
from pathlib import Path

def process_java_file(path: Path, dry_run: bool) -> tuple[bool, int]:
    """
    Returns (changed, replacements_count).
    """
    try:
        original = path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        # Fallback for odd encodings; you can change/remove if you prefer strict UTF-8.
        original = path.read_text(encoding="utf-8", errors="replace")

    count = original.count("ResourceLocation")
    if count == 0:
        return False, 0

    updated = original.replace("ResourceLocation", "Identifier")

    if not dry_run:
        path.write_text(updated, encoding="utf-8")

    return True, count


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Recursively replace ResourceLocation with Identifier in .java files."
    )
    parser.add_argument("directory", help="Root directory to scan")
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Don't write changes, just report what would change",
    )
    args = parser.parse_args()

    root = Path(args.directory).expanduser().resolve()
    if not root.exists() or not root.is_dir():
        print(f"Error: not a directory: {root}")
        return 2

    files_changed = 0
    total_replacements = 0
    files_scanned = 0

    for java_path in root.rglob("*.java"):
        if not java_path.is_file():
            continue

        files_scanned += 1
        changed, count = process_java_file(java_path, args.dry_run)
        if changed:
            files_changed += 1
            total_replacements += count
            prefix = "[DRY] " if args.dry_run else ""
            print(f"{prefix}{java_path}: {count} replacement(s)")

    print(
        f"\nScanned {files_scanned} .java file(s). "
        f"Changed {files_changed} file(s). "
        f"Total replacements: {total_replacements}."
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())