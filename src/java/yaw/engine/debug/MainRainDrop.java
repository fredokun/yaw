package yaw.engine.debug;


import yaw.engine.World;
import yaw.engine.items.ItemObject;
import yaw.engine.meshs.Texture;
import yaw.engine.meshs.MeshBuilder;
/**
 * The Main Class that launches our game engine.
 */
public class MainRainDrop {
    public static void main(String[] args) throws Exception {
        //This part can be activated if you want some information about the debug
        /*Configuration.DEBUG.set(true);
        Configuration.DEBUG_FUNCTIONS.set(true);
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Configuration.DEBUG_STACK.set(true);
        Configuration.DEBUG_STREAM.set(true);*/
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
        world.launch();/* Launches the thread responsible for the display and our game loop. */

        for (int i = 0; i < 7; i++) {
            ItemObject item = world.createItemObject(i + "", 0.0f, 0.0f, -2.0f, 1
                    , MeshBuilder.generateBlock(1, 1, 1));
            item.translate((int) (Math.random() * 5), (int) (Math.random() * 5), (int) (Math.random() * 5));
            //item.rotate((int) (Math.random() * 90), (int) (Math.random() * 90), (int) (Math.random() * 90));

            if (i % 3 == 0)
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/grassblock.png"));
            else if (i % 3 == 1)
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/sand.png"));
            else
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        }




        /* Creating Light for Our World */
       /* world.getSceneLight().setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));*/

        //world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));

       /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        // world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0, 0, 0)));

        world.getCamera().translate(1, 3, 7);
        while (true) {
            // world.getCamera().rotate(0, 5, 0); /* Allows to advance the edge in the scene 3D. */
            //c1.rotate(-1, -1, -1);

            //c2.rotate(-1, -1, -1);
            /* world.c.translate(0,0,-5); */
            Thread.sleep(50); /* Allows to see the block (cube) move at constant rate. */
        }
    }

}
