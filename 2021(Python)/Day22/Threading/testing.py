import ctypes
import time
import multiprocessing


def addN(s, e, data):
    c = 0
    for i in range(s, e):
            c += i     
    with data.get_lock():
        data.value += c
    #print("Done with", multiprocessing.current_process().name, c)
        
start_time = time.time()

if __name__ == "__main__":
    
    n = 200000000
    threads = 4

    n_per_thread = n // threads
    
    v = multiprocessing.Value(ctypes.c_longlong, n)
    
    ts = []
    for i in range(threads):
        ts.append(multiprocessing.Process(target=addN, args=(i*n_per_thread, (i+1)*n_per_thread, v)))
        
    for t in ts:
        t.start()
        
    for t in ts:
        t.join()
        
    print("Result:", v.value)
    print("Correct Answer:", (n*(n+1))//2 == v.value)
    print("------ %s seconds" % (time.time() - start_time))