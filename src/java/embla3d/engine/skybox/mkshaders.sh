#! /bin/sh

PYTHON3=python3

echo "* Making skybox vertex shader *"
$PYTHON3 ../shader_classify.py "embla3d.engine.skybox" ./skyboxVertexShader.vs

echo "* Making skybox fragment shader *"
$PYTHON3 ../shader_classify.py "embla3d.engine.skybox" ./skyboxFragmentShader.fs

