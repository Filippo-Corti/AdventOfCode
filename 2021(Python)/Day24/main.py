import sys

f = open("input.txt", 'r')
lines = f.readlines()

blocks = [
    (1, 10, 5),
    (1, 13, 9),
    (1, 12, 4),
    (26, -12, 4),
    (1, 11, 10),
    (26, -13, 14),
    (26, -9, 14),
    (26, -12, 12),
    (1, 14, 14),
    (26, -9, 14),
    (1, 15, 5),
    (1, 11, 10),
    (26, -16, 8),
    (26, -2, 15)
]

def executeOneBlock(w, z, blockIdx):
    x = z % 26 + blocks[blockIdx][1]
    z = int(z / blocks[blockIdx][0]) # N1 can be either 1 or 26
    if (x == w):
        return z
    
    z *= 26
    z += w + blocks[blockIdx][2]
    return z

def part1(z = 0, i = 0, inputs = ""):
    if i == len(blocks):
        if z == 0:
            print(inputs)
            sys.exit(1)
        else:
            return
    
    if blocks[i][0] == 1:
        for d in range(9, 0, -1):
            new_z = executeOneBlock(d, z, i)
            part1(new_z, i+1, inputs + str(d))
    else:
        only_d = z % 26 + blocks[i][1]
        if only_d >= 1 and only_d <= 9:
            new_z = int(z / 26)
            part1(new_z, i+1, inputs + str(only_d))
            
            
def part2(z = 0, i = 0, inputs = ""):
    if i == len(blocks):
        if z == 0:
            print(inputs)
            sys.exit(1)
        else:
            return
    
    if blocks[i][0] == 1:
        for d in range(1, 10):
            new_z = executeOneBlock(d, z, i)
            part2(new_z, i+1, inputs + str(d))
    else:
        only_d = z % 26 + blocks[i][1]
        if only_d >= 1 and only_d <= 9:
            new_z = int(z / 26)
            part2(new_z, i+1, inputs + str(only_d))


part1()
#part2()

