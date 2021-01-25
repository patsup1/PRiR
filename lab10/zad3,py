import tensorflow as tf

fashion_mnist = tf.keras.datasets.fashion_mnist
(X_train, y_train), (X_val, y_val) = fashion_mnist.load_data()

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Flatten, add, Dense

model = Sequential()
model.add(Flatten(input_shape=(28, 28)))
model.add(Dense(128, activation='relu'))
model.add(Dense(10, activation = 'softmax'))

model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

history = model.fit(X_train, 
                    y_train, 
                    epochs=10,
                    verbose=1,
                    batch_size = 256,
                    validation_split = 0.2
                   )

import numpy as np

class_names = ['T-shirt/top', 'Trouser', 'Pullover', 'Dress', 'Coat',
               'Sandal', 'Shirt', 'Sneaker', 'Bag', 'Ankle boot']


predicted = model.predict(np.expand_dims(X_val[0],0))
class_names[np.argmax(predicted)]
