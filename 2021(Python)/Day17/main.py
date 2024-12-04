import parse as parse

f = open('input.txt', 'r')
min_x, max_x, min_y, max_y = parse.parse("target area: x={:d}..{:d}, y={:d}..{:d}", f.read())


def is_in_target(x, y):
    return x >= min_x and x <= max_x and y >= min_y and y <= max_y

def lands_in_target(dx, dy):
    x, y = 0, 0
    style = 0
    while x <= max_x and y >= min_y:
        x, y = x+dx, y+dy
        style = max(y, style)
        if is_in_target(x, y):
            return (True, style)
        if dx == 0:
            ddx = 0
        elif dx > 0:
            ddx = -1
        else:
            ddx = 1
        dx += ddx 
        dy -= 1
    return (False, style)

#Part 1 (can you always use (n*(n+1)) // 2 ?)
def part1():   
    max_style = 0
    for dx in range(0, max_x+1):
        for dy in range(0, -min_y+1):
            in_target, style = lands_in_target(dx, dy)
            if in_target:
                max_style = max(max_style, style)   
    return max_style
    
#Part 2 
def part2():
    c = 0
    for dx in range(0, max_x+1):
        for dy in range(min_y, -min_y+1):
            in_target, style = lands_in_target(dx, dy)
            if in_target:
                c += 1
    return c
    
print(part1())
print(part2())