import sys
import math
from copy import deepcopy

DIRS_8 = ((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))

grid = {
    (r, c): item == '@'
    for r, row in enumerate(open(sys.argv[1]))
    for c, item in enumerate(row.strip())
}

neighbors = lambda grid, r, c: [
    (r + nr, c + nc) for nr, nc in DIRS_8 if grid.get((r + nr, c + nc))
]


def count_removables(grid, maxwaves):
    gridcpy = deepcopy(grid)
    sum = 0
    for i in range(maxwaves):
        removables = [
            (r, c)
            for (r, c) in gridcpy.keys()
            if gridcpy.get((r, c)) and len(neighbors(gridcpy, r, c)) < 4
        ]
        if not removables:
            break
        sum += len(removables)
        for (r, c) in removables:
            gridcpy[(r, c)] = False
    return sum

print(count_removables(grid, maxwaves=1))
print(count_removables(grid, maxwaves=1000))