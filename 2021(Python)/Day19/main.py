from collections import defaultdict
from itertools import combinations

f = open('input.txt', 'r')

data = f.read().split("\n\n")
scanners = [[tuple(map(int, line.split(","))) for line in scanner.splitlines()[1:]] for scanner in data]

coord_remaps = [(0, 1, 2), (0, 2, 1), (1, 0, 2), (1, 2, 0), (2, 0, 1), (2, 1, 0)]
coord_negations = [(1, 1, 1), (1, 1, -1), (1, -1, 1), (1, -1, -1), (-1, 1, 1), (-1, 1, -1), (-1, -1, 1), (-1, -1, -1)]

permutations = [(a, b) for a in coord_negations for b in coord_remaps]

def find_beacon_position(s, b, orientation):
    d, c = permutations[orientation]
    x = s[0] + d[0] * b[c[0]]
    y = s[1] + d[1] * b[c[1]]
    z = s[2] + d[2] * b[c[2]]
    return (x, y, z)

def find_scanner_position(pos1, pos2, orientation):
    d, c = permutations[orientation]
    x = pos1[0] - d[0] * pos2[c[0]]
    y = pos1[1] - d[1] * pos2[c[1]]
    z = pos1[2] - d[2] * pos2[c[2]]
    return (x, y, z)

def check_for_overlaps(beacons_positions, s):
    differences = defaultdict(list)
    for a, b, c in beacons_positions:
        for d in scanners[s]:
            for p, ((d1, d2, d3), (x, y, z)) in enumerate(permutations):
                key = (a - d1 * d[x], b - d2 * d[y], c - d3 * d[z], p)
                differences[key].append(((a, b, c), (d)))
                if len(differences[key]) >= 12:
                    return (True, key, differences[key][0])
    return (False, 0 , 0)

scanners_positions = {0:(0, 0, 0)}
beacons_positions = set()
beacons_positions.update(scanners[0])
queue = [i for i in range(1, len(scanners))]

while len(queue) > 0:
    scanner = queue.pop(0)
    found, key, couple = check_for_overlaps(beacons_positions, scanner)
    if found:
        pos = find_scanner_position(couple[0], couple[1], key[3])
        scanners_positions[scanner] = pos
        beacons_positions.update(map(lambda b: find_beacon_position(key[:3], b, key[3]), scanners[scanner]))
        print(len(scanners_positions))
    else:
        queue.append(scanner)
            

#Part1
print(len(beacons_positions))

#Part2
def manhattan_distance(a, b):
    return sum([abs(a[i] - b[i]) for i in range(len(a))])

couples = [(a, b) for a, b in combinations(scanners_positions.values(), 2)]
dists = map(lambda c: manhattan_distance(*c), couples)
print(max(dists))
