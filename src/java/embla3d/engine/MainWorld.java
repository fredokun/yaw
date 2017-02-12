package embla3d.engine;

import embla3d.engine.items.GenericItem;
import embla3d.engine.items.ItemManagement;
import embla3d.engine.light.AmbientLight;
import embla3d.engine.light.DirectionalLight;
import embla3d.engine.light.SpotLight;
import embla3d.engine.skybox.Skybox;
import org.joml.Vector3f;

/**
 * The Main Class that launches our game engine.
 */
public class MainWorld {
    public static void main(String[] args) throws Exception {
        World world = new World(0, 0, 500, 500);/* Create the world with its dimensions. */
        world.init();  /* Initializes the world */
        (new Thread(world)).start();/* Launches the thread responsible for the display and our game loop. */
        GenericItem c1 = ItemManagement.createBlock(world, 1, 1, 1, 1, 1, 1, 1); /* Create our block */
        GenericItem c2 = ItemManagement.createPyramid(world, 0, 0, 0, 5, 5, 5, 1); /* Create our Pyramid */
        c1.translate(0, 0, 0); /* Allows to resize our block.*/
        c2.translate(0, 0, -3); /* Allows to resize our Pyramid. */
        c2.translate(0, 0, 0); /* Allows to resize our Pyramid.*/
        c2.rotate(0, 2, 0);
        /* Creating Light for Our World */
        world.sl.setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
        world.sl.setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.sl.setAmbient(new AmbientLight(0.5f));
       /* world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1)))); */

       /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(10, 0, 0)));

        world.c.translate(0, 0, 3);
        while (true) {
            world.c.rotate(0, 5, 0); /* Allows to advance the edge in the scene 3D. */
           /* world.c.translate(0,0,-5); */
            Thread.sleep(50); /* Allows to see the block (cube) move at constant rate. */
        }
    }

}