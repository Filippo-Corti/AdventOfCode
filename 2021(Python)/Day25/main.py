
f = open('input.txt')
lines = f.read().split("\n")

rows = len(lines)
cols = len(lines[0])

easts = {(row, col):True for (row, line) in enumerate(lines) for (col, v) in enumerate(line) if v == '>'}
souths = {(row, col):True for (row, line) in enumerate(lines) for (col, v) in enumerate(line) if v == 'v'}

def move_east(row, col):
    new_col = col + 1 if col + 1 < cols else 0
    return (row, new_col)

def move_south(row, col):
    new_row = row + 1 if row + 1 < rows else 0
    return (new_row, col)

has_stopped = False
i = 0
while not has_stopped:
    has_stopped = True
    new_easts = {}
    new_souths = {}
    for east in easts:
        new_east = move_east(*east)
        if new_east not in easts and new_east not in souths:
            new_easts[new_east] = True
            has_stopped = False
        else:
            new_easts[east] = True
         
    easts = new_easts      
    
    for south in souths:
        new_south = move_south(*south)
        if new_south not in easts and new_south not in souths:
            new_souths[new_south] = True
            has_stopped = False
        else:
            new_souths[south] = True
    
    souths = new_souths
    i+=1

print(i)    
             
        