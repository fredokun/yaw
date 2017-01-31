package embla3d.engine.meshGenerator;

import embla3d.engine.meshs.HalfBlockMesh;
import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Vector3f;

public class HalfBlockGenerator {
    /**
     * Generate a haslf block Mesh with the specified material , width, length and height.
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
                -x, -y, z,
                x, -y, z,
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
                -x, y, -z,
                -x, -y, z,
                -x, -y, -z,
                //Right face
                x, y, -z,
                x, -y, z,
                x, -y, -z
        };

        float frontNormal = (float) Math.sqrt(yLength * yLength + zLength * zLength);
        if (frontNormal == 0)
            frontNormal = 0.00000000000001f;
        float yNormal = z / frontNormal;
        float zNormal = y / frontNormal;

        float[] normals = {
                //Front face
                0, yNormal, zNormal,
                0, yNormal, zNormal,
                0, yNormal, zNormal,
                0, yNormal, zNormal,
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
                //Right face
                1, 0, 0,
                1, 0, 0,
                1, 0, 0
        };

        int[] indices = new int[]{
                //Front face
                0, 1, 2, 0, 2, 3,
                //Back face
                4, 7, 6, 4, 6, 5,
                //Bottom Face
                10, 8, 9, 10, 9, 11,
                //Left face
                12, 14, 13,
                //Right face
                15, 16, 17
        };

        return new HalfBlockMesh(vertices, m, normals, indices, xLength, yLength, zLength);
    }

    /**
     * Generate a half block Mesh with the specified material , width, length and angle.
     * calculate ylenght based on the angle and width
     *
     * @param xLength width
     * @param angle   angle
     * @param zLength height
     * @param m       material
     * @return Mesh
     */
    public static Mesh generate(float xLength, float zLength, int angle, Material m) {
        angle = angle % 90;
        if (angle == 0)
            angle = 1;
        angle = Math.abs(angle);
        float yLength = (float) (Math.tan(angle) * zLength);
        return generate(xLength, yLength, zLength, m);
    }

    /**
     * Generate a half block mesh with the specified material (cx, cy, cz, reflectance), width, length and height
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
