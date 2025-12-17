lines = open('input.txt').read().split("\n")[:-1]

def part1(banks):
    s = 0
    for bank in banks:
        a = b = '0'
        i = 0
        while i < len(bank):
            if bank[i] > a and i + 1 < len(bank):
                if b > bank[i]:
                    a = b
                    b = bank[i]
                else:
                    a = bank[i]
                    b = bank[i + 1]
                    i += 1
            elif bank[i] > b:
                if b > a:
                    a = b
                b = bank[i]
            i += 1
        incr = int(a) * 10 + int(b)
        s += incr
        print(bank, incr)
    return s


print(part1(lines))
