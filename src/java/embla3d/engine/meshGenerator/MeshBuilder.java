package embla3d.engine.meshGenerator;

import embla3d.engine.meshs.Mesh;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ismael on 12/02/2017.
 * Generic Mesh generator, can create specific mesh just with a map (goal)
 */
public class MeshBuilder {
    public static final String BLOCK_MESH = "block";
    public static final String GROUND_MESH = "ground";
    public static final String HALF_BLOCK_MESH = "half-block";
    public static final String PYRAMID_MESH = "pyramid";
    public static final String OCTAHEDRON_MESH = "octahedron";
    public static final String TETRAHEDRON_MESH = "tetrahedron";
    //TODO
    /*generic call*/
    /*dispatch*/

    /**
     * Generate a pyramide mesh with the specified material (cx, cy, cz, reflectance), width, length and height
     *
     * @param meshType meshType
     * @param xLength  width
     * @param yLength  length
     * @param zLength  height
     * @param cx       Red value of the material
     * @param cy       Green  value of the material
     * @param cz       Blue  value of the material
     * @param r        reflectance
     * @return Mesh
     */
    public static Mesh generate(String meshType, float xLength, float yLength, float zLength, float cx, float cy, float cz, float r) {
        Mesh mesh = null;
        System.out.println(meshType);
        switch (meshType) {
            case BLOCK_MESH:
                mesh = BlockGenerator.generate(xLength, yLength, zLength, cx, cy, cz, r);
                break;
            case GROUND_MESH:
                mesh = GroundGenerator.generate(xLength, yLength, zLength, cx, cy, cz, r);
                break;
            case HALF_BLOCK_MESH:
                mesh = HalfBlockGenerator.generate(xLength, yLength, zLength, cx, cy, cz, r);
                break;
            case PYRAMID_MESH:
                mesh = PyramidGenerator.generate(xLength, yLength, zLength, cx, cy, cz, r);
                break;
            case OCTAHEDRON_MESH:
                mesh = RegOctahedronGenerator.generate(cx, cy, cz, r);
                break;
            case TETRAHEDRON_MESH:
                mesh = RegTetrahedronGenerator.generate(cx, cy, cz, r);
                break;
            default:
                System.out.println("error");
        }
        return mesh;
        //return generate(xLength, yLength, zLength, new Material(new Vector3f(cx, cy, cz), r));
    }

    /**
     * Create a map and store each axis individually (3D position)
     *
     * @param xLength width
     * @param yLength length
     * @param zLength height
     * @return the map
     */
    private static Map<String, String> getPostitionAttributesMap(float xLength, float yLength, float zLength) {
        Map<String, String> lOptionalAttributes = new HashMap<>();
        lOptionalAttributes.put("xLength", String.valueOf(xLength));
        lOptionalAttributes.put("yLength", String.valueOf(yLength));
        lOptionalAttributes.put("zLength", String.valueOf(zLength));
        return lOptionalAttributes;
    }
}
