package yaw.engine.meshs;

import yaw.engine.meshs.strategy.BoundingBoxDrawingStrategy;
import org.joml.Vector3f;
import yaw.engine.meshs.strategy.DefaultDrawingStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic Mesh generator, can create specific mesh just with a map (goal)
 */
public class MeshBuilder {
    private static final String BLOCK_MESH = "block";
    private static final String GROUND_MESH = "ground";
    private static final String HALF_BLOCK_MESH = "half-block";
    private static final String PYRAMID_MESH = "pyramid";
    private static final String OCTAHEDRON_MESH = "octahedron";
    private static final String TETRAHEDRON_MESH = "tetrahedron";
    //

    //TODO remove the text coord array, only use it for test

    /**
     * Generate a mesh with the specified material (cx, cy, cz, reflectance), width, length and height
     *
     * @param meshType meshType
     * @param xLength  width
     * @param yLength  length
     * @param zLength  height
     * @param cx       Red value of the material
     * @param cy       Green  value of the material
     * @param cz       Blue  value of the material
     * @return Mesh
     */
    @Deprecated
    public static Mesh generate(String meshType, float xLength, float yLength, float zLength, float cx, float cy, float cz) {
        Mesh mesh = null;
        /*Warning default reflectance*/
        Material lMaterial = null;
        try {
            lMaterial = new Material(new Vector3f(cx, cy, cz));
        } catch (Exception pE) {
            pE.printStackTrace();
        }
        //System.out.println(meshType);
        switch (meshType) {
            case BLOCK_MESH:
                mesh = generateBlock(xLength, yLength, zLength);
                break;
            case GROUND_MESH:
                mesh = generateGround(xLength, yLength, zLength);
                break;
            case HALF_BLOCK_MESH:
                mesh = generateHalfBlock(xLength, yLength, zLength);
                break;
            case PYRAMID_MESH:
                mesh = generatePyramid(xLength, yLength, zLength);
                break;
            case OCTAHEDRON_MESH:
                mesh = generateOctahedron();
                break;
            case TETRAHEDRON_MESH:
                mesh = generateTetrahedron();
                break;
            default:
                System.out.println("error");
        }
        if (mesh != null) mesh.setMaterial(lMaterial);
        return mesh;
        //return generate(xLength, yLength, zLength, new Material(new Vector3f(cx, cy, cz), r));
    }

