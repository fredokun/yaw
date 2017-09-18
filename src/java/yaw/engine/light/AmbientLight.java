package yaw.engine.light;

import org.joml.Vector3f;

/**
* The ambient light is a type of light comes from everywhere in the space and illuminates all the objects in the same way.
*
*/

public class AmbientLight {
    private Vector3f mColor;
    private  float mIntensity;
    // Value put in the shader
    private Vector3f mShaderValue;

    /**
     * Constructor with the specified color and intensity
     * Create the shaderValue with the color and intensity.
     *
     * @param color     color
     * @param intensity intensity
     */
    public AmbientLight(Vector3f color, float intensity){
	this.mColor = color;
	this.mIntensity = intensity;
	this.mShaderValue= new Vector3f(color.x*intensity,color.y *intensity,color.z*intensity);
    }

    /**
     * Constructor with the specified color (cx,cy,cz) and intensity
     * Create the shaderValue with the color and intensity.
     *
     * @param red        Red value
     * @param green      Green value
     * @param blue       Blue value
     * @param intensity  intensity
     */
    public AmbientLight(float red, float green, float blue, float intensity){
	this.mColor = new Vector3f(red, green, blue);
	this.mIntensity = intensity;
	this.mShaderValue= new Vector3f(mColor.x*intensity,mColor.y *intensity,mColor.z*intensity);
    }

    /**
     * Constructor without parameters
     * Create the shaderValue with 1.
     */
    public AmbientLight(){
	this.mColor = new Vector3f(1f,1f,1f);
	this.mIntensity=1f;
	this.mShaderValue = new Vector3f(1f,1f,1f);
    }

    /**
     * Constructor with the specified intensity
     * Create the shaderValue with the color and intensity.
     *
     * @param intensity intensity
     */
    public AmbientLight(float intensity){
	this.mColor = new Vector3f(1f,1f,1f);
	this.mIntensity=intensity;
	this.mShaderValue = new Vector3f(mColor.x *intensity,mColor.y *intensity,mColor.z *intensity);
    }

    /**
     * Setter for the color.
     * Initialize the color.
     * Create the shaderValue with the color and intensity.
     *
     * @param color color
     */
    public void setColor(Vector3f color) {
	this.mColor = color;
	this.mShaderValue= new Vector3f(color.x *mIntensity,color.y *mIntensity,color.z *mIntensity);
    }

    /**
     * Initialize the intensity.
     * Create the shaderValue with the color and intensity.
     *
     * @param intensity intensity
     */
    public void setIntensity(float intensity) {
	this.mIntensity = intensity;
	this.mShaderValue= new Vector3f(mColor.x *intensity,mColor.y *intensity,mColor.z*intensity);
    }

    /**
     * getters
     */
    public Vector3f getColor() {
	return mColor;
    }

    public float getIntensity() {
	return mIntensity;
    }

    public Vector3f getShaderValue() {
	return mShaderValue;
    }	
}
