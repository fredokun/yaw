package gameEngine;

import gameEngine.items.GenericItem;
import gameEngine.items.ItemManagement;
import gameEngine.light.AmbiantLight;
import gameEngine.light.DirectionnalLight;
import gameEngine.light.PointLight;
import gameEngine.light.SpotLight;

import org.joml.Vector3f;


public class MainWorld {

	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		GenericItem c1 =ItemManagement.createBlock(world, 1, 1, 1, 1, 1, 1, 1);
		c1.translate(0, 0, -2);
		world.sl.setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
		//world.sl.setSun(new DirectionnalLight(new Vector3f(1,1,1),1,new Vector3f(0,-1,1)));
		world.sl.setAmbiant(new AmbiantLight(0.5f));
//		while(true){
//			Thread.sleep(500);
//			System.out.println(world.getListCamera().size());
//		}
	}

}
