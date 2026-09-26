import sys
from operator import add, mul
from functools import reduce


def transpose(M):
    return [[M[r][c] for r in range(len(M))] for c in range(len(M[0]))]


def solve(operands, op):
    return reduce({"+": add, "*": mul}[op], operands)


def colstonums(numbers):
    listtonum = lambda l: reduce(
        lambda acc, x: acc * 10 + x, (int(x) for x in l if x != " "), 0
    )

    return reduce(
        lambda state, n: (
            (state[1] and (state[0] + [[n]]))
            or (n != 0 and (state[0][:-1] + [state[0][-1] + [n]]))
            or (state[0]),
            n == 0,
        ),
        map(listtonum, numbers),
        ([], True),
    )[0]


lines = open(sys.argv[1]).readlines()
ops = [c for c in list(lines[-1]) if c in ("*", "+")]

numbers = transpose([list(map(int, line.split())) for line in lines[:-1]])
print(sum(solve(nums, op) for nums, op in zip(numbers, ops)))


numbers = colstonums(transpose([list(line[:-1]) for line in lines[:-1]]))
print(sum(solve(nums, op) for nums, op in zip(numbers, ops)))
