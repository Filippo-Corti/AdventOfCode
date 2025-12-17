from math import floor, ceil, prod
import re

f = open('input.txt', 'r')

data = f.read().splitlines()

def magnitude(seq):
    if '[' not in seq:
        return int(seq)
    cb = 0
    for i,c in enumerate(seq):
        if c == '[':
            cb += 1
        elif c == ']':
            cb -= 1
        if c == ',' and cb == 1:
            return 3*magnitude(seq[1:i]) + 2*magnitude(seq[i+1:-1])

#Returns (number, start_index, end_index)
def find_number(seq, index):
    numbers = list(re.finditer("[0-9]+", seq))
    if len(numbers) == 0 or len(numbers) < abs(index):
        return (-1, -1, -1)
    number = numbers[index]
    return (int(number[0]), number.start(), number.end())

def add(seq1, seq2):
    return "[" + seq1 + "," + seq2 + "]"

def explode(seq, start):
    end = start + seq[start:].index(']')
    before, after = seq[:start], seq[end+1:]
    number_before = find_number(before, -1) #First number to the left of pair
    number_after = find_number(after, 0) #First number to the right of pair
    
    pair = list(map(int, re.findall("[0-9]+", seq[start:end+1]))) #Exploding Pair values
    
    part1 = seq[:number_before[1] if number_before[0] >= 0 else start] #Unchanged - Start of string
    part2 = str(pair[0] + number_before[0]) if number_before[0] >= 0 else '' #Left of pair added to the first number to the left
    part3 = seq[number_before[2] : start] if number_before[0] >= 0 else '' # Unchanged - Between part2 and the exploding pair
    part4 = '0' #Exploding Pair becomes 0
    part5 = seq[end+1 : (end + number_after[1] + 1)] if number_after[0] >= 0 else ''# Unchanged - Between exploding pair and part6
    part6 = str(pair[1] + number_after[0]) if number_after[0] >= 0 else '' #Right of pair added to the first number to the right
    part7 = seq[(end + number_after[2] + 1) if number_after[0] >= 0 else end+1 :] #Unchanged - End of string
  
    return part1 + part2 + part3 + part4 + part5 + part6 + part7

def split(seq, span):
    number = int(seq[span[0]:span[1]])
    left = str(floor(number / 2))
    right = str(ceil(number / 2))
    return seq[:span[0]] + "[" + left + "," + right + "]" + seq[span[1]:]

def reduceSeq(seq):
    # Check for 4 nesting levels
    cb = 0
    for i,c in enumerate(seq):
        if c == '[':
            cb += 1
        elif c == ']':
            cb -= 1
        if cb == 5:
            return reduceSeq(explode(seq, i))
    
    # Check for double digits numbers
    dd = list(re.finditer("[0-9]{2}", seq))
    if len(dd) > 0:
        return reduceSeq(split(seq, dd[0].span()))

    return seq

#Part 1
def part1():  
    result = data[0]
    for n in data[1:]:
        result = add(result, n)
        result = reduceSeq(result)
    return magnitude(result)
    
    
#Part 2 
def part2():
    couples = []
    for n1 in data:
        for n2 in data:
            if n1 != n2:
                couples.append((n1, n2))
    return max(map(lambda couple: magnitude(reduceSeq(add(*couple))), couples))

print(part1())
print(part2())

