import sys

f = open('input.txt', 'r')
lines = f.read().splitlines()
numbers = [int(n) for n in lines[0].split(",")]

boards_count = (len(lines) - 1) // 6
boards = []
for i in range(boards_count):
    board = [[(int(n), False) for n in lines[j].split()] for j in range(i*6 + 2, i*6 + 7)]
    boards.append(board)

def mark(board, n):
    for r in range(len(board)):
        for c in range(len(board[r])):
            if board[r][c][0] == n:
                board[r][c] = (board[r][c][0], True)

def bingo(board):
    for row in board:
        if all([t[1] for t in row]):
            return True
    for col in zip(*board):
        if all([t[1] for t in col]):
            return True
    return False

def count_score(board):
    tocount = [list([el[0] for el in filter(lambda el: not el[1], row)]) for row in board]
    return sum(sum(row) for row in tocount)

#Part 1
def part1(boards, numbers):
    for extracted in numbers:
        for board in boards:
            mark(board, extracted)
            if bingo(board):
                return count_score(board) * extracted
                
#Part 2 
def part2(boards, numbers):
    winners = []
    count_winners = 0
    for extracted in numbers:
        for board in boards:
            mark(board, extracted)
            if bingo(board):
                if board not in winners:
                    count_winners += 1
                winners.append(board)
                if count_winners == boards_count:
                    return count_score(board) * extracted
    

print(part1(boards, numbers))
print(part2(boards, numbers))