package gameEngine;

import gameEngine.light.AmbiantLight;
import gameEngine.light.PointLight;

import org.joml.Vector3f;

import basicMeshes.CubeItem;

public class MainWorld {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		CubeItem c1 =world.CreateCube(1,0,1);
		c1.translate(0, 0, 0);
		world.sl.setPointLight(new PointLight(new Vector3f(00f,0f,1f), new Vector3f(), 1, 0, 1, 0), 0);
		world.sl.setAmbiant(new AmbiantLight(0.5f));
		while(true){
			Thread.sleep(50);
			c1.repelBy(new Vector3f(0,0,-2), 0.2f);
		}
	}

}
