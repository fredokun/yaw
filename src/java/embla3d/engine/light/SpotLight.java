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
    
    public SpotLight(float cx, float cy, float cz, float px, float py, float pz, float intensity,
			float constantAtt, float linearAtt, float quadraticAtt, float cdx, float cdy, float cdz, float cutoffAngle) {
		super(cx,cy,cz,px,py,pz,intensity,constantAtt,linearAtt,quadraticAtt);
		this.conedir = new Vector3f(cdx, cdy, cdz);
		this.cutoffAngle = cutoffAngle;
	}
    
    public SpotLight(){
    	super();
    	conedir = new Vector3f(0,0,-1);
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
	
	public void setConedir(float x,float y,float z){
		this.conedir=new Vector3f(x,y,z);
	}
	
	public void setConeDir(Vector3f cd){
		this.conedir=cd;
	}

	public float getCutoffAngle() {
		return cutoffAngle;
	}
	
	public void setCutoffAngle(float coa){
		this.cutoffAngle=coa;
	}
}
