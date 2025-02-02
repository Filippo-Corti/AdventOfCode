

f = open("input.txt", 'r')
lines = f.readlines()

registers = {
    'w': 0,
    'x': 0, # Is always set to z % N at the start
    'y': 0,
    'z': 26
}

inputs = 1

def evaluate(operand):
    try:
        return int(operand)
    except ValueError:
        return registers[operand]

def execute(instruction):
    global inputs
    operands = instruction.split()
    match operands[0]:
        case "inp":
            str_inputs = str(inputs)
            registers[operands[1]] = int(str_inputs[0])
            if len(str_inputs) > 1:
                inputs = int(str_inputs[1:])
        case "add":
            a = evaluate(operands[1])
            b = evaluate(operands[2])
            registers[operands[1]] = a + b
        case "mul":
            a = evaluate(operands[1])
            b = evaluate(operands[2])
            registers[operands[1]] = a * b
        case "div":
            a = evaluate(operands[1])
            b = evaluate(operands[2])
            registers[operands[1]] = a / b
        case "mod":
            a = evaluate(operands[1])
            b = evaluate(operands[2])
            registers[operands[1]] = a % b
        case "eql":
            a = evaluate(operands[1])
            b = evaluate(operands[2])
            registers[operands[1]] = 1 if a == b else 0


for line in lines:
    execute(line)    
    print(registers)        
