package yaw;

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
import org.joml.Vector3f;

/**
 * Test class for Hit boxes: rotations, colllision, multi-bounding-box hitboxes
 */
public class TestHitBox implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private ItemGroup cube_1_hitbox ;
    private ItemGroup cube_2_hitbox ;
    private float speed = 10;

    public TestHitBox(ItemGroup cubes_1, ItemGroup cubes_2) {
        this.cube_1_hitbox = cubes_1;
        this.cube_2_hitbox = cubes_2;
    }

    public ItemGroup getItem(int n) {
        if(n==1) return cube_1_hitbox;
        return cube_2_hitbox;
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
        //cube_1_hitbox.rotate(0f, 1f, 0f);
        //cube_2_hitbox.rotate(0f, 1f, 0f);

        //Collision Test, this is testing the collision between each hitbox of each item for the example, this will not be the same for accurate tests
        cube_2_hitbox.translate(-0.01f,0,0);
        HitBox hb_1_1 =  cube_1_hitbox.fetchHitBox("hitbox 1");
        HitBox hb_1_2 =  cube_1_hitbox.fetchHitBox("hitbox 2");
        HitBox hb_2_1 =  cube_2_hitbox.fetchHitBox("hitbox 1");
        HitBox hb_2_2 =  cube_2_hitbox.fetchHitBox("hitbox 2");
        if(hb_1_1.collidesWith(hb_2_1) || hb_1_1.collidesWith(hb_2_2) ||
                hb_1_2.collidesWith(hb_2_1) || hb_1_2.collidesWith(hb_2_2)){
            System.out.println("There is a collision");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

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





        //Creation of the First group
        ItemGroup gr1 = new ItemGroup("gr1");
        ItemObject c1 = world.createItemObject("1", 0.0f, 0.0f, 0.0f, 1, MeshBuilder.generateHalfBlock(1, 1, 1));
        gr1.add("item",c1);
        HitBox i = world.createHitBox("c1 first bounding box",0.f,0.f, 0.25f,1f,1.0f, 1.0f, 0.5f);
        gr1.add("hitbox 1", i);
        HitBox i2 = world.createHitBox("c1 second bounding box",0.f,0.f, -0.25f,1f, 1.0f, 1.0f, 0.5f);
        gr1.add("hitbox 2", i2);



        //Creation of the second group
        ItemGroup gr2 = new ItemGroup("gr2");
        ItemObject c2 = world.createItemObject("2", 0.0f, 0.0f, 0.0f, 1, MeshBuilder.generateHalfBlock(1, 1, 1));
        gr2.add("item",c2);
        HitBox j = world.createHitBox("c1 first bounding box",0.0f, 0.0f, 2.25f,1f, 1.0f, 1.0f, 0.5f);
        gr2.add("hitbox 1", j);
        HitBox j2 = world.createHitBox("c1 second bounding box",0.0f, 0.0f, 1.72f,1f, 1.0f, 1.0f, 0.5f);
        gr2.add("hitbox 2", j2);
        gr2.translate(3,0,0);

        //System.out.println("Collision ?: " + Collision.isInCollision(c1, c2));
        //System.out.println("Collision ?: " + Collision.isCollision(c1, c2));

        /* Creating Light for Our World */
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));

        //world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));

        /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0,0,0)));


        TestHitBox mBb = new TestHitBox(gr1, gr2);

        gr1.rotateXYZ(0,45,0);
        world.getCamera().translate(0,0,7);

        world.registerUpdateCallback(mBb);


        world.launch();
        world.waitFortermination();

    }


}
