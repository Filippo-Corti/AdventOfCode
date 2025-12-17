intervals = [
        tuple(intervals.split("-"))
        for intervals in open('input.txt').readlines()[0].split(",")
]

def part1(pairs):
    s = 0
    for a, b in pairs:
        for x in range(int(a), int(b) + 1):
            xs = str(x)
            if len(xs) % 2 == 1:
                continue
            half = xs[:len(xs)//2]
            if x == int(half * 2):
                s += x
    return s

def part2(pairs):
    s = 0
    for a, b in pairs:
        for x in range(int(a), int(b) + 1):
            xs = str(x)
            for i in range(2, len(xs) + 1):
                if len(xs) % i != 0: continue
                part = xs[:len(xs)//i]
                if x == int(part * i):
                    s += x
                    break
    return s

print(part1(intervals))
print(part2(intervals))


