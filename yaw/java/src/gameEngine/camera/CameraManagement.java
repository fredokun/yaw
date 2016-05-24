package gameEngine.camera;

import java.util.Collections;

import org.joml.Vector3f;

import gameEngine.World;

public class CameraManagement {

	public static Camera addCamera(World world){
		Camera c = new Camera();
		world.getListCamera().add(c);
		return c;
	}
	
	public static Camera addCamera(World world,Camera c){
		if(!world.getListCamera().contains(c))
			world.getListCamera().add(c);
		return c;
	}
	
	public static Camera getLiveCamera(World world){
		return world.getCamera();
	}
	
	public static void setLiveCamera(World world, Camera c){
		Camera x=world.getCamera();
		world.getListCamera().add(x);
		world.setCamera(c);
		world.getListCamera().removeAll(Collections.singleton(c));
		
	}
	public static Camera getCamera(World world,int i){
		if(i<0 || i>=world.getListCamera().size())
			return null;
		return world.getListCamera().get(i);
	}
	
	public static void removeCamera(World world,Camera c){
		world.getListCamera().remove(c);
	}
	
	public static void removeCameraNumber(World world,int i){
		if(i<0 || i>=world.getListCamera().size())
			return;
		world.getListCamera().remove(i);
	}
	
	public static void setPositionCamera(Camera c,float x,float y,float z){
		c.setPosition(new Vector3f(x,y,z));
	}
	
	public static Vector3f getPosition(Camera c){
		return c.getPosition();
	}

}
