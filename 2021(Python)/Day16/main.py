from math import prod

f = open('input.txt', 'r')

data = f.read()
transmission = bin(int(data, 16))[2:].zfill(len(data) * 4)

version_sum = 0

def decodePacket(string):
    global version_sum
    version = int(string[0:3], base=2)
    version_sum += version
    pkt_type = int(string[3:6], base=2)
    if pkt_type == 4: # Literal Value Packet
        i = 6
        value = ""
        while True:
            group = string[i:i+5]
            value += group[1:]
            i += 5
            if group[0] == '0':
                break
        return (string[i:], int(value, base=2))
    else: #Operator Packet
        length_type = string[6]
        results = []
        if length_type == '0':
            total_length = int(string[7:22], base=2)
            string = string[22:]
            while total_length > 0:
                str_len = len(string)
                string, value = decodePacket(string)
                results.append(value)
                total_length -= (str_len - len(string))
        else:
            packets_number = int(string[7:18], base=2)    
            string = string[18:]   
            while packets_number > 0:
                string, value = decodePacket(string)
                results.append(value)
                packets_number -= 1
        if pkt_type == 0:
            value = sum(results)
        elif pkt_type == 1:
            value = prod(results)
        elif pkt_type == 2:
            value = min(results)
        elif pkt_type == 3:
            value = max(results)
        elif pkt_type == 5:
            value = 1 if results[0] > results[1] else 0
        elif pkt_type == 6:
            value = 1 if results[0] < results[1] else 0
        elif pkt_type == 7:
            value = 1 if results[0] == results[1] else 0
        return (string, value)
        

#Part 1
def part1(string):  
    global version_sum
    decodePacket(string)
    return version_sum
    
#Part 2 
def part2(string):
    return decodePacket(string)[1]

print(part1(transmission))
print(part2(transmission))

