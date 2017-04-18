package embla3d.engine;

import embla3d.engine.items.Item;
import embla3d.engine.items.ItemManagement;
import embla3d.engine.light.AmbientLight;
import embla3d.engine.light.DirectionalLight;
import embla3d.engine.light.SpotLight;
import embla3d.engine.skybox.Skybox;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

/**
 * The Main Class that launches our game engine.
 */
public class MainWorld {
    public static void main(String[] args) throws Exception {
        World world = new World(0, 0, 500, 500);/* Create the world with its dimensions. */
        world.init();  /* Initializes the world */
        (new Thread(world)).start();/* Launches the thread responsible for the display and our game loop. */
        Item c1 = ItemManagement.createBlock(world, 0, 0, 1, 1, 1, 1, 1);
        c1.setBoundingBox(ItemManagement.createBoundingBox(world, 0, 1, 0, 1, 1, 1, 2, true));

        Item c2 = ItemManagement.createBlock(world, 0, 0, 1, 1, 1, 1, 1);
        c2.setBoundingBox(ItemManagement.createBoundingBox(world, 0, 1, 0, 1, 1, 1, 2, true));

        c2.translate(2,2,0);
        c2.rotate(0,0,20);

        System.out.println("Collision ?: "+Collision.isInCollision(c1.getBoundingBox(), c2.getBoundingBox()));


        /* Creating Light for Our World */
        world.getSceneLight().setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));
       /* world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1)))); */

       /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(10, 0, 0)));

        world.getCamera().translate(0, 0, 3);
        while (true) {

            Thread.sleep(50); /* Allows to see the block (cube) move at constant rate. */
        }
    }

}