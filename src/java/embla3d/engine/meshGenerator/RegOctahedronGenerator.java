package embla3d.engine.meshGenerator;

import embla3d.engine.meshs.Material;
import embla3d.engine.meshs.Mesh;
import org.joml.Vector3f;

public class RegOctahedronGenerator {
    /**
     * Generate a regular octohedron mesh with the specified material (cx, cy, cz, reflectance)
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
     * Generate a regular octohedron mesh  with the specified material
     * default size 1*1*1 for each triangle
     * Warning: vertices are hard coded
     *
     * @param m material
     * @return Mesh
     */
    public static Mesh generate(Material m) {
        float[] vertices = new float[]{
                //LTF
                -0.5f, 0f, 0f,
                0f, 0.5f, 0f,
                0f, 0f, 0.5f,
                //RTF
                0.5f, 0f, 0f,
                0f, 0.5f, 0f,
                0f, 0f, 0.5f,
                //LBF
                -0.5f, 0f, 0f,
                0f, -0.5f, 0f,
                0f, 0f, 0.5f,
                //RBF
                0.5f, 0f, 0f,
                0f, -0.5f, 0f,
                0f, 0f, 0.5f,
                //LTB
                -0.5f, 0f, 0f,
                0f, 0.5f, 0f,
                0f, 0f, -0.5f,
                //RTB
                0.5f, 0f, 0f,
                0f, 0.5f, 0f,
                0f, 0f, -0.5f,
                //LBB
                -0.5f, 0f, 0f,
                0f, -0.5f, 0f,
                0f, 0f, -0.5f,
                //RBB
                0.5f, 0f, 0f,
                0f, -0.5f, 0f,
                0f, 0f, -0.5f
        };
        float pos = (float) (1 / Math.sqrt(3));
        float neg = (float) (-1 / Math.sqrt(3));
        //for(int i=0; i< vertices.length; i++)
        //System.out.println(vertices[i]+ " ");
        float[] normals = new float[]{
                //LTF
                neg, pos, pos,
                neg, pos, pos,
                neg, pos, pos,
                //RTF
                pos, pos, pos,
                pos, pos, pos,
                pos, pos, pos,
                //LBF
                neg, neg, pos,
                neg, neg, pos,
                neg, neg, pos,
                //RBF
                pos, neg, pos,
                pos, neg, pos,
                pos, neg, pos,
                //LTB
                neg, pos, neg,
                neg, pos, neg,
                neg, pos, neg,
                //RTB
                pos, pos, neg,
                pos, pos, neg,
                pos, pos, neg,
                //LBB
                neg, neg, neg,
                neg, neg, neg,
                neg, neg, neg,
                //RBB
                pos, neg, neg,
                pos, neg, neg,
                pos, neg, neg
        };
        int[] indices = new int[]{
                //LTF
                0, 2, 1,
                //RTF
                3, 4, 5,
                //LBF
                6, 7, 8,
                //RBF
                9, 11, 10,
                //LTB
                12, 13, 14,
                //RTB
                15, 17, 16,
                //LBB
                18, 20, 19,
                //RBB
                21, 22, 23
        };
        return new Mesh(vertices, m, normals, indices, 6);
    }
}
