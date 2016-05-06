package gameEngine.camera;

import gameEngine.Camera;
import gameEngine.World;

public class CameraGestion {

	public static Camera addCamera(World world){
		Camera c = new Camera();
		world.getListCamera().add(c);
		return c;
	}
	
	public static void setLiveCamera(World world, Camera c){
		world.setCamera(c);
		
	}
	public static Camera getCamera(World world,int i){
		return world.getListCamera().get(i);
	}

}
