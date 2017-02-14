package embla3d.engine.meshGenerator;

import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Vector3f;

import java.util.Map;

public class GroundGenerator {
    /**
     * Generate a ground mesh with the specified material (cx, cy, cz, reflectance), width, length and height
     *
     * @param width  width
     * @param length length
     * @param height height
     * @param cx     Red value of the material
     * @param cy     Green  value of the material
     * @param cz     Blue  value of the material
     * @param r      reflectance
     * @return Mesh
     */
    public static Mesh generate(float width, float length, float height, float cx, float cy, float cz, float r) {
        return generate(width, length, height, new Material(new Vector3f(cx, cy, cz), r));
    }

    /**
     * Generate a ground Mesh with the specified material , width, length and height.
     * Create vertices based on the specified width, length and height.
     * Normals and indices are hard coded
     *
     * @param width  width
     * @param length length
     * @param height height
     * @param m      material
     * @return Mesh
     */
    public static Mesh generate(float width, float length, float height, Material m) {
        float halfW = width / 2;
        float halfL = length / 2;
        float[] vertices = new float[]{
                -halfW, height, -halfL,
                -halfW, height, halfL,
                halfW, height, -halfL,
                halfW, height, halfL
        };
        float[] normals = new float[]{
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0
        };
        int[] indices = new int[]{
                0, 1, 2,
                1, 3, 2
        };
        Mesh lMesh = new Mesh(vertices, m, normals, indices);
        Map<String, String> lOptionalAttributes = MeshBuilder.getPositionAttributesMap(width, length, height);
        lMesh.putOptionalAttributes(lOptionalAttributes);
        return lMesh;

    }
}
