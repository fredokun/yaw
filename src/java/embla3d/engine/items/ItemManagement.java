package embla3d.engine.items;

import embla3d.engine.World;
import embla3d.engine.meshs.Mesh;
import embla3d.engine.meshs.MeshBuilder;
import org.joml.Vector3f;

import static embla3d.engine.meshs.MeshBuilder.*;

/**
 * Class to create some shapes (Block, HalfBlock, Pyramid, Octahedron, Tetrahedron) * and basic functions for these items
 * TODO Refactor change these create method into a more generic method
 * mesh_name must be a parameter of the function
 */
public class ItemManagement {
    public static Item createBlock(World world, float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
        Mesh appearance = MeshBuilder.generate(BLOCK_MESH, xLength, yLength, zLength, red, green, blue);
        Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);
        world.getSceneVertex().add(item);
        return item;
    }

    public static Item createHalfBlock(World world, float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
        Mesh appearance = MeshBuilder.generate(HALF_BLOCK_MESH, xLength, yLength, zLength, red, green, blue);
        Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);
        world.getSceneVertex().add(item);
        return item;
    }

    public static Item createPyramid(World world, float red, float green, float blue, float xLength, float yLength, float zLength, float scale) {
        Mesh appearance = MeshBuilder.generate(PYRAMID_MESH, xLength, yLength, zLength, red, green, blue);
        Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);
        world.getSceneVertex().add(item);
        return item;
    }

    public static Item createRegTetrahedron(World world, float red, float green, float blue, float scale) {
        Mesh appearance = MeshBuilder.generate(TETRAHEDRON_MESH, 0, 0, 0, red, green, blue);
        Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);
        world.getSceneVertex().add(item);
        return item;
    }

    public static Item createRegOctahedron(World world, float red, float green, float blue, float scale) {
        Mesh appearance = MeshBuilder.generate(OCTAHEDRON_MESH, 0, 0, 0, red, green, blue);
        Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), false);
        world.getSceneVertex().add(item);
        return item;
    }

    public static Item createGround(World world, float red, float green, float blue, float width, float length) {
        Mesh appearance = MeshBuilder.generate(GROUND_MESH, width, length, 0, red, green, blue);
        Item item = new Item(appearance, 1.0f, new Vector3f(0f, -2f, 0f), false);
        world.getSceneVertex().add(item);
        return item;
    }

    public static Item createBoundingBox(World world, float red, float green, float blue, float xLength, float yLength, float zLength, float scale, boolean isBoundingBox) {
        Mesh appearance = MeshBuilder.generate(BLOCK_MESH, xLength, yLength, zLength, red, green, blue);
        Item item = new Item(appearance, scale, new Vector3f(0f, 0f, -2f), isBoundingBox);
        world.getSceneVertex().add(item);
        return item;
    }

    public static Item clone(World world, Item i) {
        Item item = new Item(i);
        world.getSceneVertex().add(item);
        return item;
    }

    public static void removeItem(World world, Item item) {
        world.getSceneVertex().removeItem(item);
        for (ItemGroup g : item.getGroups()) {
            g.remove(item);
        }
    }

    public static ItemGroup createGroup(World world) {
        ItemGroup group = new ItemGroup();
        world.getItemGroupArrayList().add(group);
        return group;
    }

    public static void removeGroup(World world, ItemGroup g) {
        world.getItemGroupArrayList().remove(g);
        for (Item i : g.getItems()) i.removeFromGroup(g);
    }
}