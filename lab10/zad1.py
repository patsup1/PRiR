def Prostokat(a, b, podzialy, f):

  dx = (b - a) / podzialy

  wynik = 0
  for i in range(1, podzialy+1):
    wynik += f(a + i*dx)

  wynik *= dx
  return wynik


from math import sin



def Trapez(a, b, podzialy, f):

  dx = (b - a) / podzialy

  wynik = (f(a) + f(b)) / 2
  for i in range(1, podzialy):
    wynik += f(a + i*dx)

  wynik *= dx
  return wynik


from math import sin

def cpu():
  with tf.device('/cpu:0'):
    Prostokat(0, 3, 10000, sin)
    Trapez(0, 3, 10000, sin)

def gpu():
  with tf.device('/device:GPU:0'):
    Prostokat(0, 3, 10000, sin)
    Trapez(0, 3, 10000, sin)

import timeit
import tensorflow as tf

cpu()
gpu()

print('CPU sekundy:')
cpu_time = timeit.timeit('cpu()', number=1000, setup="from __main__ import cpu")
print(cpu_time)
print('GPU sekundy:')
gpu_time = timeit.timeit('gpu()', number=1000, setup="from __main__ import gpu")
print(gpu_time)
