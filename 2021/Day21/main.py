from collections import Counter, defaultdict
from copy import deepcopy
from functools import cache
from itertools import product, cycle
from parse import parse

f = open('input.txt', 'r')

data = f.readlines()

def part1():
    players_positions = [int(s.strip()[-1]) for s in data]
    players_scores = [0, 0]
    i = 0
    die = cycle(range(1, 101))
    rolls = 0
    while True:
        v = next(die) + next(die) + next(die)
        rolls += 3
        players_positions[i] = (players_positions[i] - 1 + v) % 10 + 1
        players_scores[i] += players_positions[i]
        if players_scores[i] >= 1000:
            return rolls * players_scores[(i + 1) % 2]
        i = not i
        

possible_sums = Counter(map(sum, product([1, 2, 3], repeat=3))).most_common()

@cache 
def count_wins(positions, scores, turn):
    if scores[0] >= 21: return (1, 0)
    if scores[1] >= 21: return (0, 1)
    count_0, count_1 = 0, 0
    for v, c in possible_sums:
        if turn:
            new_positions = (positions[0], (positions[1] - 1 + v) % 10 + 1)
            new_scores = (scores[0], scores[1] + new_positions[1])
        else:
            new_positions = ((positions[0] - 1 + v) % 10 + 1, positions[1])
            new_scores = (scores[0] + new_positions[0], scores[1])
        x, y = count_wins(new_positions, new_scores, not turn)
        count_0 += x * c 
        count_1 += y * c
    return (count_0, count_1)
    
        
def part2():
    players_positions = [int(s.strip()[-1]) for s in data]
    players_scores = [0, 0]
    return count_wins(tuple(players_positions), tuple(players_scores), 0)
    
    
#print(part1())
print(max(part2()))