package embla3d.engine.meshGenerator;

import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Vector3f;

import java.util.Map;

public class BlockGenerator {
    /**
     * Generate a BlockMesh with the specified material (cx, cy, cz, reflectance), width, length and height
     *
     * @param xLength xLength
     * @param yLength yLength
     * @param zLength zLength
     * @param cx      Red value of the material
     * @param cy      Green  value of the material
     * @param cz      Blue  value of the material
     * @param r       reflectance
     * @return BlockMesh
     */
    public static Mesh generate(float xLength, float yLength, float zLength, float cx, float cy, float cz, float r) {
        return generate(xLength, yLength, zLength, new Material(new Vector3f(cx, cy, cz), r));
    }

    /**
     * Generate a BlockMesh with the specified material , width, length and height.
     * Create vertices based on the specified width, length and height.
     * Normals and indices are hard coded
     *
     * @param xLength xLength
     * @param yLength yLength
     * @param zLength zLength
     * @param m       material
     * @return BlockMesh
     */
    public static Mesh generate(float xLength, float yLength, float zLength, Material m) {
        float x = xLength / 2f;
        float y = yLength / 2f;
        float z = zLength / 2f;
        float[] vertices = new float[]{
                //Front face
                x, y, z,
                -x, y, z,
                -x, -y, z,
                x, -y, z,
                //Top face
                x, y, z,
                -x, y, z,
                x, y, -z,
                -x, y, -z,
                //Back face
                x, y, -z,
                -x, y, -z,
                -x, -y, -z,
                x, -y, -z,
                //Bottom face
                x, -y, z,
                -x, -y, z,
                x, -y, -z,
                -x, -y, -z,
                //Left face
                -x, y, z,
                -x, y, -z,
                -x, -y, z,
                -x, -y, -z,
                //Right face
                x, y, z,
                x, y, -z,
                x, -y, z,
                x, -y, -z
        };
        //for light
        float[] normals = {
                //Front face
                0, 0, 1f,
                0, 0, 1f,
                0, 0, 1f,
                0, 0, 1f,
                //Top face
                0, 1f, 0,
                0, 1f, 0,
                0, 1f, 0,
                0, 1f, 0,
                //Back face
                0, 0, -1f,
                0, 0, -1f,
                0, 0, -1f,
                0, 0, -1f,
                //Bottom face
                0, -1f, 0,
                0, -1f, 0,
                0, -1f, 0,
                0, -1f, 0,
                //Left face
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                //Right face
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                1, 0, 0
        };
        //mapping the triangle for each face
        int[] indices = new int[]{
                //Front face
                0, 1, 2, 0, 2, 3,
                //Top face
                4, 6, 5, 6, 7, 5,
                //Back face
                8, 11, 10, 8, 10, 9,
                //Bottom Face
                14, 12, 13, 14, 13, 15,
                //Left face
                16, 19, 18, 16, 17, 19,
                //Right face
                20, 22, 21, 22, 23, 21
        };
        Mesh lMesh = new Mesh(vertices, m, normals, indices, 8);
        Map<String, String> lOptionalAttributes = MeshBuilder.getPositionAttributesMap(xLength, yLength, zLength);
        lMesh.putOptionalAttributes(lOptionalAttributes);
        return lMesh;

    }
}
