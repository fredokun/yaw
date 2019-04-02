package yaw;

import org.joml.Vector3f;
import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.items.HitBox;
import yaw.engine.items.ItemGroup;
import yaw.engine.items.ItemObject;
import yaw.engine.light.AmbientLight;
import yaw.engine.light.DirectionalLight;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.MeshBuilder;
import yaw.engine.skybox.Skybox;

/**
 * Test class for Hit boxes: rotations, colllision, multi-bounding-box hitboxes
 */
public class HitboxRotate implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private ItemGroup cube_1_hitbox ;
    private float speed = 10;

    public HitboxRotate(ItemGroup cubes_1) {
        this.cube_1_hitbox = cubes_1;
    }

    static Mesh createGroupHb(int n) {
        Mesh mesh = MeshBuilder.generateBlock(1, 1, 1);
        return mesh;
    }


    @Override
    public void update(double deltaTime) {
        nbUpdates++;
        totalDeltaTime += deltaTime;

        long currentMillis = System.currentTimeMillis();
        if (currentMillis - prevDeltaRefreshMillis > deltaRefreshMillis) {
            double avgDeltaTime = totalDeltaTime / (double) nbUpdates;
            System.out.println("Average deltaTime = " + Double.toString(avgDeltaTime) +" s ("+nbUpdates+")");
            nbUpdates = 0;
            totalDeltaTime = 0.0;
            prevDeltaRefreshMillis = currentMillis;
        }

        // A quick view of rotating hitboxes



        cube_1_hitbox.rotate(1f, 1f, 0f);


    }


    public static void main(String[] args)  {
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



        //Initial position for the cubes
        float[] f = new float[]{0.f, 0.f, 0.f};


        //Dimensions of each hitbox
        float[] tabA = {1.0f, 1.0f, 0.5f};


        //Creation of the First group
        ItemGroup gr1 = new ItemGroup();
        ItemObject c1 = world.createItemObject("1", f, 1, MeshBuilder.generateHalfBlock(1, 1, 1));
        gr1.add("item",c1);
        HitBox i = world.createHitBox("c1 first bounding box",f,1f,tabA);
        i.setPosition(new Vector3f(f[0]+0.f,f[1]+0.f,f[2]+0.25f));
        gr1.add("hitbox 1", i);
        HitBox i2 = world.createHitBox("c1 second bounding box",f,1f,tabA);
        i2.setPosition(new Vector3f(f[0]-0.f,f[1]-0.f,f[2]-0.25f));
        gr1.add("hitbox 2", i2);







        //System.out.println("Collision ?: " + Collision.isInCollision(c1, c2));
        //System.out.println("Collision ?: " + Collision.isCollision(c1, c2));


        /* Creating Light for Our World */
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));

        //world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));

        /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0,0,0)));


        HitboxRotate mBb = new HitboxRotate(gr1);

        gr1.rotate(0,45,0);
        world.getCamera().translate(0,0,7);

        world.registerUpdateCallback(mBb);


        Thread th = new Thread(world);
        th.start();



        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
