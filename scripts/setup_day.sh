#!/bin/bash

# Add debug flag at the start
DEBUG=0
if [ "$1" = "--debug" ]; then
    DEBUG=1
    shift
fi

# Function to convert number to title case day name
getDayStr() {
    local -a ones=([1]=One Two Three Four Five Six Seven Eight Nine)
    local -a teens=([10]=Ten Eleven Twelve Thirteen Fourteen Fifteen Sixteen Seventeen Eighteen Nineteen)
    local -a twenties=([20]=Twenty TwentyOne TwentyTwo TwentyThree TwentyFour TwentyFive)
    
    if (( $1 < 10 )); then
        echo "${ones[$1]}"
    elif (( $1 < 20 )); then
        echo "${teens[$1]}"
    else
        echo "${twenties[$1]}"
    fi
}

if [ -z "$1" ]; then
    echo "Usage: $0 <day_number>"
    exit 1
fi

year=$(jq -r '.["adventofcode.year"]' .vscode/settings.json)
session=$(cat ~/.adventofcode/SESSION_ID)

# Create directories if they don't exist
mkdir -p "src/main/kotlin/$year"
mkdir -p "src/test/resources/$year"

# Define all file paths
classFile="src/main/kotlin/$year/Day$(getDayStr $1).kt"
inputFile="src/test/resources/$year/day$1.input"
problemFile="src/test/resources/$year/day$1.problem"

# Only download if we need any of the files
if [ ! -f "$classFile" ] || [ ! -f "$inputFile" ] || [ ! -f "$problemFile" ]; then
    request=$(wget -qO- --header "Cookie: session=$session" "https://adventofcode.com/$year/day/$1")

    # Create class file if it doesn't exist
    if [ ! -f "$classFile" ]; then
        question=$(echo "$request" | sed -n "/<article.*>/,/<\/article>/p" | sed -e 's/<pre><code>/\n```\n/g' | sed -e 's/<\/code><\/pre>/```\n/g' | sed -e 's/<[^>]*>//g' | sed -e 's/---\(.*\)---/\n\n---\1---\n\n/g')
        title="Day $1: $(echo "$question" | grep -m1 "Day $1:" | cut -d':' -f2- | xargs)"
        gh issue create --title "$title" --body "$question"

        echo "package \`$year\`

import Project

class Day$(getDayStr $1)(file: String) : Project() {
    private val input = getLines(file)

    override fun part1(): Any {
        return \"Not implemented\"
    }

    override fun part2(): Any {
        return \"Not implemented\"
    }
}" > "$classFile"
    fi

    # Create input file if it doesn't exist
    if [ ! -f "$inputFile" ]; then
        wget -qO- --header "Cookie: session=$session" "https://adventofcode.com/$year/day/$1/input" > "$inputFile"
    fi

    # Create problem file if it doesn't exist
    if [ ! -f "$problemFile" ]; then
        echo "$request" | sed -n '/<article class="day-desc">/,/<\/article>/p' | sed 's/<[^>]*>//g' > "$problemFile"
    fi
fi

# Debug sample extraction if flag is set
if [ $DEBUG -eq 1 ]; then
    echo "=== Problem file contents ==="
    cat "$problemFile"
    echo -e "\n=== Attempting to extract sample ==="
    echo "First attempt:"
    sed -n '/^```$/,/^```$/p' "$problemFile"
    echo -e "\nTrying with different pattern:"
    sed -n '/For example:/,/^$/p' "$problemFile" | grep -v "For example:" | grep -v "^$"
fi

# Always update sample input from problem file
sample=$(sed -n '/For example:/,/^$/p' "$problemFile" | grep -v "For example:" | grep -v "^$")

if [ ! -z "$sample" ]; then
    echo "$sample" > "src/test/resources/$year/day$1.sample-input"
fi
