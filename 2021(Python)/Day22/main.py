from parse import parse

f = open('input.txt', 'r')

ff = f.readlines()

data = [parse('{} x={:d}..{:d},y={:d}..{:d},z={:d}..{:d}', d.strip()).fixed for d in ff]

def cubes_in_cuboid(d):
    return [(x, y, z) for x in range(d[1], d[2] + 1) for y in range(d[3], d[4] + 1) for z in range(d[5], d[6] + 1)]

def part1(data):
    cubes = set()
    for d in data:
        if d[0] == 'on':
            cubes.update(cubes_in_cuboid(d))
        else:
            cubes.difference_update(cubes_in_cuboid(d))
    return len(cubes)
        
def part2(data):
    x = [d[1] for d in data] + [d[2] + 1 for d in data]
    y = [d[3] for d in data] + [d[4] + 1 for d in data]
    z = [d[5] for d in data] + [d[6] + 1 for d in data]
    x.sort()
    y.sort()
    z.sort()
    data.reverse()
    
    count = 0
    
    for x1, x2 in zip(x, x[1:]):
        ins_x = [d for d in data if d[1] <= x1 <= d[2]]
        for y1, y2 in zip(y, y[1:]):
            ins_y = [d for d in ins_x if d[3] <= y1 <= d[4]]
            for z1, z2 in zip(z, z[1:]):
                if next((d[0] == 'on' for d in ins_y if d[5] <= z1 <= d[6]), False):
                    count += (x2 - x1) * (y2 - y1) * (z2 - z1)
                
    return count

#print(part1(data[:20]))
print(part2(data))