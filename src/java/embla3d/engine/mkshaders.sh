#! /bin/sh

PYTHON3=python3

echo "* Making vertex shader *"
$PYTHON3 ./shader_classify.py "embla3d.engine" ./vertShader.vs

echo "* Making fragment shader *"
$PYTHON3 ./shader_classify.py "embla3d.engine" ./fragShader.fs
