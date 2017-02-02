package embla3d.engine.light;

import org.joml.Vector3f;

/**
* Point light is a type of light models a light source that’s emitted uniformly form a point in space in all directions.
*
* This class allows to define :
*  -> the light basic attribute : color , position and intensity (between 0 and 1)
*  -> the attenuation is a function of the distance and light. So we need 3 parameters : constant, linear and quadratic.
**/

public class PointLight {
    //Light basic attribute
    public Vector3f color;
    public Vector3f position;
    public float intensity;
	
    //Attenuation Attribute
    public float constantAtt;
    public float linearAtt;
    public float quadraticAtt;

    /**
     * Constructor with the specified color, position, intensity, constantAtt, linearAtt, quadraticAtt
     *
     * @param color         color
     * @param position      position
     * @param intensity     intensity
     * @param constantAtt   constantAtt
     * @param linearAtt     linearAtt
     * @param quadraticAtt  quadraticAtt
     */
    public PointLight(Vector3f color, Vector3f position, float intensity,
		      float constantAtt, float linearAtt, float quadraticAtt) {
	this.color = color;
	this.position = position;
	this.intensity = intensity;
	this.constantAtt = constantAtt;
	this.linearAtt = linearAtt;
	this.quadraticAtt = quadraticAtt;
    }

    /**
     * Constructor with the specified color (cx,cy,cz), position (px,py,pz), intensity, constantAtt, linearAtt, quadraticAtt
     *
     * @param cx            Red value
     * @param cy            Green value
     * @param cz            Blue value
     * @param px            Position x
     * @param py            Position y
     * @param pz            Position z
     * @param intensity     intensity
     * @param constantAtt   constantAtt
     * @param linearAtt     linearAtt
     * @param quadraticAtt  quadraticAtt
     */
    public PointLight(float cx, float cy, float cz, float px, float py, float pz, float intensity,
		      float constantAtt, float linearAtt, float quadraticAtt) {
	this.color = new Vector3f(cx, cy, cz);
	this.position = new Vector3f(px, py, pz);
	this.intensity = intensity;
	this.constantAtt = constantAtt;
	this.linearAtt = linearAtt;
	this.quadraticAtt = quadraticAtt;
    }

    /**
     * Constructor without parameters
     */
    public PointLight(){
	intensity = 0;
	color = new Vector3f(0,0,0);
	position = new Vector3f(0,0,0);
	constantAtt =0;
	linearAtt=0;
	quadraticAtt=0;
    }

    /**
     * Constructor with the parameters source
     *
     * @param source source
     */
    public PointLight(PointLight source){
	intensity = source.intensity;
	color = source.color;
	position = source.position;
	constantAtt =source.constantAtt;
	linearAtt=source.linearAtt;
	quadraticAtt=source.quadraticAtt;
    }

    /**
     * getters and setters
     */
    public Vector3f getColor() {
	return color;
    }
    public void setColor(Vector3f color){
	this.color=color;
    }
    public void setColor(float r,float g,float b){
	this.color=new Vector3f(r,g,b);
    }
    public Vector3f getPosition() {
	return position;
    }
    public void setPosition(Vector3f pos){
	this.position=pos;
    }
    public void setPosition(float x,float y,float z){
	this.position=new Vector3f(x,y,z);
    }
    public float getIntensity() {
	return intensity;
    }
    public void setIntensity(float intens){
	this.intensity=intens;
    }
    public float getConstantAtt() {
	return constantAtt;
    }
    public void setConstantAtt(float att){
	this.constantAtt=att;
    }
    public float getLinearAtt() {
	return linearAtt;
    }
    public void setLinearAtt(float att){
	this.linearAtt=att;
    }
    public float getQuadraticAtt() {
	return quadraticAtt;
    }
    public void setQuadraticAtt(float att){
	this.quadraticAtt=att;
    }
	
	
}
