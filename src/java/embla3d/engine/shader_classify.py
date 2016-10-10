
# A simple (and dirty) script to generate
# a java class from a shared program.
# This helps is statically resolving shader programs
# using LWJGL3.

import sys
import os.path

def jstringify(content):
    '''Return a java string from a "normal" string '''

    jstr = "\""

    for ch in content:
        if ch == '"':
            jstr += '\\"'
        elif ch == '\n':
            jstr += '\\n'
        else:
            jstr += ch
    
    return jstr + '\"'

def classify(package_name, class_name, shader_str):
    return """
// This file is generated from `{class_name}.fs` shader program
// Please do not edit directly
package {package_name};

public class {class_name} {{
    public final static String SHADER_STRING = {shader_str};
}}
""".format(**locals())

def fatal_error(explain):
    print("Fatal error: {}".format(explain), file=sys.stderr)
    print("Abort.")
    
if __name__ == "__main__":
    

    if len(sys.argv)!=3:
        fatal_error("needs at least two arguments")

    package_name = sys.argv[1]
    input_fname = sys.argv[2]

    print("Classifying shader program '{}' in package '{}'"
          .format(input_fname, package_name))
    
    base_fname = os.path.basename(input_fname)
    class_name = os.path.splitext(base_fname)[0]
    output_fname = class_name + ".java"

    with open(input_fname, 'r') as f:
        content = f.read()

    shader_str = jstringify(content)

    class_src = classify(package_name, class_name, shader_str)

    with open(output_fname, 'w') as f:
        f.write(class_src)

    print("... shader program classified in '{}'".format(output_fname))
    print("Bye bye!")

