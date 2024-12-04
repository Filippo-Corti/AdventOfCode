

from collections import defaultdict
from copy import deepcopy

f = open('input.txt', 'r')

data = f.read().split("\n\n")
count = 0
algorithm = data[0]
image = defaultdict(lambda: '.' if count % 2 == 0 else '#')
image.update({(a, b):h for a, r in enumerate(data[1].split("\n")) for b, h in enumerate(r)})

DIRECTIONS = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 0), (0, 1), (1, -1), (1, 0), (1, 1)] 

def neighbours(x, y):
    return [(x+dx, y+dy) for dx, dy in DIRECTIONS]
        
def apply_algorithm():
    global image, count
    copy = deepcopy(image)
    min_v = min([a for a, _ in copy.keys()])
    max_v = max([a for a, _ in copy.keys()])
    
    #Apply Algorithm:
    for i in range(min_v - 1, max_v + 2):
        for j in range(min_v - 1, max_v + 2):
            c = (i, j)
            n = neighbours(*c)
            s = ''.join(list(map(lambda x: '1' if image[x] == '#' else '0', n)))
            copy[c] = algorithm[int(s, base=2)]
    image = copy
    count += 1
        
for i in range(50):
    apply_algorithm()
    print(i)
print(len(list(filter(lambda x: x == '#', image.values()))))

