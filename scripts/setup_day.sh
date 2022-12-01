session=$(cat ~/.adventofcode/SESSION_ID)
request=$(wget -qO- --header "Cookie: session=$session" https://adventofcode.com/2022/day/$1)
question=$(echo "$request" | sed -n "/<article.*>/,/<\/article>/p" | sed -e 's/<pre><code>/\n```\n/g' | sed -e 's/<\/code><\/pre>/```\n/g' | sed -e 's/<[^>]*>//g' | sed -e 's/---\(.*\)---/\n\n---\1---\n\n/g')
# shellcheck disable=SC2001
# shellcheck disable=SC2086
title=$(echo $question | sed -e 's/^--- \([^-]*\)---.*/\1/g')

hub issue create --message "$title $question"
wget -qO- --header "Cookie: session=$session" https://adventofcode.com/2022/day/$1/input > src/test/resources/2022/day$1.input