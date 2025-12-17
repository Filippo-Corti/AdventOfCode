f = open("input.txt", "r")
content = [int(line) for line in f.readlines()]
f.close()

# Part 1
print(len(list(filter(lambda i: i > 0 and content[i] > content[i-1], [i for i, _ in enumerate(content)])))) #Brutta
print(sum(x < y for x, y in zip(content, content[1:]))) #Bella

#Part 2
print(sum(x < y for x, y in zip(content, content[3:])))