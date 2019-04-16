package yaw;


import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.items.ItemObject;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.MeshBuilder;
import yaw.engine.meshs.Texture;

/**
 *  Camera view from the ceiling
 */
public class TestCeilingCamera implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private float speed = 10;

    public TestCeilingCamera() {

    }


    static Mesh createCube() {
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



    }

    public static void main(String[] args) {
        World world = new World(0, 0, 700, 700);/* Create the world with its dimensions. */

        float[] f = new float[]{0.f, 0.f, 0.f};

        for (int i = 0; i < 10; i++) {
            ItemObject item = world.createItemObject(i + "", 0.0f, 0.0f, 0.0f, 1, MeshBuilder.generateBlock(1, 1, 1));
            item.translate(i,i,i);

            if (i % 3 == 0)
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/grassblock.png"));
            else if (i % 3 == 1)
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/sand.png"));
            else
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        }
        world.getCamera().translate(0.f, 30, 0);
        world.getCamera().rotate(-90,0,0);
        TestCeilingCamera ceilingCamera = new TestCeilingCamera();

        world.registerUpdateCallback(ceilingCamera);

        Thread th = new Thread(world);
        th.start();


        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
