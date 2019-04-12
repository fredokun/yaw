package yaw.engine.debug;

import yaw.engine.World;
import yaw.engine.items.HitBox;
import yaw.engine.items.ItemGroup;
import yaw.engine.items.ItemObject;
import yaw.engine.light.AmbientLight;
import yaw.engine.light.DirectionalLight;
import yaw.engine.light.SpotLight;
import yaw.engine.skybox.Skybox;
import org.joml.Vector3f;
import yaw.engine.meshs.MeshBuilder;

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
        //LoggerYAW.getInstance().activateConsoleMode();
        World world = new World(0, 0, 700, 700);/* Create the world with its dimensions. */
        //THE WORLD IS NOW INIT IN THE THREAD
        //testing texture here
        /*Float[] f = new Float[]{0.f, 0.f, -2.f};
        Item c1 = world.createItem("1", f, 1, MeshBuilderOld.generateBlock(1, 1, 1));

        Item c2 = world.createItem("2", f, 3, MeshBuilderOld.generateTetrahedron());

        c1.translate(0, 0, 0); *//* Allows to resize our block.*//*
        c2.setPosition(-3, 0, 0); *//* Allows to resize our Pyramid. *//*
        c2.translate(10, 0, 0); *//* Allows to resize our Pyramid.*//*
        c2.rotate(-3, 2, 0);*/
        (new Thread(world)).start();/* Launches the thread responsible for the display and our game loop. */


        ItemGroup g1 = new ItemGroup("g1");
        ItemObject c1 = world.createItemObject("1", 0.0f, 0.0f, -2.0f, 1, MeshBuilder.generateHalfBlock(1, 1, 1));
        HitBox i = world.createHitBox("c1 first bounding box",0.0f, 0.0f, -2.0f, 1f, 1.0f, 1.0f, 1.0f);
        g1.add("cube", c1);
        g1.add("hitbox", i);




        ItemGroup g2 = new ItemGroup("g2");
        ItemObject c2 = world.createItemObject("2", 0.0f, 0.0f, -2.0f, 1
                , MeshBuilder.generateHalfBlock(1, 1, 1));
        HitBox i2 = world.createHitBox("c1 first bounding box",0.0f, 0.0f, -2.0f,1f, 1.0f, 1.0f, 1.0f);
        g2.add("cube", c2);
        g2.add("hitbox", i2);

        g1.translate(3, 0, 0);
        g2.translate(2, 2, 0);
        g1.rotateXYZ(0, 0, 10);
        g2.rotateXYZ(0, 10, 0);

        System.out.println("Collision ?: " + i.intersect(i2));


        /* Creating Light for Our World */
        world.getSceneLight().setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));

        //world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));

       /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0, 0, 0)));

        world.getCamera().translate(0, 2, 7);
        while (true) {
            //world.getCamera().rotate(0, 5, 0); /* Allows to advance the edge in the scene 3D. */
            g1.rotateX(1);

            g2.rotateY(1);
            /* world.c.translate(0,0,-5); */
            Thread.sleep(50); /* Allows to see the block (cube) move at constant rate. */
        }
    }

}
