#!/bin/bash

find src/main/kotlin -name "Day*.kt" -type f | while read -r file; do
    echo "Processing $file"
    
    # Only add suspend keyword, preserve return type
    sed -i '' \
        -e 's/override fun part1()/override suspend fun part1()/' \
        -e 's/override fun part2()/override suspend fun part2()/' \
        "$file"
done 