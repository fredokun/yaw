package yaw;
import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.items.Item;
import yaw.engine.items.ItemGroup;
import yaw.engine.meshs.*;

import org.joml.Vector3f;

        import yaw.engine.UpdateCallback;
        import yaw.engine.World;
        import yaw.engine.items.Item;
        import yaw.engine.meshs.Mesh;
        import yaw.engine.meshs.MeshBuilder;

public class GroupRotatingCubes implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private ItemGroup cubes ;
    private float speed = 10;

    public GroupRotatingCubes(ItemGroup cubes) {
        this.cubes = cubes;
    }

    public Item getItem(int i) {
        return cubes.getItems().get(i);
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

        for(int i=0; i<cubes.getItems().size(); i++) {
            cubes.getItems().get(i).rotate(0.0f, 3.1415925f * speed * (float) deltaTime, 0.0f);
        }

    }

    public static void main(String[] args) {
        Mesh cubem = createCube();
        int nbCubes = 5;

        Mesh [] Mcubes = new Mesh [nbCubes];
        ItemGroup Wcubes = new ItemGroup();
        World world = new World(0, 0, 800, 600);

        for(int i=0; i <nbCubes; i++){
            Mcubes[i]= createCube();

            float[] pos = {(float)Math.random()*(4)-2, (float)Math.random()*(4)-2, -6f };
            Item tmp = world.createItem(String.format("cube%d", i),pos,1.0f,Mcubes[i]);
            tmp.getAppearance().getMaterial().setTexture(new Texture("/ressources/diamond_block.png"));

            Wcubes.add(tmp);

        }

        yaw.GroupRotatingCubes rCube = new yaw.GroupRotatingCubes(Wcubes);

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
