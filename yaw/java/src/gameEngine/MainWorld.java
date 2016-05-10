package gameEngine;

import gameEngine.items.GenericItem;
import gameEngine.items.ItemManagement;
import gameEngine.light.AmbiantLight;
import gameEngine.light.PointLight;

import org.joml.Vector3f;


public class MainWorld {

	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		GenericItem c1 =ItemManagement.createBlock(world,1,0,1, 1, 1, 1, 1);
		c1.translate(2, 0, -8);
		System.out.println(c1.getTranslation());
		world.sl.setPointLight(new PointLight(new Vector3f(0.5f,0f,0.5f), new Vector3f(), 1, 0, 0.5f, 0), 0);
		world.sl.setAmbiant(new AmbiantLight(0.5f));
		while(true){
			Thread.sleep(500);
			System.out.println(world.getListCamera().size());
		}
	}

}
