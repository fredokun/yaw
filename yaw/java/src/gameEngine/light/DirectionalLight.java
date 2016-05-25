package gameEngine.light;

import org.joml.Vector3f;

public class DirectionalLight extends AmbientLight {
	public Vector3f direction;
	
	public DirectionalLight(Vector3f direction){
		super(0);
		this.direction = direction;
	}
	
	public DirectionalLight(DirectionalLight source){
		super(source.getColor(),source.getIntensity());
		direction=source.direction;
	}
	
	public DirectionalLight(Vector3f color, float intensity,Vector3f direction){
		super(color, intensity);
		this.direction = direction;
	}
	
	public DirectionalLight(float cx, float cy, float cz, float intensity, float dx, float dy, float dz){
		super(cx, cy, cz, intensity);
		this.direction = new Vector3f(dx, dy, dz);
	}
	
	//Default: vertical white light
	public DirectionalLight(){
		super(0);
		direction = new Vector3f(0f,-1f,0f);
	}

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