    /**
     * Generate a BlockMesh with the specified material , width, length and height.
     * Create vertices based on the specified width, length and height.
     * Normals and indices are hard coded
     *
     * @param xLength xLength
     * @param yLength yLength
     * @param zLength zLength
     * @return BlockMesh
     */
    @Deprecated
    public static Mesh generateBlock(float xLength, float yLength, float zLength) {
        float x = xLength / 2f;
        float y = yLength / 2f;
        float z = zLength / 2f;
        float[] vertices = new float[]{
                //Front face
                x, y, z, -x, y, z, -x, -y, z, x, -y, z,
                //Top face
                x, y, z, -x, y, z, x, y, -z, -x, y, -z,
                //Back face
                x, y, -z, -x, y, -z, -x, -y, -z, x, -y, -z,
                //Bottom face
                x, -y, z, -x, -y, z, x, -y, -z, -x, -y, -z,
                //Left face
                -x, y, z, -x, y, -z, -x, -y, z, -x, -y, -z,
                //Right face
                x, y, z, x, y, -z, x, -y, z, x, -y, -z};
        //for light
        float[] normals = {
                //Front face
                0, 0, 1f, 0, 0, 1f, 0, 0, 1f, 0, 0, 1f,
                //Top face
                0, 1f, 0, 0, 1f, 0, 0, 1f, 0, 0, 1f, 0,
                //Back face
                0, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, -1f,
                //Bottom face
                0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0,
                //Left face
                -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
                //Right face
                1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0};
        //mapping for the texture
        float[] textCoord = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
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
                20, 22, 21, 22, 23, 21};
        Mesh lMesh = new Mesh(vertices, textCoord, normals, indices, 8);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        Map<String, String> lOptionalAttributes = MeshBuilder.getPositionAttributesMap(xLength, yLength, zLength);
        lMesh.putOptionalAttributes(lOptionalAttributes);
        return lMesh;

    }

    /**
     * Create a map and store each axis individually (3D position)
     *
     * @param xLength width
     * @param yLength length
     * @param zLength height
     * @return the map
     */
    public static Map<String, String> getPositionAttributesMap(float xLength, float yLength, float zLength) {
        Map<String, String> lOptionalAttributes = new HashMap<>();
        lOptionalAttributes.put("xLength", String.valueOf(xLength));
        lOptionalAttributes.put("yLength", String.valueOf(yLength));
        lOptionalAttributes.put("zLength", String.valueOf(zLength));
        return lOptionalAttributes;
    }

    /**
     * Generate a ground Mesh with the specified material , width, length and height.
     * Create vertices based on the specified width, length and height.
     * Normals and indices are hard coded
     *
     * @param width  width
     * @param length length
     * @param height height
     * @return Mesh
     */
    @Deprecated
    public static Mesh generateGround(float width, float length, float height) {
        float halfW = width / 2;
        float halfL = length / 2;
        float[] vertices = new float[]{-halfW, height, -halfL, -halfW, height, halfL, halfW, height, -halfL, halfW, height, halfL};
        float[] normals = new float[]{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};
        int[] indices = new int[]{0, 1, 2, 1, 3, 2};
        Mesh lMesh = new Mesh(vertices, null, normals, indices);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        Map<String, String> lOptionalAttributes = MeshBuilder.getPositionAttributesMap(width, length, height);
        lMesh.putOptionalAttributes(lOptionalAttributes);
        return lMesh;

    }

    /**
     * Generate a half block Mesh with the specified  width, length and height.
     * Create vertices and normals based on the specified width, length and height.
     *
     * @param xLength width
     * @param yLength length
     * @param zLength height
     * @return Mesh
     */
    @Deprecated
    public static Mesh generateHalfBlock(float xLength, float yLength, float zLength) {
        float x = xLength / 2f;
        float y = yLength / 2f;
        float z = zLength / 2f;
        float[] vertices = new float[]{
                //Front face
                -x, -y, z, x, -y, z, x, y, -z, -x, y, -z,
                //Back face
                x, y, -z, -x, y, -z, -x, -y, -z, x, -y, -z,
                //Bottom face
                x, -y, z, -x, -y, z, x, -y, -z, -x, -y, -z,
                //Left face
                -x, y, -z, -x, -y, z, -x, -y, -z,
                //Right face
                x, y, -z, x, -y, z, x, -y, -z};
        float frontNormal = (float) Math.sqrt(yLength * yLength + zLength * zLength);
        if (frontNormal == 0) frontNormal = 0.00000000000001f;
        float yNormal = z / frontNormal;
        float zNormal = y / frontNormal;
        float[] normals = {
                //Front face
                0, yNormal, zNormal, 0, yNormal, zNormal, 0, yNormal, zNormal, 0, yNormal, zNormal,
                //Back face
                0, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, -1f,
                //Bottom face
                0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0,
                //Left face
                -1, 0, 0, -1, 0, 0, -1, 0, 0,
                //Right face
                1, 0, 0, 1, 0, 0, 1, 0, 0};
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
                15, 16, 17};
        Mesh lMesh = new Mesh(vertices, null, normals, indices, 6);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        Map<String, String> lOptionalAttributes = MeshBuilder.getPositionAttributesMap(xLength, yLength, zLength);
        lMesh.putOptionalAttributes(lOptionalAttributes);
        return lMesh;
    }

    /**
     * Generate a ground Mesh with the specified width, length and height.
     * Create vertices and normals based on the specified width, length and height.
     *
     * @param xLength width
     * @param yLength length
     * @param zLength height
     * @return Mesh
     */
    @Deprecated
    public static Mesh generatePyramid(float xLength, float yLength, float zLength) {
        float x = xLength / 2f;
        float y = yLength / 2f;
        float z = zLength / 2f;
        float[] vertices = new float[]{
                //Front face
                0, y, 0, x, -y, z, -x, -y, z,
                //Back face
                0, y, 0, -x, -y, -z, x, -y, -z,
                //Left face
                0, y, 0, -x, -y, z, -x, -y, -z,
                //Right face
                0, y, 0, x, -y, -z, x, -y, z,
                //Bottom face
                x, -y, z, -x, -y, z, x, -y, -z, -x, -y, -z};
        float frontNormal = (float) Math.sqrt(yLength * yLength + zLength * zLength);
        if (frontNormal == 0) frontNormal = 0.00000000000001f;
        float yFNormal = z / frontNormal;
        float zNormal = y / frontNormal;
        float lateralNormal = (float) Math.sqrt(yLength * yLength + xLength * xLength);
        if (lateralNormal == 0) lateralNormal = 0.00000000000001f;
        float xNormal = y / lateralNormal;
        float yLNormal = z / lateralNormal;
        float[] normals = {
                //Front face
                0, yFNormal, zNormal, 0, yFNormal, zNormal, 0, yFNormal, zNormal,
                //Back face
                0, yFNormal, -zNormal, 0, yFNormal, -zNormal, 0, yFNormal, -zNormal,
                //Left face
                -xNormal, yLNormal, 0, -xNormal, yLNormal, 0, -xNormal, yLNormal, 0,
                //Right face
                xNormal, yLNormal, 0, xNormal, yLNormal, 0, xNormal, yLNormal, 0,
                //Bottom face
                0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0};
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
                12, 13, 15, 12, 15, 14};
        Mesh lMesh = new Mesh(vertices, null, normals, indices, 5);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        Map<String, String> lOptionalAttributes = MeshBuilder.getPositionAttributesMap(xLength, yLength, zLength);
        lMesh.putOptionalAttributes(lOptionalAttributes);
        return lMesh;
    }

    /**
     * Generate a regular octohedron mesh
     * default size 1*1*1 for each triangle
     * Warning: vertices are hard coded
     *
     * @return Mesh
     */
    @Deprecated
    public static Mesh generateOctahedron() {
        float[] vertices = new float[]{
                //LTF
                -0.5f, 0f, 0f, 0f, 0.5f, 0f, 0f, 0f, 0.5f,
                //RTF
                0.5f, 0f, 0f, 0f, 0.5f, 0f, 0f, 0f, 0.5f,
                //LBF
                -0.5f, 0f, 0f, 0f, -0.5f, 0f, 0f, 0f, 0.5f,
                //RBF
                0.5f, 0f, 0f, 0f, -0.5f, 0f, 0f, 0f, 0.5f,
                //LTB
                -0.5f, 0f, 0f, 0f, 0.5f, 0f, 0f, 0f, -0.5f,
                //RTB
                0.5f, 0f, 0f, 0f, 0.5f, 0f, 0f, 0f, -0.5f,
                //LBB
                -0.5f, 0f, 0f, 0f, -0.5f, 0f, 0f, 0f, -0.5f,
                //RBB
                0.5f, 0f, 0f, 0f, -0.5f, 0f, 0f, 0f, -0.5f};
        float pos = (float) (1 / Math.sqrt(3));
        float neg = (float) (-1 / Math.sqrt(3));
        //for(int i=0; i< vertices.length; i++)
        //System.out.println(vertices[i]+ " ");
        float[] normals = new float[]{
                //LTF
                neg, pos, pos, neg, pos, pos, neg, pos, pos,
                //RTF
                pos, pos, pos, pos, pos, pos, pos, pos, pos,
                //LBF
                neg, neg, pos, neg, neg, pos, neg, neg, pos,
                //RBF
                pos, neg, pos, pos, neg, pos, pos, neg, pos,
                //LTB
                neg, pos, neg, neg, pos, neg, neg, pos, neg,
                //RTB
                pos, pos, neg, pos, pos, neg, pos, pos, neg,
                //LBB
                neg, neg, neg, neg, neg, neg, neg, neg, neg,
                //RBB
                pos, neg, neg, pos, neg, neg, pos, neg, neg};
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
                21, 22, 23};
        Mesh lMesh = new Mesh(vertices, null, normals, indices, 6);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        return lMesh;
    }

    /**
     * Generate a regular Tetrahedron mesh
     * default size 1*1*1 for each triangle
     * Warning: vertices are hard coded
     *
     * @return Mesh
     */
    @Deprecated
    public static Mesh generateTetrahedron() {
        float[] vertices = new float[]{
                //First Face
                -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f,
                //Snd Face
                -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f,
                //Third Face
                -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f,
                //Last Face
                0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f};
        float pos = (float) (1 / Math.sqrt(3));
        float neg = (float) (-1 / Math.sqrt(3));
        float[] normals = new float[]{
                //First Face
                pos, pos, pos, pos, pos, pos, pos, pos, pos,
                //Snd Face
                neg, pos, neg, neg, pos, neg, neg, pos, neg,
                //Third Face
                neg, neg, pos, neg, neg, pos, neg, neg, pos,
                //Last Face
                pos, neg, neg, pos, neg, neg, pos, neg, neg};
        int[] indices = new int[]{
                //First Face
                0, 2, 1,
                //Snd Face
                3, 4, 5,
                //Third Face
                6, 7, 8,
                //Last Face
                9, 10, 11};

        Mesh lMesh = new Mesh(vertices, null, normals, indices, 8);
        lMesh.setDrawingStrategy(new DefaultDrawingStrategy());
        return lMesh;

    }

    /**
     * Generate a BoundingBoxMesh with the specified material , width, length and height.
     * Create vertices based on the specified width, length and height.
     * Normals and indices are hard coded
     *
     * @param xLength xLength
     * @param yLength yLength
     * @param zLength zLength
     * @return BoundingBoxMesh
     */
    public static Mesh generateBoundingBox(float xLength, float yLength, float zLength) {
        float x = xLength / 2f;
        float y = yLength / 2f;
        float z = zLength / 2f;
        float[] vertices = new float[]{
                //Front face
                x, y, z, -x, y, z, -x, -y, z, x, -y, z,
                //Top face
                x, y, z, -x, y, z, x, y, -z, -x, y, -z,
                //Back face
                x, y, -z, -x, y, -z, -x, -y, -z, x, -y, -z,
                //Bottom face
                x, -y, z, -x, -y, z, x, -y, -z, -x, -y, -z,
                //Left face
                -x, y, z, -x, y, -z, -x, -y, z, -x, -y, -z,
                //Right face
                x, y, z, x, y, -z, x, -y, z, x, -y, -z};

        float[] normals = {0};
        float[] textCoord = {0};

        int[] indices = new int[]{
                //Front face
                0, 1, 1, 2, 2, 3, 3, 0,
                //Top face
                0, 8, 9, 1, 2, 10, 3, 11,
                //Back face
                8, 9, 9, 10, 10, 11, 11, 8};

        Mesh lMesh = new Mesh(vertices, textCoord, normals, indices);
        //we set the new strategy
        lMesh.setDrawingStrategy(new BoundingBoxDrawingStrategy());
        Map<String, String> lOptionalAttributes = MeshBuilder.getPositionAttributesMap(xLength, yLength, zLength);
        lMesh.putOptionalAttributes(lOptionalAttributes);
        return lMesh;
    }
}
