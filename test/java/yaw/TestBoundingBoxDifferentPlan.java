package yaw;

import org.joml.Vector3f;
import yaw.engine.World;
import yaw.engine.collision.Collision;
import yaw.engine.items.Item;
import yaw.engine.light.AmbientLight;
import yaw.engine.light.DirectionalLight;
import yaw.engine.light.SpotLight;
import yaw.engine.meshs.MeshBuilder;
import yaw.engine.skybox.Skybox;

/**
 * The Main Class that launches our game engine.
 */
public class TestBoundingBoxDifferentPlan {
    public static void main(String[] args) throws Exception {

        World world = new World(0, 0, 700, 700);/* Create the world with its dimensions. */

        float[] f = new float[]{0.f, 0.f, 0.f};
        Thread t = new Thread(world);
        t.start();/* Launches the thread responsible for the display and our game loop. */

        Item c1 = world.createItem("1", f, 1, MeshBuilder.generateBlock(1, 1, 1));
        float[] tab = {1.0f, 1.0f, 1.0f}; // a cube
        c1.setBoundingBox(world.createBoundingBox("c1 bounding box", f, 1, tab));

        float[] tab2 = {1.0f, 1.0f, 1.0f}; // a rectangle
        Item c2 = world.createItem("2", f, 1, MeshBuilder.generateBlock(1, 1, 1));
        c2.setBoundingBox(world.createBoundingBox("c1 bounding box", f, 1, tab2));

        c1.translate(0, 0, 0);

        // No collision if an item is in front of an other
        c2.translate(0.75f, 0, 4);

        c1.rotate(0, 0, 0);
        c2.rotate(0, 0, 0);

        System.out.println("Collision ?: " + Collision.isInCollision(c1, c2));


        /* Creating Light for Our World */
        world.getSceneLight().setSpotLight(new SpotLight(5, 0, 0, 0, 0, 0, 0.3f, 0, 0.5f, 0, -0f, 0, -1, 5f), 0);
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));

        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0, 0, 0)));

        world.getCamera().translate(0,0,7);

        while (true) {
            c1.rotate(-0.1f, -1, -1);
            c2.translate(0,0,0);
            c2.rotate(-0.1f, -1, -1);
            System.out.println("Collision ?: " + Collision.isInCollision(c1, c2));
            Thread.sleep(100); /* Allows to see the block (cube) move at constant rate. */
        }

    }

}
