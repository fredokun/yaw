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
    public Vector3f mColor;
    public Vector3f mPosition;
    public float mIntensity;
	
    //Attenuation Attribute
    public float mConstantAtt;
    public float mLinearAtt;
    public float mQuadraticAtt;

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
	this.mColor = color;
	this.mPosition = position;
	this.mIntensity = intensity;
	this.mConstantAtt = constantAtt;
	this.mLinearAtt = linearAtt;
	this.mQuadraticAtt = quadraticAtt;
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
	this.mColor = new Vector3f(cx, cy, cz);
	this.mPosition = new Vector3f(px, py, pz);
	this.mIntensity = intensity;
	this.mConstantAtt = constantAtt;
	this.mLinearAtt = linearAtt;
	this.mQuadraticAtt = quadraticAtt;
    }

    /**
     * Constructor without parameters
     */
    public PointLight(){
	this.mIntensity = 0;
	this.mColor = new Vector3f(0,0,0);
	this.mPosition = new Vector3f(0,0,0);
	this.mConstantAtt =0;
	this.mLinearAtt=0;
	this.mQuadraticAtt=0;
    }

    /**
     * Constructor with the parameters source
     *
     * @param source source
     */
    public PointLight(PointLight source){
	this.mIntensity = source.mIntensity;
	this.mColor = source.mColor;
	this.mPosition = source.mPosition;
	this.mConstantAtt =source.mConstantAtt;
	this.mLinearAtt=source.mLinearAtt;
	this.mQuadraticAtt=source.mQuadraticAtt;
    }

    /**
     * getters and setters
     */
    public Vector3f getColor() {
	return mColor;
    }
    public void setColor(Vector3f color){
	this.mColor=color;
    }
    public void setColor(float r,float g,float b){
	this.mColor=new Vector3f(r,g,b);
    }
    public Vector3f getPosition() {
	return mPosition;
    }
    public void setPosition(Vector3f pos){
	this.mPosition=pos;
    }
    public void setPosition(float x,float y,float z){
	this.mPosition=new Vector3f(x,y,z);
    }
    public float getIntensity() {
	return mIntensity;
    }
    public void setIntensity(float intens){
	this.mIntensity=intens;
    }
    public float getConstantAtt() {
	return mConstantAtt;
    }
    public void setConstantAtt(float att){
	this.mConstantAtt=att;
    }
    public float getLinearAtt() {
	return mLinearAtt;
    }
    public void setLinearAtt(float att){
	this.mLinearAtt=att;
    }
    public float getQuadraticAtt() {
	return mQuadraticAtt;
    }
    public void setQuadraticAtt(float att){
	this.mQuadraticAtt=att;
    }
	
	
}
