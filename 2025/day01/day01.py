import sys

lines = [(int(line[1:]), line[0]) for line in open(sys.argv[1])]

def part1(lines):
	dial = 50
	zeros = 0
	for (v, dir) in lines:
		v = v % 100
		if dir == 'R':
			dial = (dial + v + 100) % 100
		else:
			dial = (dial - v + 100) % 100
		if dial == 0: 
			zeros += 1
	print(zeros)
	

def part2(lines):
	dial = 50
	zeros = 0
	for (v, dir) in lines:
		zeros += (v // 100)
		v = v % 100
		multi_rotation = False
		if dir == 'R':
			new_dial = (dial + v) % 100
			if dial != 0 and abs(new_dial) < dial:
				zeros += 1
				multi_rotation = True			
			dial = (new_dial + 100) % 100
		else:
			new_dial = (dial - v) % 100
			if dial != 0 and abs(new_dial) > dial:
				zeros += 1
				multi_rotation = True
			dial = (new_dial + 100) % 100
		if dial == 0 and not multi_rotation:
			zeros += 1
		
	print(zeros)
	
part1(lines)
part2(lines) 

		
