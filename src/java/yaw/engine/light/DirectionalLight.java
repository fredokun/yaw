package yaw.engine.light;

import org.joml.Vector3f;

/**
 * all the objects in the 3D the space are hit by parallel ray lights coming from a specific direction.
 * No matter if the object is close or of far away, all the ray lights impact the objects with the same angle.
 */

public class DirectionalLight extends AmbientLight {
    public Vector3f mDirection;

    /**
     * Constructor with the parameters direction and the intensity = 0.
     *
     * @param direction direction
     */
    public DirectionalLight(Vector3f direction){
	super(0);
	this.mDirection = direction;
    }

    /**
     * Constructor with the parameters source.
     *
     * @param source source
     */
    public DirectionalLight(DirectionalLight source){
	super(source.getColor(),source.getIntensity());
        mDirection=source.mDirection;
    }

    /**
     * Constructor with the parameters color, intensity and direction.
     *
     * @param color     color
     * @param intensity intensity
     * @param direction direction
     */
    public DirectionalLight(Vector3f color, float intensity,Vector3f direction){
	super(color, intensity);
	this.mDirection = direction;
    }

    /**
     * Constructor with the specified color (cx,cy,cz), intensity and direction (dx,dy,dz),
     *
     * @param red           Red value
     * @param green         Green value
     * @param blue          Blue value
     * @param intensity     intensity
     * @param dx            Direction x
     * @param dy            Direction y
     * @param dz            Direction z
     */
    public DirectionalLight(float red, float green, float blue, float intensity, float dx, float dy, float dz){
	super(red, green, blue, intensity);
	this.mDirection = new Vector3f(dx, dy, dz);
    }
	
    //Default: vertical white light
    public DirectionalLight(){
	super(0);
        mDirection = new Vector3f(0f,-1f,0f);
    }

    /**
     * getter and setters
     */
    public Vector3f getDirection() {
	return mDirection;
    }

    public void setDirection(Vector3f direction) {
	this.mDirection = direction;
    }
	
    public void setDirection(float x,float y,float z){
	this.mDirection=new Vector3f(x,y,z);
    }

}
