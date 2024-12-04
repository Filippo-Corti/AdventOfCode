import functools
import numpy as np

f = open('input.txt', 'r')

data = list(map(int,  f.read().split(",")))


#Part 1
def part1(data):
    minx = min(data)
    maxx = max(data)
    fuels = [sum(list(map(lambda el: abs(el - x), data))) for x in range(minx, maxx + 1)]
    return min(fuels)
                
#Part 2 
def part2(data):
    minx = min(data)
    maxx = max(data)
    fuels = [sum(list(map(lambda el: (abs(el - x) * (abs(el - x) + 1)) // 2, data))) for x in range(minx, maxx + 1)]
    return min(fuels)

print(part1(data))
print(part2(data))