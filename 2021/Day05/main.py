import numpy as np


f = open('input.txt', 'r')

#Replace arrows with commas and split by comma
lines = [line.replace(' -> ', ',').split(',') for line in f.read().splitlines()]
# Map to ints
lines = [list(map(int, line)) for line in lines]

#Part 1
def part1(lines):
    positions = {}
    for x1, y1, x2, y2 in lines: #Foreach with multiple elements every iteration
        if x1 == x2 or y1 == y2:
            minx, miny, maxx, maxy = min(x1, x2), min(y1, y2), max(x1, x2), max(y1, y2)
            for x in range(minx, maxx + 1):
                for y in range(miny, maxy + 1):
                    pos = (x, y)
                    if pos in positions:
                        positions[pos] += 1
                    else:
                        positions[pos] = 1
    return len(list(filter(lambda el: el > 1, positions.values()))) 
                
#Part 2 
def part2(lines):
    positions = {}
    for x1, y1, x2, y2 in lines: #Foreach with multiple elements every iteration
        minx, miny, maxx, maxy = min(x1, x2), min(y1, y2), max(x1, x2), max(y1, y2)
        if x1 == x2 or y1 == y2:
            for x in range(minx, maxx + 1):
                for y in range(miny, maxy + 1):
                    pos = (x, y)
                    if pos in positions:
                        positions[pos] += 1
                    else:
                        positions[pos] = 1
        else:
            xrange = range(x1, x2 + (1 if x2 > x1 else -1), 1 if x2 > x1 else -1)
            yrange = range(y1, y2 + (1 if y2 > y1 else -1), 1 if y2 > y1 else -1)
            for x, y in zip(xrange, yrange):
                pos = (x, y)
                if pos in positions:
                    positions[pos] += 1
                else:
                    positions[pos] = 1
    return len(list(filter(lambda el: el > 1, positions.values())))
    

print(part1(lines))
print(part2(lines))