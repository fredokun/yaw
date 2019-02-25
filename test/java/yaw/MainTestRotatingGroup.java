package yaw;

import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.items.Item;
import yaw.engine.items.ItemGroup;
import yaw.engine.light.SpotLight;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.MeshBuilder;
import yaw.engine.meshs.Texture;

public class MainTestRotatingGroup implements UpdateCallback {
    private int nbUpdates = 0;
    private double totalDeltaTime = 0.0;
    private static long deltaRefreshMillis = 1000;
    private long prevDeltaRefreshMillis = 0;
    private ItemGroup cubes ;
    private float speed = 10;

    public MainTestRotatingGroup(ItemGroup cubes) {
        this.cubes = cubes;
    }

    public ItemGroup getItem() {
        return cubes;
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

        cubes.rotate(0.0f, 3.1415925f * speed * (float) deltaTime, 0.0f);
        for(int i=0; i<cubes.getItems().size();i++){
            cubes.getItems().get(i).rotate(0.0f, -6.283f * speed * (float) deltaTime, 0.0f);
        }


    }

    public static void main(String[] args) {
        Mesh cubem = createCube();
        Mesh cubem2 = createCube();


        World world = new World(0, 0, 800, 600);

        world.getCamera().setPosition(0,0,0);
        world.getSceneLight().setSpotLight(new SpotLight(1, 300, 1, 0, 0, 2, 1f, 0, 0.1f, 0, 0, 0, -.1f, 10f), 1);


        float[] pos = { -1.5f, 0f, -6f };
        float[] pos2 = { 1.5f, 0f, -6f };


        Item cube = world.createItem("cube", pos, 1.0f, cubem);
        cube.getAppearance().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        Item cube2 = world.createItem("cube2", pos2, 1.0f, cubem2);
        cube2.getAppearance().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        ItemGroup g = new ItemGroup();
        g.add(cube);
        g.add(cube2);
        MainTestRotatingGroup rGroup = new MainTestRotatingGroup(g);



        world.registerUpdateCallback(rGroup);


        Thread th = new Thread(world);
        th.start();


        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
