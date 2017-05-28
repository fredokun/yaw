package embla3d.engine.items;

import embla3d.engine.meshs.Mesh;
import embla3d.engine.meshs.MeshBuilder;
import org.joml.Vector3f;

/**
 * Class to create some shapes (Block, HalfBlock, Pyramid, Octahedron, Tetrahedron) * and basic functions for these items
 * TODO Refactor change these create method into a more generic method
 * mesh_name must be a parameter of the function
 */
public class ItemManagement {
    /**
     * Create an item with the specified parameters and add it to the  world
     *
     * @param id        id
     * @param pPosition position
     * @param pScale    scale
     * @param pMesh     mesh
     * @return the item
     */
    public static Item createItem(String id, float[] pPosition, float pScale, Mesh pMesh) {
        Item item = new Item(id, pPosition, pScale, false, pMesh);

        return item;
    }

    /*
        public static Item createBlock( float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
            Mesh appearance = MeshBuilder.generate(BLOCK_MESH, xLength, yLength, zLength, red, green, blue);
            Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f));
            //
            Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);

            return item;
        }

        public static Item createHalfBlock( float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
            Mesh appearance = MeshBuilder.generate(HALF_BLOCK_MESH, xLength, yLength, zLength, red, green, blue);
            Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);

            return item;
        }

        public static Item createPyramid( float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
            Mesh appearance = MeshBuilder.generate(PYRAMID_MESH, xLength, yLength, zLength, red, green, blue);
            Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);

            return item;
        }

        public static Item createRegTetrahedron( float red, float green, float blue, float scale) {
            Mesh appearance = MeshBuilder.generate(TETRAHEDRON_MESH, 0, 0, 0, red, green, blue);
            Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);

            return item;
        }

        public static Item createRegOctahedron( float red, float green, float blue, float scale) {
            Mesh appearance = MeshBuilder.generate(OCTAHEDRON_MESH, 0, 0, 0, red, green, blue);
            Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);

            return item;
        }

        public static Item createGround( float red, float green, float blue, float width, float length) {
            Mesh appearance = MeshBuilder.generate(GROUND_MESH, width, length, 0, red, green, blue);
            Item item = new Item(appearance, 1.0f, new Vector3f(0f, -2f, 0f), false);

            return item;
        }
    */
    public static Item createBoundingBox(String id, float[] pPosition, float pScale, float[] pLength) {
        Mesh appearance = MeshBuilder.generateBoundingBox(pLength[0], pLength[1], pLength[2]);
        appearance.getMaterial().setColor(new Vector3f(0,255,0));
        Item item = new Item(id, new Vector3f(pPosition[0], pPosition[0], pPosition[0]), new Vector3f(), pScale, true, appearance, null);

        return item;
    }


}