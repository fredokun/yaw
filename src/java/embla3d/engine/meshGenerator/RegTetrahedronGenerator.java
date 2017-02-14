package embla3d.engine.meshGenerator;

import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Vector3f;

public class RegTetrahedronGenerator {
    /**
     * Generate a regular tetrahedron mesh with the specified material (cx, cy, cz, reflectance)
     *
     * @param cx Red value of the material
     * @param cy Green  value of the material
     * @param cz Blue  value of the material
     * @param r  reflectance
     * @return Mesh
     */
    public static Mesh generate(float cx, float cy, float cz, float r) {
        return generate(new Material(new Vector3f(cx, cy, cz), r));
    }

    /**
     * Generate a regular Tetrahedron mesh  with the specified material
     * default size 1*1*1 for each triangle
     * Warning: vertices are hard coded
     *
     * @param m material
     * @return Mesh
     */
    public static Mesh generate(Material m) {
        float[] vertices = new float[]{
                //First Face
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                //Snd Face
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                //Third Face
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                //Last Face
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f
        };
        float pos = (float) (1 / Math.sqrt(3));
        float neg = (float) (-1 / Math.sqrt(3));
        float[] normals = new float[]{
                //First Face
                pos, pos, pos,
                pos, pos, pos,
                pos, pos, pos,
                //Snd Face
                neg, pos, neg,
                neg, pos, neg,
                neg, pos, neg,
                //Third Face
                neg, neg, pos,
                neg, neg, pos,
                neg, neg, pos,
                //Last Face
                pos, neg, neg,
                pos, neg, neg,
                pos, neg, neg
        };
        int[] indices = new int[]{
                //First Face
                0, 2, 1,
                //Snd Face
                3, 4, 5,
                //Third Face
                6, 7, 8,
                //Last Face
                9, 10, 11
        };

        return new Mesh(vertices, m, normals, indices);

    }
}
