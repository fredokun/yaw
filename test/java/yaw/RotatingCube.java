package yaw;

import org.joml.Vector3f;

import yaw.engine.UpdateCallback;
import yaw.engine.World;
import yaw.engine.items.Item;
import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.MeshBuilder;

import java.util.Scanner;

public class RotatingCube implements UpdateCallback {
	private int nbUpdates = 0;
	private double totalDeltaTime = 0.0;
	private static long deltaRefreshMillis = 1000;
	private long prevDeltaRefreshMillis = 0;
	private float vitesse = 2.0f;
	private float rotation = 0.0f;
	private Item cube ;
	
	public RotatingCube(Item cube) {
		this.cube = cube;
	}
	
	public Item getItem() {
		return cube;
	}
	
	static Mesh createCube() {
		Mesh mesh = MeshBuilder.generateBlock(1, 1, 1);
		return mesh;
	}
	
	@Override
	public void update(double deltaTime) {

		rotation += deltaTime;
		/*totalDeltaTime += deltaTime;
		nbUpdates++;
		long currentMillis = System.currentTimeMillis();
		if (currentMillis - prevDeltaRefreshMillis > deltaRefreshMillis) {
			double avgDeltaTime = totalDeltaTime / (double) nbUpdates;
			System.out.println("Average deltaTime = " + Double.toString(avgDeltaTime) +" s");
			System.out.println("NB nbUpdates = " + nbUpdates);
			nbUpdates = 0;
			totalDeltaTime = 0.0;
			prevDeltaRefreshMillis = currentMillis;
		}*/
		
		Vector3f rot = new Vector3f(0.0f,3.1415925f * rotation* vitesse, 0.0f);
		cube.setRotation(rot);
		
		
	}
	
	public static void main(String[] args) {

		Mesh cubem = createCube();

		World world = new World(0, 0, 800, 600,120.0,2.0,3);


		float[] pos3 = { 0f, 0f, -2f };
		Item cube3 = world.createItem("cube3", pos3, 1.0f, cubem);

		RotatingCube rCube = new RotatingCube(cube3);

		world.registerUpdateCallback(rCube);

		Thread th = new Thread(world);
		th.start();

		Scanner sc = new Scanner(System.in);
		float vitesse= sc.nextInt()/10.0f;
		while (vitesse !=-1){
			rCube.vitesse=vitesse;
			vitesse=sc.nextInt()/10.0f;
		}
		
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
