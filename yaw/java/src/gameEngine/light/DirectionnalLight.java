package gameEngine.light;

import org.joml.Vector3f;

public class DirectionnalLight extends AmbiantLight {
	public Vector3f direction;
	
	public DirectionnalLight(Vector3f direction){
		super(0);
		this.direction = direction;
	}
	
	public DirectionnalLight(DirectionnalLight source){
		super(source.getColor(),source.getIntensity());
		direction=source.direction;
	}
	
	public DirectionnalLight(Vector3f color, float intensity,Vector3f direction){
		super(color, intensity);
		this.direction = direction;
	}
	
	//Default: vertical white light
	public DirectionnalLight(){
		super(0);
		direction = new Vector3f(0f,-1f,0f);
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

}
