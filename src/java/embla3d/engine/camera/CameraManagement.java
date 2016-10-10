package embla3d.engine.camera;

import java.util.Collections;

import org.joml.Vector3f;

import embla3d.engine.World;

public class CameraManagement {

    public static Camera addCamera(World world){
	Camera c = new Camera();
	world.getCamerasList().add(c);
	return c;
    }
	
    public static Camera addCamera(World world,Camera c){
	if(!world.getCamerasList().contains(c))
	    world.getCamerasList().add(c);
	return c;
    }
	
    public static Camera getLiveCamera(World world){
	return world.getCamera();
    }
	
    public static void setLiveCamera(World world, Camera c){
	Camera x=world.getCamera();
	world.getCamerasList().add(x);
	world.setCamera(c);
	world.getCamerasList().removeAll(Collections.singleton(c));
		
    }
    public static Camera getCamera(World world,int i){
	if(i<0 || i>=world.getCamerasList().size())
	    return null;
	return world.getCamerasList().get(i);
    }
	
    public static void removeCamera(World world,Camera c){
	world.getCamerasList().remove(c);
    }
	
    public static void removeCameraNumber(World world,int i){
	if(i<0 || i>=world.getCamerasList().size())
	    return;
	world.getCamerasList().remove(i);
    }
	
    public static void setPositionCamera(Camera c,float x,float y,float z){
	c.setPosition(new Vector3f(x,y,z));
    }
	
    public static Vector3f getPosition(Camera c){
	return c.getPosition();
    }

}
