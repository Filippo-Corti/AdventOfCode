from collections import defaultdict

f = open('input.txt', 'r')
neighbours = defaultdict(list)

for line in f.read().splitlines():
    a, b = line.split("-")
    neighbours[a] += [b]
    neighbours[b] += [a]

#Part 1
def part1():
    count = 0
    paths = [['start']]
    while len(paths) != 0:
        path = paths.pop()
        current = path[-1]
        if current == 'end':
            count += 1
            continue
        for n in neighbours[current]:
            if n.islower():
                if path.count(n) < 1:
                    paths.append(path + [n])
            else:
                paths.append(path + [n])
    return count                    
        
#Part 2 
def part2():
    count = 0
    paths = [(['start'], False)]
    while len(paths) != 0:
        path, double_small = paths.pop()
        current = path[-1]
        if current == 'end':
            count += 1
            continue
        for n in neighbours[current]:
            if n.islower():
                if path.count(n) < 1:
                    paths.append((path + [n], double_small))
                elif not double_small and n != 'start':
                    paths.append((path + [n], True))
            else:
                paths.append((path + [n], double_small))
    return count       

print(part1())
print(part2())

