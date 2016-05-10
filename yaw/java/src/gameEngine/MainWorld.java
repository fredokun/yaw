package gameEngine;

import gameEngine.items.CubeItem;
import gameEngine.items.GenericItem;
import gameEngine.items.ItemManagement;
import gameEngine.light.AmbiantLight;
import gameEngine.light.PointLight;

import org.joml.Vector3f;


public class MainWorld {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		GenericItem c1 =ItemManagement.createBlock(world,1,0,0, 5, 5, 5, 1);
		c1.translate(0, 0, 0);
		world.sl.setPointLight(new PointLight(new Vector3f(0f,0f,1f), new Vector3f(), 1, 0, 1, 0), 0);
		world.sl.setAmbiant(new AmbiantLight(0.5f));
		while(true){
			Thread.sleep(50);
			c1.repelBy(new Vector3f(0,0,-2), 0.2f);
		}
	}

}
