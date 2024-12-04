import functools
import numpy as np

f = open('input.txt', 'r')

fish = list(map(int,  f.read().split(",")))

@functools.cache
def score(n, days):
    if days - n <= 0:
        return 0
    tot = (days - n) // 7 + (1 if (days - n) % 7 != 0 else 0) #Born by n itself
    for i in range(tot):
        tot += score(8, days - n - 7*i - 1)
    return tot

#Part 1
def part1(fish):
    tot = 0
    for f in fish:
        tot += score(f, 80) + 1
    return tot
                
#Part 2 
def part2(fish):
    tot = 0
    for f in fish:
        tot += score(f, 256) + 1
    return tot

print(part1(fish))
print(part2(fish))