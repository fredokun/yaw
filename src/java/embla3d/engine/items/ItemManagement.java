package embla3d.engine.items;

import embla3d.engine.World;
import embla3d.engine.meshGenerator.MeshBuilder;
import embla3d.engine.meshs.Mesh;
import org.joml.Vector3f;

import static embla3d.engine.meshGenerator.MeshBuilder.*;
import static embla3d.engine.meshs.Material.REFLECTANCE_DEFAULT_VALUE;

/**
 * Class to create some shapes (Block, HalfBlock, Pyramide, Octahedron, Tetreahedron) * and basic fonctions for these items
 * TODO Refactor change these create method into a more generic method
 * mesh_name must be a parameter of the function
 *
 */
public class ItemManagement {
    public static GenericItem createBlock(World world, float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
        Mesh appearance = MeshBuilder.generate(BLOCK_MESH, xLength, yLength, zLength, red, green, blue, REFLECTANCE_DEFAULT_VALUE);
        GenericItem item = new GenericItem(appearance, scale, new Vector3f(), new Vector3f(0f, 0f, -2f));
        world.getSceneVertex().add(item);
        return item;
    }

    public static GenericItem createHalfBlock(World world, float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
        Mesh appearance = MeshBuilder.generate(HALF_BLOCK_MESH, xLength, yLength, zLength, red, green, blue, REFLECTANCE_DEFAULT_VALUE);
        GenericItem item = new GenericItem(appearance, scale, new Vector3f(), new Vector3f(0f, 0f, -2f));
        world.getSceneVertex().add(item);
        return item;
    }

    public static GenericItem createPyramid(World world, float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
        Mesh appearance = MeshBuilder.generate(PYRAMID_MESH, xLength, yLength, zLength, red, green, blue, REFLECTANCE_DEFAULT_VALUE);
        GenericItem item = new GenericItem(appearance, scale, new Vector3f(), new Vector3f(0f, 0f, -2f));
        world.getSceneVertex().add(item);
        return item;
    }

    public static GenericItem createRegTetrahedron(World world, float red, float green, float blue, float scale) {
        Mesh appearance = MeshBuilder.generate(TETRAHEDRON_MESH, 0, 0, 0, red, green, blue, REFLECTANCE_DEFAULT_VALUE);
        GenericItem item = new GenericItem(appearance, scale, new Vector3f(), new Vector3f(0f, 0f, -2f));
        world.getSceneVertex().add(item);
        return item;
    }

    public static GenericItem createRegOctahedron(World world, float red, float green, float blue, float scale) {
        Mesh appearance = MeshBuilder.generate(OCTAHEDRON_MESH, 0, 0, 0, red, green, blue, REFLECTANCE_DEFAULT_VALUE);
        GenericItem item = new GenericItem(appearance, scale, new Vector3f(), new Vector3f(0f, 0f, -2f));
        world.getSceneVertex().add(item);
        return item;
    }

    public static GenericItem createGround(World world, float red, float green, float blue, float width, float length) {
        Mesh appearance = MeshBuilder.generate(GROUND_MESH, width, length, 0, red, green, blue, REFLECTANCE_DEFAULT_VALUE);
        GenericItem item = new GenericItem(appearance, 1.0f, new Vector3f(), new Vector3f(0f, -2f, 0f));
        world.getSceneVertex().add(item);
        return item;
    }

    public static GenericItem clone(World world, GenericItem i) {
        GenericItem item = new GenericItem(i);
        world.getSceneVertex().add(item);
        return item;
    }

    public static void removeItem(World world, GenericItem item) {
        world.getSceneVertex().removeItem(item);
        for (ItemGroup g : item.getGroups()) {
            g.remove(item);
        }
    }

    public static ItemGroup createGroup(World world) {
        ItemGroup group = new ItemGroup();
        world.getGroupsList().add(group);
        return group;
    }

    public static void removeGroup(World world, ItemGroup g) {
        world.getGroupsList().remove(g);
        for (MyItem i : g.getItems()) i.removeFromGroup(g);
    }
}