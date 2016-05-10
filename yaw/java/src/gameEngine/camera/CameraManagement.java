package gameEngine.camera;

import org.joml.Vector3f;

import gameEngine.World;

public class CameraManagement {

	public static Camera addCamera(World world){
		Camera c = new Camera();
		world.getListCamera().add(c);
		return c;
	}
	
	public static Camera getLiveCamera(World world){
		return world.getCamera();
	}
	
	public static void setLiveCamera(World world, Camera c){
		world.setCamera(c);
		
	}
	public static Camera getCamera(World world,int i){
		return world.getListCamera().get(i);
	}
	
	public static void removeCamera(World world,Camera c){
		world.getListCamera().remove(c);
	}
	
	public static void removeCameraNumber(World world,int i){
		world.getListCamera().remove(i);
	}
	
	public static void setPositionCamera(Camera c,float x,float y,float z){
		c.setPosition(new Vector3f(x,y,z));
	}
	
	public static void setPositionCameraSec(Camera c,Vector3f v){
		c.setPosition(v);
	}
}
