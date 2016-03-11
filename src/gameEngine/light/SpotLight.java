package gameEngine.light;

import org.joml.Vector3f;

public class SpotLight extends PointLight {
	Vector3f conedir;
    float cutoffAngle;
    
    public SpotLight(Vector3f color, Vector3f position, float intensity,
			float constantAtt, float linearAtt, float quadraticAtt, Vector3f conedir, float cutoffAngle) {
		super(color,position,intensity,constantAtt,linearAtt,quadraticAtt);
		this.conedir = conedir;
		this.cutoffAngle = cutoffAngle;
	}
    
    public SpotLight(){
    	super();
    	conedir = new Vector3f(0,0,0);
    	cutoffAngle = 0;
    }

    public SpotLight(SpotLight source){
    	super(source);
    	conedir = source.conedir;
    	cutoffAngle = source.cutoffAngle;
    }
    
	public Vector3f getConedir() {
		return conedir;
	}

	public float getCutoffAngle() {
		return cutoffAngle;
	}
}
