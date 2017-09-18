#! /bin/sh

PYTHON3=python3

echo "* Making skybox vertex shader *"
$PYTHON3 ../shader_classify.py "yaw.engine.skybox" ./skyboxVertexShader.vs

echo "* Making skybox fragment shader *"
$PYTHON3 ../shader_classify.py "yaw.engine.skybox" ./skyboxFragmentShader.fs

