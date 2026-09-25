import sys

DIRS_8 = ((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))

grid = {
    (r, c)
    for r, row in enumerate(open(sys.argv[1]))
    for c, item in enumerate(row.strip())
    if item == "@"
}

neighbors = lambda grid, r, c: [
    (r + nr, c + nc) for nr, nc in DIRS_8 if (r + nr, c + nc) in grid
]


def count_removables(grid, maxwaves=None, depth=0):
    if maxwaves == depth:
        return 0

    toremove = {(r, c) for (r, c) in grid if len(neighbors(grid, r, c)) < 4}
    if not toremove:
        return 0
    return len(toremove) + count_removables(grid - toremove, maxwaves, depth + 1)


print(count_removables(grid, maxwaves=1))
print(count_removables(grid))
