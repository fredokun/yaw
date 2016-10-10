package embla3d.engine.meshs;

import org.joml.Vector3f;

public class Material {
    public Vector3f color;
    // Reflectance should be between 0 and 1
    public float reflectance;
    
    public Material(Vector3f color, float reflectance){
    	this.color = color;
    	this.reflectance= reflectance;
    }
    
    public Material(Vector3f color){
    	this.color = color;
    	this.reflectance= 1000f;
    }
    
    public Material(float reflectance){
    	this.color = new Vector3f(1f,1.f,1.f);
    	this.reflectance= reflectance;
    }
    
    public Material(){
    	this.color = new Vector3f(1.f,1.f,1.f);
    	this.reflectance= 1000;
    }
    
    public Vector3f getColor() {
    	return color;
    }
    
    public float getReflectance() {
    	return reflectance;
    }
}
