#!/bin/bash

# Fix specific return type declarations
find src/main/kotlin/2020 -name "Day*.kt" -type f | while read -r file; do
    echo "Processing $file"
    
    # Replace function declarations with specific return types
    sed -i '' \
        -e 's/override fun part1(): Int/override suspend fun part1(): Any/' \
        -e 's/override fun part2(): Int/override suspend fun part2(): Any/' \
        -e 's/override fun part1(): BigInteger/override suspend fun part1(): Any/' \
        -e 's/override fun part2(): BigInteger/override suspend fun part2(): Any/' \
        -e 's/override fun part1(): Long/override suspend fun part1(): Any/' \
        -e 's/override fun part2(): Long/override suspend fun part2(): Any/' \
        "$file"
done 