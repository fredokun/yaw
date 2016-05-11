package gameEngine.light;

import org.joml.Vector3f;

public class PointLight {
	//Light basic attribute
	public Vector3f color;
	public Vector3f position;
	public float intensity;
	
	//Attenuation Attribute
	public float constantAtt;
	public float linearAtt;
	public float quadraticAtt;
	
	public PointLight(Vector3f color, Vector3f position, float intensity,
			float constantAtt, float linearAtt, float quadraticAtt) {
		this.color = color;
		this.position = position;
		this.intensity = intensity;
		this.constantAtt = constantAtt;
		this.linearAtt = linearAtt;
		this.quadraticAtt = quadraticAtt;
	}
	
	public PointLight(){
		intensity = 0;
		color = new Vector3f(0,0,0);
		position = new Vector3f(0,0,0);
		constantAtt =0;
		linearAtt=0;
		quadraticAtt=0;
	}
	
	public PointLight(PointLight source){
		intensity = source.intensity;
		color = source.color;
		position = source.position;
		constantAtt =source.constantAtt;
		linearAtt=source.linearAtt;
		quadraticAtt=source.quadraticAtt;
	}

	public Vector3f getColor() {
		return color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getIntensity() {
		return intensity;
	}

	public float getConstantAtt() {
		return constantAtt;
	}

	public float getLinearAtt() {
		return linearAtt;
	}

	public float getQuadraticAtt() {
		return quadraticAtt;
	}
	
	
}
