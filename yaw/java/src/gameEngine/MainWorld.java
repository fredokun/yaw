package gameEngine;

import gameEngine.items.GenericItem;
import gameEngine.items.ItemManagement;
import gameEngine.light.AmbiantLight;
import gameEngine.light.DirectionnalLight;
import gameEngine.light.PointLight;
import gameEngine.light.SpotLight;
import gameEngine.meshGenerator.GroundGenerator;
import gameEngine.meshs.Material;
import gameEngine.skyBox.SkyBox;

import org.joml.Vector3f;


public class MainWorld {

	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		GenericItem c1 =ItemManagement.createBlock(world, 1, 1, 1, 1, 1, 1, 1);
		c1.translate(0, 0, -2);
		world.sl.setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
		world.sl.setSun(new DirectionnalLight(new Vector3f(1,1,1),1,new Vector3f(0,-1,1)));
		world.sl.setAmbiant(new AmbiantLight(0.5f));
		world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));
		world.setSkyBox(new SkyBox(500, 500, 500, new Vector3f(1,0,0)));
//		while(true){
//			Thread.sleep(500);
//			System.out.println(world.getListCamera().size());
//		}
	}

}
