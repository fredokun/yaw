package yaw;

import org.joml.Vector3f;
import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.items.ItemObject;
import yaw.engine.meshs.*;

/**
 * Basic example of a cube rotating on y axis
 */
public class RotatingCube implements UpdateCallback {
	private int nbUpdates = 0;
	private double totalDeltaTime = 0.0;
	private static long deltaRefreshMillis = 1000;
	private long prevDeltaRefreshMillis = 0;
	private ItemObject cube ;
	private float speed = 100;
	
	public RotatingCube(ItemObject cube) {
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

		cube.rotateXYZ(0f, 3.1415925f * speed * (float) deltaTime, 0f);
		cube.rotateZAround(1f, new Vector3f(0f, 0f, -3f));
		//cube.rotateX(0.0f);
		//cube.rotateY(3.1415925f * speed * (float) deltaTime);


	}
	
	public static void main(String[] args) {
		Mesh cubem = createCube();
		
		World world = new World(0, 0, 800, 600);
		
		ItemObject cube = world.createItemObject("cube", 0f, 0f, -2f, 1.0f, cubem);
		cube.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
		cube.translate(2f,0f, -5f);

		RotatingCube rCube = new RotatingCube(cube);


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
