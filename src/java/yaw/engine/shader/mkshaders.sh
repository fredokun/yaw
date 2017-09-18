#! /bin/sh

PYTHON3=python3

echo "* Making vertex shader *"
$PYTHON3 ./shader_classify.py "yaw.engine.shader" ./vertShader.vs

echo "* Making fragment shader *"
$PYTHON3 ./shader_classify.py "yaw.engine.shader" ./fragShader.fs
