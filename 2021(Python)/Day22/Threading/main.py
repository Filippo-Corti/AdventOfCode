from parse import parse
import multiprocessing as mp
import ctypes
import time

"""
BENCHMARK (Portatile):
1 Processo: 690s
2 Processi: 387s
5 Processi: 300s
10 Processi: 292s

BENCHMARK (PC Fisso):
10 Processi: 64s
15 Processi: 49s
20 Processi: 47s
40 Processi: 46.7s
"""

start_time = time.time()
THREADS_COUNT =  40 #Make sure it's a divisor for 'iterations' (840)


def cubes_in_cuboid(d):
    return [(x, y, z) for x in range(d[1], d[2] + 1) for y in range(d[3], d[4] + 1) for z in range(d[5], d[6] + 1)]

def part1(data):
    cubes = set()
    for d in data:
        if d[0] == 'on':
            cubes.update(cubes_in_cuboid(d))
        else:
            cubes.difference_update(cubes_in_cuboid(d))
    return len(cubes)

def count_active_cubes(x, y, z, data, shared_total):
    iters = len(x)
    i = 0

    count = 0
    for x1, x2 in zip(x, x[1:]):
        ins_x = [d for d in data if d[1] <= x1 <= d[2]]
        for y1, y2 in zip(y, y[1:]):
            ins_y = [d for d in ins_x if d[3] <= y1 <= d[4]]
            for z1, z2 in zip(z, z[1:]):
                if next((d[0] == 'on' for d in ins_y if d[5] <= z1 <= d[6]), False):
                    count += (x2 - x1) * (y2 - y1) * (z2 - z1)
        print("%s is at %d/%d" % (mp.current_process().name, i, iters))
        i += 1

    with shared_total.get_lock():
        shared_total.value += count

if __name__ == '__main__':
        
    f = open('../input.txt', 'r')

    ff = f.readlines()

    data = [parse('{} x={:d}..{:d},y={:d}..{:d},z={:d}..{:d}', d.strip()).fixed for d in ff]

            
    def part2(data):
        x = [d[1] for d in data] + [d[2] + 1 for d in data]
        y = [d[3] for d in data] + [d[4] + 1 for d in data]
        z = [d[5] for d in data] + [d[6] + 1 for d in data]
        x.sort()
        y.sort()
        z.sort()
        data.reverse()
        
        i, iterations = 0, len(x)
        
        total = mp.Value(ctypes.c_ulonglong, 0)

        ts = []
        t_size = iterations // THREADS_COUNT

        for j in range(THREADS_COUNT):
            if j == 0:
                x_t = x[0: t_size]
                print(0, t_size)
            elif j == THREADS_COUNT - 1:
                x_t = x[j*t_size - 1:(j+1)*t_size + 1]
                print(j*t_size - 1, (j+1)*t_size)
            else:
                x_t = x[j*t_size - 1:(j+1)*t_size]
                print(j*t_size - 1, (j+1)*t_size - 1)
            ts.append(mp.Process(target=count_active_cubes, args=(x_t, y, z, data, total)))

        for t in ts:
            t.start()
        
        for t in ts:
            t.join()

        return total.value

    #print(part1(data[:20]))
    print(part2(data))
    print("---- %s seconds ----" % (time.time() - start_time))