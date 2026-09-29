import sys
from functools import reduce

f = open(sys.argv[1])

beams = {f.readline().find("S"): 1}
layers = [
    {i for i, c in enumerate(line) if c == "^"} for line in f if line.find("^") >= 0
]

# count = 0

# def pass_through(beams, layer):
#     global count
#     newbeams = set()
#     for beam in beams:
#         if beam not in layer:
#             newbeams.add(beam)
#         else:
#             count += 1
#             newbeams.update({beam - 1, beam + 1})
#     return newbeams


# def pass_through_all(beams, layers):
#     return reduce(lambda beams, layer: pass_through(beams, layer), layers, beams)

# pass_through_all(beams, layers)
# print(count)

def pass_through(beams, layer):
    newbeams = {}
    for beamidx, beamcount in beams.items():
        if beamidx not in layer:
            newbeams[beamidx] = newbeams.setdefault(beamidx, 0) + beamcount
        else:
            newbeams[beamidx - 1] = newbeams.setdefault(beamidx - 1, 0) + beamcount
            newbeams[beamidx + 1] = newbeams.setdefault(beamidx + 1, 0) + beamcount
    return newbeams

def pass_through_all(beams, layers):
    return reduce(lambda beams, layer: pass_through(beams, layer), layers, beams)

final = pass_through_all(beams, layers)
print(final)
print(sum(final.values()))