from parse import findall


f = open('input.txt', 'r')

data = f.read()
dots = [(x, y) for x, y in findall("{:d},{:d}", data)]
folds = findall("fold along {:l}={:d}", data)

def print_sheet(dots):
    print("----------------")
    max_x = max([d[0] for d in dots])
    max_y = max([d[1] for d in dots])
    for y in range(max_y + 1):
        for x in range(max_x + 1):
            if (x, y) in dots:
                print('#', end='')
            else:
                print('.', end='')
        print()      
        
def fold(axis, value):
    global dots  
    if axis == 'y':
        dots = {(x, y) if y < value else (x,  value - (y - value)) for x, y in dots}
    else:
        dots = {(x, y) if x < value else (value - (x - value), y) for x, y in dots}

#Part 1
def part1():   
    global dots  
    for axis, value in folds:
        fold(axis, value)
        break
    return len(dots)
            
        
# #Part 2 
def part2():
    global dots  
    for axis, value in folds:
        fold(axis, value)
    print_sheet(dots)

#print(part1())
print(part2())

