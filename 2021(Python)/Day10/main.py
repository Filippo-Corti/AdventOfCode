from collections import defaultdict


f = open('input.txt', 'r')

data = f.read().splitlines()

PARENTHESIS = '<{[(>}])'

def match(c):
    index = PARENTHESIS.find(c)
    if index == -1:
        return ''
    if index < 4:
        return PARENTHESIS[index + 4]
    else:
        return PARENTHESIS[index - 4]

def illegal_char(line):
    s = ""
    for p in line:
        if PARENTHESIS.find(p) < 4:
            s += p   
        else:
            if s[-1:] == match(p):
                s = s[:-1]
            else:   
                return p
    return ''   

def get_score1(c):
    if c == ')': return 3
    elif c == ']': return 57
    elif c == '}': return 1197
    elif c == '>': return 25137
    return 0

#Part 1
def part1():
    illegal_chars = map(illegal_char, data)
    return sum(list(map(get_score1, illegal_chars)))

def completing_sequence(line):
    s = ""
    for p in line:
        if PARENTHESIS.find(p) < 4:
            s += p   
        else:
            s = s[:-1]
    return ''.join(map(match, list(s[::-1])))   

def get_score2(str):
    score = 0
    for c in str:
        score *= 5
        score += (4 - PARENTHESIS[4:].find(c))
    return score

#Part 2 
def part2():
    incomplete_sequences = filter(lambda line: get_score1(illegal_char(line)) == 0, data)
    scores = list(map(get_score2, map(completing_sequence, incomplete_sequences)))
    return sorted(scores)[len(scores) // 2]

print(part1())
print(part2())

