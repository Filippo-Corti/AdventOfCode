f = open('input.txt', 'r')
lines = [line.strip() for line in f.readlines()]

f.close()

#Part 1
def part1(lines):
    gamma = int(''.join(list(map(lambda el: '1' if el else '0', [([n[i] for n in lines].count('1') > len([n[i] for n in lines]) / 2) for i in range(len(lines[0]))]))), 2)
    epsilon = int(''.join(list(map(lambda el: '1' if el else '0', [([n[i] for n in lines].count('1') < len([n[i] for n in lines]) / 2) for i in range(len(lines[0]))]))), 2)
    return gamma * epsilon

#Part 2
def part2(lines):
    copy_lines = lines[:]

    for i in range(len(lines[0])):
        most_common_digit = list(map(lambda el: '1' if el else '0', [([n[i] for n in lines].count('1') >= len([n[i] for n in lines]) / 2) for i in range(len(lines[0]))]))[i]
        lines = list(filter(lambda el: el[i] == most_common_digit, lines))
        if len(lines) == 1: 
            break
    oxygen = int(lines[0], 2)
    lines = copy_lines
    for i in range(len(lines[0])):
        least_common_digit = list(map(lambda el: '1' if el else '0', [([n[i] for n in lines].count('1') < len([n[i] for n in lines]) / 2) for i in range(len(lines[0]))]))[i]
        lines = list(filter(lambda el: el[i] == least_common_digit, lines))
        if len(lines) == 1: 
            break
    co2 = int(lines[0], 2)

    return oxygen * co2


print(part1(lines))
print(part2(lines))