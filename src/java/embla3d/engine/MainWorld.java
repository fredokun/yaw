package embla3d.engine;

import embla3d.engine.items.GenericItem;
import embla3d.engine.items.ItemManagement;
import embla3d.engine.light.AmbientLight;
import embla3d.engine.light.DirectionalLight;
import embla3d.engine.light.SpotLight;
import embla3d.engine.meshGenerator.GroundGenerator;
import embla3d.engine.meshs.Material;
import embla3d.engine.skybox.Skybox;

import org.joml.Vector3f;

public class MainWorld {

	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		GenericItem c1 =ItemManagement.createBlock(world, 1, 1, 1, 1, 1, 1, 1);
		c1.translate(0, 0, -2);
		world.sl.setSpotLight(new SpotLight(1, 1, 1, 0, 0, 0, 1, 0, 0.5f, 0, 0, 0, -1, 10f), 0);
		world.sl.setSun(new DirectionalLight(new Vector3f(1,1,1),1,new Vector3f(0,-1,1)));
		world.sl.setAmbient(new AmbientLight(0.5f));
		//world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));
		world.setSkybox(new Skybox(500, 500, 500, new Vector3f(1,0,0)));
		
		world.c.translate(0, 0, 3);
		while(true){
			world.c.rotate(0,5, 0);
			//world.c.translate(0,0,-5);
			Thread.sleep(50);
		}
	}

}
