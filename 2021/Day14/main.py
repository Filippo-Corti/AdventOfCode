from parse import findall
from collections import Counter

f = open('input.txt', 'r')

data = f.read()

sequence = Counter([(a, b) for a, b in zip(data.splitlines()[0], data.splitlines()[0][1:])])
instructions = {(a, b): c for a, b, c in findall("{:l}{:l} -> {:l}", data)}

#Part 1
def part1(seq):  
    counts = Counter(data.splitlines()[0])
    for _ in range(10): 
        for (a, b), c in seq.copy().items():
            seq[(a, b)] -= c
            x = instructions[(a, b)]
            seq[(a, x)] += c
            seq[(x, b)] += c
            counts[x] += c
    return max(counts.values()) - min(counts.values())
    
#Part 2 
def part2(seq):
    counts = Counter(data.splitlines()[0])
    for _ in range(40): 
        for (a, b), c in seq.copy().items():
            seq[(a, b)] -= c
            x = instructions[(a, b)]
            seq[(a, x)] += c
            seq[(x, b)] += c
            counts[x] += c
    return max(counts.values()) - min(counts.values())

#print(part1(sequence))
print(part2(sequence))

