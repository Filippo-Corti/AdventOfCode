from collections import defaultdict
from heapq import heappush, heappop
from math import inf

f = open('input.txt', 'r')

data = f.read().splitlines()
data = {(row, col): int(value) for row, values in enumerate(data) 
                                for col, value in enumerate(values)}

start = (0, 0)
end = (max([row for (row, _) in data]), max([col for (_, col) in data]))

DIRECTIONS = [(0, 1), (0, -1), (1, 0), (-1, 0)] 

def neighbours(x, y):
    return filter(lambda el: el in data, [(x+dx, y+dy) for dx, dy in DIRECTIONS])


#Part 1
def part1():  
    print(end)
    queue = [(0, start)]
    distances = defaultdict(lambda: inf, {(0, 0): 0})
    visited = defaultdict(lambda: False)
    while queue:
        distance, current = heappop(queue)
        if current == end:
            return distance
        visited[current] = True
        for p in neighbours(*current):
            new_dist = distance + data[p]
            if not visited[p] and new_dist < distances[p]:
                distances[p] = new_dist
                heappush(queue, (new_dist, p))
    
    
#Part 2 
def part2():
    global end
    max_r, max_c = end[0] + 1, end[1] + 1
    for (r, c) in data.copy():
        for i in range(0, 5):
            for j in range(0, 5):
                new_v = data[(r,c)] + i + j
                new_v = new_v if new_v < 10 else new_v % 10 + 1
                data[(r + max_r*i), (c + max_c*j)] = new_v
                
    end = (max([row for (row, _) in data]), max([col for (_, col) in data]))
    return part1()
        

print(part1())
print(part2())

