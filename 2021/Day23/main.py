from collections import defaultdict
from heapq import heappush, heappop
from math import inf

col_by_type = {'A': 3, 'B': 5, 'C': 7, 'D': 9}
move_cost_by_type = {'A': 1, 'B': 10, 'C': 100, 'D': 1000}
hallway_positions = [(1, 1), (1, 2), (1, 4), (1, 6), (1, 8), (1, 10), (1, 11)]
end_state = (((2, 3), 'A'), ((2, 5), 'B'), ((2, 7), 'C'), ((2, 9), 'D'), ((3, 3), 'A'), ((3, 5), 'B'), ((3, 7), 'C'), ((3, 9), 'D'))

def is_reachable(state, start, end):
    steps = 0
    (row, col) = start
    (dest_row, dest_col) = end
    while row != 1:
        row-=1
        steps+=1
        if ((row, col) in state): return (False, -1)
    dc = 1 if dest_col > col else -1 
    while col != dest_col:
        col+=dc
        steps+=1
        if ((row, col) in state): return (False, -1)
    while row != dest_row:
        row+=1
        steps+=1
        if ((row, col) in state): return (False, -1)
    return (True, steps)
    
def is_goal(state):
    return state == end_state
    
def move(state, pos, new_pos):
    new_state = dict(state)
    val = new_state.pop(pos)
    new_state[new_pos] = val
    return tuple(sorted(new_state.items()))
    
def next_states(state):
    state_dict = dict(state)
    nexts = []
    for ((row, col), val) in state:
        
        #Check if it can move to its room
        dest_col = col_by_type[val]
        if col != dest_col:
            for checked_row in range(3, 1, -1):
                (ok, c) = is_reachable(state_dict, (row, col), (checked_row, dest_col))
                if ok:
                    nexts.append((move(state_dict, (row, col), (checked_row, dest_col)), c * move_cost_by_type[val]))
                    break
                else:
                    if (checked_row, dest_col) not in state_dict: break
                    elif state_dict[(checked_row, dest_col)] != val: break
                
        if row != 1: #Check if it can move in the hallway
            for hallway_pos in hallway_positions:
                (ok, c) = is_reachable(state_dict, (row, col), hallway_pos)
                if ok:
                    #print("Can move ", row, ",", col, " to ", hallway_pos)
                    nexts.append((move(state_dict, (row, col), hallway_pos), c * move_cost_by_type[val]))
        
    return nexts
        

with open("input.txt", 'r') as f:
    lines = f.read().splitlines()

start_dict = {(row, col): value for (row, line) in enumerate(lines) for (col, value) in enumerate(line) if value not in ['.', '#', ' ']}
start = tuple(sorted(start_dict.items()))

print(start)

queue = [(0, start)]
distances = defaultdict(lambda: inf, {start: 0})
visited = defaultdict(lambda: False)

while queue:
    cost, current = heappop(queue)
    if is_goal(current):
        print(cost)
        break
    visited[current] = True
    for (next_state, action_cost) in next_states(current):
        #print(next_state, action_cost)
        new_cost = cost + action_cost
        if not visited[next_state] and new_cost < distances[next_state]:
            distances[next_state] = new_cost
            heappush(queue, (new_cost, next_state))
    
    


