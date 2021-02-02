import numpy as np
import tensorflow as tf
import trimesh

import tensorflow_graphics.geometry.transformation as tfg_transformation
from tensorflow_graphics.notebooks import threejs_visualization


!wget -N https://groups.csail.mit.edu/graphics/classes/6.837/F03/models/cow-nonormals.obj
mesh = trimesh.load("cow-nonormals.obj")
mesh = {"vertices": mesh.vertices, "faces": mesh.faces}
_ = threejs_visualization.triangular_mesh_renderer(mesh, width=1200, height=800)
axis = np.array((0., 1., 0.))  
angle = np.array((np.pi / 4.,)) 
mesh["vertices"] = tfg_transformation.axis_angle.rotate(mesh["vertices"], axis,
                                                        angle).numpy()
_ = threejs_visualization.triangular_mesh_renderer(mesh, width=1200, height=800)
