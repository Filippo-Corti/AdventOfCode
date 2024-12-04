from collections import defaultdict
import functools
import numpy as np

f = open('input.txt', 'r')

data = [line.split("|") for line in f.read().splitlines()]
data = [(i.split(), o.split()) for i, o in data]

def digits_by_segments_number(n):
    digits = []
    if n == 2:
        digits.append('1')
    elif n == 3:
        digits.append('7')
    elif n == 4:
        digits.append('4')
    elif n == 5:
        digits.extend(['2', '3', '5'])
    elif n == 6:
        digits.extend(['0', '6', '9'])
    elif n == 7:
        digits.append('8')
    return digits

def intersect_chars(str1, str2):
    return len(set(str1) & set(str2))

def key_by_value(dictionary, value):
    return list(filter(lambda el: len(dictionary[el][0]) == len(value) and intersect_chars(dictionary[el][0],value) == len(value), list(dictionary.keys())))[0]

#Part 1
def part1(data):
    tot = 0
    for _, o in data:
        tot += len(list(filter(lambda el: len(el) in [2, 4, 3, 7], o)))
    return tot
                
#Part 2 
def part2(data):
    tot = 0
    for i, o in data:
        
        # 1. Create Dictionary with all possible options
        
        digits = defaultdict(list)
        for v in i:
            matches = digits_by_segments_number(len(v))
            #if len(matches) == 1:
            for match in matches:
                digits[match].append(v)
                
        # 2. Select the right option based on others
        
        # Find 0 using 1 and 4:
        for seq in digits['0']:
            if intersect_chars(seq, digits['1'][0]) == 2 and intersect_chars(seq, digits['4'][0]) == 3:
                digits['0'] = [seq]
                digits['6'].remove(seq)
                digits['9'].remove(seq)
                break
        
        # Find 2 using 1 and 4:
        for seq in digits['2']:
            if intersect_chars(seq, digits['1'][0]) == 1 and intersect_chars(seq, digits['4'][0]) == 2:
                digits['2'] = [seq]
                digits['3'].remove(seq)
                digits['5'].remove(seq)
                break
        
        # Find 3 using 1:
        for seq in digits['3']:
            if intersect_chars(seq, digits['1'][0]) == 2:
                digits['3'] = [seq]
                digits['5'].remove(seq)
                break
        
        # Find 6 using 1:
        for seq in digits['6']:
            if intersect_chars(seq, digits['1'][0]) == 1:
                digits['6'] = [seq]
                digits['9'].remove(seq)
                break
        
        result_as_list = [key_by_value(dict(digits), d) for d in o]
        tot += int(''.join(result_as_list))
    return tot
        

#print(part1(data))
print(part2(data))