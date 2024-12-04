import functools
from math import prod
import numpy as np

f = open('input.txt', 'r')

data = f.read().splitlines()
data = {(row, col): int(value) for row, values in enumerate(data) 
                                for col, value in enumerate(values)}

DIRECTIONS = [(0, 1), (0, -1), (1, 0), (-1, 0)] 

def neighbours(x, y):
    return filter(lambda el: el in data, [(x+dx, y+dy) for dx, dy in DIRECTIONS])

def is_low_point(p):
    return all(data[p] < data[n] for n in neighbours(*p))


#Part 1
def part1():
    low_points = filter(is_low_point, data)
    return sum(data[p] + 1 for p in low_points)

def basin_size(p):
    if data[p] == 9:
        return 0
    del data[p] 
    return 1 + sum(map(basin_size, neighbours(*p)))
             
#Part 2 
def part2():
    low_points = list(filter(is_low_point, data))

    basins = map(basin_size, low_points)
    return prod(sorted(basins)[-3:])
        
print(part1())
print(part2())

