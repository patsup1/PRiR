import tensorflow as tf 
import numpy as np 
from tensorflow import keras

#1D

model = tf.keras.Sequential()
model.add(keras.layers.Dense(units=50,input_shape=[1]))
model.add(keras.layers.Dense(units=50,input_shape=[50]))
model.add(keras.layers.Dense(units=1,input_shape=[50]))

opt = keras.optimizers.Adam(learning_rate=0.01)
model.compile(optimizer=opt,loss='mean_squared_error')

xs = np.arange(-10, 10, 0.01)
ys = np.asarray(list(map(lambda x: (x-2)*(x+1)*(x-4), xs)))

model.fit(xs,ys,epochs=50)

print(model.predict([10.0]))

#2D

model = tf.keras.Sequential()
model.add(keras.layers.Dense(units=50,input_shape=[2]))
model.add(keras.layers.Dense(units=50))
model.add(keras.layers.Dense(units=1))

from math import sin, sqrt

xs = np.arange(-10, 10, 0.01)
ys = np.arange(-10, 10, 0.01)
zs = np.asarray(list(map(lambda x, y: sin(sqrt(x*x + y*y)) / sqrt(x*x + y*y), xs, ys)))

opt = keras.optimizers.Adam(learning_rate=0.01)
model.compile(optimizer=opt,loss='mean_squared_error')

data = []
for i in range(len(xs)):
  data.append(([xs[i], ys[i]]))

data = np.asarray(data)
model.fit(data,zs,epochs=5)

print(model.predict([(5.0, 5.0)]))
