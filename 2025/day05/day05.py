import sys

ranges, ids = open(sys.argv[1]).read().split("\n\n")


def includes(interval, x):
    return interval[0] <= x <= interval[1]


def compact(ranges):
    compacted = list()
    a, b = ranges[0]
    for nexta, nextb in ranges[1:]:
        if nexta <= b + 1:
            a, b = (a, max(b, nextb))
        else:
            compacted.append((a, b))
            a, b = (nexta, nextb)
    compacted.append((a, b))
    return compacted


def anyincludes(ranges, x):
    L = len(ranges)
    if L == 1:
        return includes(ranges[0], x)
    h = (L + 1) // 2
    ha, hb = ranges[h]
    return (
        (x < ha and anyincludes(ranges[:h], x))
        or (x > hb and anyincludes(ranges[h:], x))
        or includes(ranges[h], x)
    )


ranges = compact(
    sorted(tuple(map(int, line.split("-"))) for line in ranges.splitlines())
)
ids = [int(item) for item in ids.splitlines()]

print(sum(map(lambda x: anyincludes(ranges, x), ids)))
print(sum(b - a + 1 for (a, b) in ranges))
