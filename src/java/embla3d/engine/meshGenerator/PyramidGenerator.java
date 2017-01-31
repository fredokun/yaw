package embla3d.engine.meshGenerator;

import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import embla3d.engine.meshs.PyramidMesh;
import org.joml.Vector3f;

public class PyramidGenerator {

    /**
     * Generate a ground Mesh with the specified material , width, length and height.
     * Create vertices and normals based on the specified width, length and height.
     *
     * @param xLength width
     * @param yLength length
     * @param zLength height
     * @param m       material
     * @return Mesh
     */
    public static Mesh generate(float xLength, float yLength, float zLength, Material m) {
        float x = xLength / 2f;
        float y = yLength / 2f;
        float z = zLength / 2f;
        float[] vertices = new float[]{
                //Front face
                0, y, 0,
                x, -y, z,
                -x, -y, z,
                //Back face
                0, y, 0,
                -x, -y, -z,
                x, -y, -z,
                //Left face
                0, y, 0,
                -x, -y, z,
                -x, -y, -z,
                //Right face
                0, y, 0,
                x, -y, -z,
                x, -y, z,
                //Bottom face
                x, -y, z,
                -x, -y, z,
                x, -y, -z,
                -x, -y, -z
        };

        float frontNormal = (float) Math.sqrt(yLength * yLength + zLength * zLength);
        if (frontNormal == 0)
            frontNormal = 0.00000000000001f;
        float yFNormal = z / frontNormal;
        float zNormal = y / frontNormal;

        float lateralNormal = (float) Math.sqrt(yLength * yLength + xLength * xLength);
        if (lateralNormal == 0)
            lateralNormal = 0.00000000000001f;
        float xNormal = y / lateralNormal;
        float yLNormal = z / lateralNormal;

        float[] normals = {
                //Front face
                0, yFNormal, zNormal,
                0, yFNormal, zNormal,
                0, yFNormal, zNormal,
                //Back face
                0, yFNormal, -zNormal,
                0, yFNormal, -zNormal,
                0, yFNormal, -zNormal,
                //Left face
                -xNormal, yLNormal, 0,
                -xNormal, yLNormal, 0,
                -xNormal, yLNormal, 0,
                //Right face
                xNormal, yLNormal, 0,
                xNormal, yLNormal, 0,
                xNormal, yLNormal, 0,
                //Bottom face
                0, -1f, 0,
                0, -1f, 0,
                0, -1f, 0,
                0, -1f, 0
        };

        int[] indices = new int[]{
                //Front face
                0, 2, 1,
                //Back face
                3, 5, 4,
                //Left face
                6, 8, 7,
                //Right face
                9, 11, 10,
                //Bottom Face
                12, 13, 15, 12, 15, 14
        };

        return new PyramidMesh(vertices, m, normals, indices, xLength, yLength, zLength);
    }

    /**
     * Generate a pyramide mesh with the specified material (cx, cy, cz, reflectance), width, length and height
     *
     * @param xLength width
     * @param yLength length
     * @param zLength height
     * @param cx      Red value of the material
     * @param cy      Green  value of the material
     * @param cz      Blue  value of the material
     * @param r       reflectance
     * @return Mesh
     */
    public static Mesh generate(float xLength, float yLength, float zLength, float cx, float cy, float cz, float r) {
        return generate(xLength, yLength, zLength, new Material(new Vector3f(cx, cy, cz), r));
    }
}
