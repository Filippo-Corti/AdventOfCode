import sys
from functools import reduce


def transpose(M):
    return [[M[r][c] for r in range(len(M))] for c in range(len(M[0]))]


def solve(operands, op):
    return (op == "+" and reduce(lambda acc, x: acc + x, operands)) or (
        op == "*" and reduce(lambda acc, x: acc * x, operands)
    )


data = transpose([line.split() for line in open(sys.argv[1])])
problems = [(map(int, problem[:-1]), problem[-1]) for problem in data]

print(sum(solve(nums, op) for nums, op in problems))

lines = open(sys.argv[1]).readlines()

operands = [list(line[:-1]) for line in lines[:-1]]
ops = [c for c in list(lines[-1]) if c in ["*", "+"]]

operands = transpose(operands)

problems = list()
problem = list()
for operand in operands:
    try:
        num = reduce(lambda acc, x: acc * 10 + x, [int(x) for x in operand if x != " "])
        problem.append(num)
    except TypeError:
        problems.append(problem)
        problem = list()
problems.append(problem)

print(sum(solve(problem, op) for problem, op in zip(problems, ops)))
