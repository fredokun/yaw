package yaw;

import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.items.ItemObject;
import yaw.engine.light.DirectionalLight;
import yaw.engine.light.SpotLight;
import yaw.engine.meshs.*;

/**
 *  Just a rotating cube with light
 */
public class RotatingCubeLight implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private ItemObject cube ;
    private float speed = 10;

    public RotatingCubeLight(ItemObject cube) {
        this.cube = cube;
    }

    public ItemObject getItem() {
        return cube;
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

        cube.rotate(0.0f, 3.1415925f * speed * (float) deltaTime, 0.0f);


    }

    public static void main(String[] args) {
        Mesh cubem = createCube();


        World world = new World(0, 0, 800, 600);
        world.getCamera().setPosition(0,5,5);
        world.getCamera().rotate(-45,0,0);

        //world.getSceneLight().getSpotTable()[0] = new SpotLight(0, 255, 0, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -5, 10f);
        world.getSceneLight().getSpotTable()[1] = new SpotLight(0, 255, 0, 0f, -0.3f,-3f, 1, 0, 0.75f, 0, 0, -5, -5, 3);
        world.getSceneLight().getSpotTable()[2] = new SpotLight(255, 0, 0, 0, 0.3f, -3, 1f, 0, 0.75f, 0, 0f, -5, -5, 3f);
        world.getSceneLight().setSun(new DirectionalLight());

        float[] pos = { 0f, 0f, 0f };
        ItemObject cube = world.createItemObject("cube", pos, 1.0f, cubem);
        cube.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        //cube.rotate(0,45,0);
        RotatingCubeLight rCube = new RotatingCubeLight(cube);

        world.registerUpdateCallback(rCube);

        Thread th = new Thread(world);
        th.start();


        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
