import sys

data = open(sys.argv[1]).read().split("\n\n")

ranges = sorted(
    tuple(int(item) for item in line.strip().split("-"))
    for line in data[0].split("\n")
)

ids = [
    int(item)
    for item in data[1].split("\n")
]

def compact(ranges):
    newranges = list()
    (curra, currb) = ranges[0]
    for i in range(1, len(ranges)):
        (nexta, nextb) = ranges[i]
        if nexta <= currb + 1:
            (curra, currb) = (curra, max(currb, nextb))
        else:
            newranges.append((curra, currb))
            (curra, currb) = (nexta, nextb)
    newranges.append((curra, currb))    
    return newranges

def iscovered(ranges, x):
    '''Binary search to find if x is in any range'''
    L = len(ranges)
    if L == 1:
        (ha, hb) = ranges[0]
        return ha <= x <= hb
    h = (L+1) // 2
    (ha, hb) = ranges[h]
    if x < ha:
        return iscovered(ranges[:h], x)
    if x > hb:
        return iscovered(ranges[h:], x)
    else:
        return ha <= x <= hb
    
ranges = compact(ranges)

print(len([x for x in ids if iscovered(ranges, x)]))
print(sum(b-a+1 for (a, b) in ranges))
