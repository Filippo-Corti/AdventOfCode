f = open('input.txt', 'r')

data = {(x,y): int(h) for x, row in enumerate(f.read().splitlines())
                    for y, h in enumerate(row)}

DIRECTIONS = [(0, 1), (0, -1), (1, 0), (-1, 0), (1, 1), (-1, -1), (1, -1), (-1, 1)] 

def neighbours(x, y):
    return filter(lambda coords: coords in data, [(x+dx, y+dy) for dx, dy in DIRECTIONS])

def update_neighbours(p, updated):
    updated.append(p)
    data[p] = 0
    for n in filter(lambda n: n not in updated, neighbours(*p)):
        data[n] += 1
        if data[n] >= 10:
            update_neighbours(n, updated)
        
def step():
    for p in data:
        data[p] += 1
    updated = []
    for p in filter(lambda e: data[e] >= 10, data):
        update_neighbours(p, updated)
    return len(updated)
            
#Part 1
def part1():
    return sum(step() for _ in range(100))

#Part 2 
def part2():
    for i in range(1, 1000):
        flashed = step()
        if flashed == 100:
            return i
    return -1

print(part1())
print(100 + part2())

