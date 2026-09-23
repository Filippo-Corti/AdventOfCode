import sys
import functools

data = lambda: (line.strip() for line in open(sys.argv[1]))


def maxjolts(bank, n):
    """Extracts the largest number of jolts from the given bank, activating exactly n battires"""
    jolts = list(range(0, n))
    for i in range(n, len(bank)):
        to_drop = next(
            (j 
             for j, k in zip(jolts, jolts[1:] + [i]) 
             if bank[k] > bank[j]
            ), None
        )

        if to_drop is not None:
            jolts.remove(to_drop)
            jolts.append(i)

    return functools.reduce(
        lambda acc, x: acc * 10 + x, 
        (int(bank[i]) for i in jolts)
    )


print(sum(maxjolts(bank, n=2) for bank in data()))

print(sum(maxjolts(bank, n=12) for bank in data()))
