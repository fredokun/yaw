package embla3d.engine.light;

import org.joml.Vector3f;

/**
* The ambient light is a type of light comes from everywhere in the space and illuminates all the objects in the same way.
*
*/

public class AmbientLight {
    private Vector3f color;
    private  float intensity;
    // Value put in the shader
    private Vector3f shaderValue;

    /**
     * Constructor with the specified color and intensity
     * Create the shaderValue with the color and intensity.
     *
     * @param color     color
     * @param intensity intensity
     */
    public AmbientLight(Vector3f color, float intensity){
	this.color = color;
	this.intensity = intensity;
	this.shaderValue= new Vector3f(color.x*intensity,color.y *intensity,color.z*intensity);
    }

    /**
     * Constructor with the specified color (cx,cy,cz) and intensity
     * Create the shaderValue with the color and intensity.
     *
     * @param cx        Red value
     * @param cy        Green value
     * @param cz        Blue value
     * @param intensity intensity
     */
    public AmbientLight(float cx, float cy, float cz, float intensity){
	this.color = new Vector3f(cx, cy, cz);
	this.intensity = intensity;
	this.shaderValue= new Vector3f(color.x*intensity,color.y *intensity,color.z*intensity);
    }

    /**
     * Constructor without parameters
     * Create the shaderValue with 1.
     */
    public AmbientLight(){
	this.color = new Vector3f(1f,1f,1f);
	this.intensity=1f;
	this.shaderValue = new Vector3f(1f,1f,1f);
    }

    /**
     * Constructor with the specified intensity
     * Create the shaderValue with the color and intensity.
     *
     * @param intensity intensity
     */
    public AmbientLight(float intensity){
	this.color = new Vector3f(1f,1f,1f);
	this.intensity=intensity;
	this.shaderValue = new Vector3f(color.x *intensity,color.y *intensity,color.z *intensity);
    }

    /**
     * Setter for the color.
     * Initialize the color.
     * Create the shaderValue with the color and intensity.
     *
     * @param color color
     */
    public void setColor(Vector3f color) {
	this.color = color;
	this.shaderValue= new Vector3f(color.x *intensity,color.y *intensity,color.z *intensity);
    }

    /**
     * Initialize the intensity.
     * Create the shaderValue with the color and intensity.
     *
     * @param intensity intensity
     */
    public void setIntensity(float intensity) {
	this.intensity = intensity;
	this.shaderValue= new Vector3f(color.x *intensity,color.y *intensity,color.z*intensity);
    }

    /**
     * getters
     */
    public Vector3f getColor() {
	return color;
    }

    public float getIntensity() {
	return intensity;
    }

    public Vector3f getShaderValue() {
	return shaderValue;
    }	
}
