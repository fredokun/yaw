package embla3d.engine.debug;

import embla3d.engine.World;
import embla3d.engine.collision.Collision;
import embla3d.engine.items.Item;
import embla3d.engine.light.AmbientLight;
import embla3d.engine.light.DirectionalLight;
import embla3d.engine.light.SpotLight;
import embla3d.engine.meshs.MeshBuilder;
import embla3d.engine.skybox.Skybox;
import org.joml.Vector3f;

/**
 * The Main Class that launches our game engine.
 */
public class MainWorld {
    public static void main(String[] args) throws Exception {
        //This part can be activated if you want some information about the debug
        /*Configuration.DEBUG.set(true);
        Configuration.DEBUG_FUNCTIONS.set(true);
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Configuration.DEBUG_STACK.set(true);
        Configuration.DEBUG_STREAM.set(true);*/
        //LoggerEMBLA3D.getInstance().activateConsoleMode();
        World world = new World(0, 0, 700, 700);/* Create the world with its dimensions. */
        //THE WORLD IS NOW INIT IN THE THREAD
        //testing texture here
        /*Float[] f = new Float[]{0.f, 0.f, -2.f};
        Item c1 = world.createItem("1", f, 1, MeshBuilder.generateBlock(1, 1, 1));

        Item c2 = world.createItem("2", f, 3, MeshBuilder.generateTetrahedron());

        c1.translate(0, 0, 0); *//* Allows to resize our block.*//*
        c2.setPosition(-3, 0, 0); *//* Allows to resize our Pyramid. *//*
        c2.translate(10, 0, 0); *//* Allows to resize our Pyramid.*//*
        c2.rotate(-3, 2, 0);*/
        float[] f = new float[]{0.f, 0.f, -2.f};
        (new Thread(world)).start();/* Launches the thread responsible for the display and our game loop. */

        Item c1 = world.createItem("1", f, 1, MeshBuilder.generateBlock(1, 1, 1));
        float[] tab = {1.0f, 1.0f, 1.0f}; // a cube
        c1.setBoundingBox(world.createBoundingBox("c1 bounding box", f, 2, tab));

        float[] tab2 = {2.0f, 1.0f, 1.0f}; // a rectangle
        Item c2 = world.createItem("1", f, 1, MeshBuilder.generateBlock(1, 1, 1));
        c2.setBoundingBox(world.createBoundingBox("c1 bounding box", f, 2, tab2));

        c1.translate(1, 0, 0);
        c2.translate(2, 2, 0);
        c1.rotate(0, 0, 10);
        c2.rotate(0, 10, 0);

        System.out.println("Collision ?: " + Collision.isInCollision(c1, c2));


        /* Creating Light for Our World */
        world.getSceneLight().setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));

        //world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));

       /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0, 0, 0)));

        world.getCamera().translate(2, 2, 7);
        while (true) {
            //world.getCamera().rotate(0, 5, 0); /* Allows to advance the edge in the scene 3D. */
            //c1.rotate(-1, -1, -1);

            //c2.rotate(-1, -1, -1);
            /* world.c.translate(0,0,-5); */
            Thread.sleep(50); /* Allows to see the block (cube) move at constant rate. */
        }
    }

}