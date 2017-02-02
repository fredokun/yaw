package embla3d.engine.light;

import org.joml.Vector3f;

/**
 * all the objects in the 3D the space are hit by parallel ray lights coming from a specific direction.
 * No matter if the object is close or of far away, all the ray lights impact the objects with the same angle.
 */

public class DirectionalLight extends AmbientLight {
    public Vector3f direction;

    /**
     * Constructor with the parameters direction and the intensity = 0.
     *
     * @param direction direction
     */
    public DirectionalLight(Vector3f direction){
	super(0);
	this.direction = direction;
    }

    /**
     * Constructor with the parameters source.
     *
     * @param source source
     */
    public DirectionalLight(DirectionalLight source){
	super(source.getColor(),source.getIntensity());
	direction=source.direction;
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
	this.direction = direction;
    }

    /**
     * Constructor with the specified color (cx,cy,cz), intensity and direction (dx,dy,dz),
     *
     * @param cx            Red value
     * @param cy            Green value
     * @param cz            Blue value
     * @param intensity     intensity
     * @param dx            Direction x
     * @param dy            Direction y
     * @param dz            Direction z
     */
    public DirectionalLight(float cx, float cy, float cz, float intensity, float dx, float dy, float dz){
	super(cx, cy, cz, intensity);
	this.direction = new Vector3f(dx, dy, dz);
    }
	
    //Default: vertical white light
    public DirectionalLight(){
	super(0);
	direction = new Vector3f(0f,-1f,0f);
    }

    /**
     * getter and setters
     */
    public Vector3f getDirection() {
	return direction;
    }

    public void setDirection(Vector3f direction) {
	this.direction = direction;
    }
	
    public void setDirection(float x,float y,float z){
	this.direction=new Vector3f(x,y,z);
    }

}
