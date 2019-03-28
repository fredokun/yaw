package yaw;

import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.camera.Camera;
import yaw.engine.items.ItemObject;
import yaw.engine.meshs.*;

/**
 * A camera moving on Z
 */
public class MovingCameraOnZ implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private Camera camera;
    private float speed = 10;
    private float z = -10;
    private boolean inversingmove = false;

    public MovingCameraOnZ(Camera c) {
        this.camera = c;
    }

    public Camera getItem() {
        return camera;
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
        if(inversingmove){
            z+=0.1f;
            camera.translate(0,0,0.1f);
            if(z>=10){
                inversingmove = false;
            }
        }else{
            camera.translate(0,0,-0.1f);
            z-=0.1f;
            if(z<=-10){
                inversingmove = true;
            }
        }


    }

    public static void main(String[] args) {
        World world = new World(0, 0, 700, 700);/* Create the world with its dimensions. */

        float[] f = new float[]{0.f, 0.f, 0.f};

        for (int i = 0; i < 5; i++) {

            ItemObject item = world.createItemObject(i + "", f, 1, MeshBuilder.generateBlock(1, 1, 1));
            item.translate(i,i,i);

            if (i % 3 == 0)
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/grassblock.png"));
            else if (i % 3 == 1)
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/sand.png"));
            else
                item.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        }

        world.getCamera().translate(-15, 15, -10); // placing camera to have a side vue of the world
        world.getCamera().rotate(-45,-90,0); //rotate the camera to see the center of the world
        MovingCameraOnZ movingCamera = new MovingCameraOnZ(world.getCamera());


        world.registerUpdateCallback(movingCamera);


        Thread th = new Thread(world);
        th.start();


        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

