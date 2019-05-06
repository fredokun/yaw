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
public class IncludedHitBoxTest implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private HitBox hb1 ;
    private HitBox hb2 ;
    private float speed = 10;

    public IncludedHitBoxTest(HitBox h1, HitBox h2) {
        this.hb1 = h1;
        this.hb2 = h2;
    }

    public HitBox getItem(int n) {
        if(n==1) return hb1;
        return hb2;
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

        if(hb2.isIncludedIn(hb1))
            System.out.println("Included");




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




        //Creation of the First group

        HitBox i = world.createHitBox("c1 first bounding box",0.f,0.f, 0.0f,1f,1.0f, 1.0f, 1f);




        //Creation of the second group

        HitBox j = world.createHitBox("c1 first bounding box",0.f, 0.5f, 0f,0.05f, 1.0f, 1.0f, 1f);



        /* Creating Light for Our World */
        world.getSceneLight().setSun(new DirectionalLight(new Vector3f(1, 1, 1), 1, new Vector3f(0, -1, 1)));
        world.getSceneLight().setAmbient(new AmbientLight(0.5f));

        //world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));

        /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0,0,0)));


        IncludedHitBoxTest mBb = new IncludedHitBoxTest(i, j);
        //i.translate(-2,0,0);
        i.rotateXYZ(0,0,45);
        world.getCamera().translate(0,0,7);

        world.registerUpdateCallback(mBb);


        world.launch();
        world.waitFortermination();

    }


}
