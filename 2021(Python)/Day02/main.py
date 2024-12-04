f = open('input.txt', 'r')
content = [el.split() for el in f.readlines()]
commands = [(x[0], int(x[1])) for x in content]
f.close()

#Part 1
def part1(commands):
    horiz, depth = 0, 0
    for command, value in commands:
        match command:
            case "forward":
                horiz += value
            case "up":
                depth -= value
            case "down":
                depth += value
    return horiz * depth

#Part 2
def part2(commands):
    horiz, depth, aim = 0, 0, 0
    for command, value in commands:
        match command:
            case "forward":
                horiz += value
                depth += aim * value
            case "up":
                aim -= value
            case "down":
                aim += value
    return horiz * depth



print(part1(commands))
print(part2(commands))