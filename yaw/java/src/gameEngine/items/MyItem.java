package gameEngine.items;

import gameEngine.Mesh;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MyItem {
	private Mesh apparence;
	
	private float scale;
	private Vector3f rotation;
	private Vector3f translation;
	
	public MyItem(Mesh apparence, float scale, Vector3f rotation,
			Vector3f position) {
		super();
		this.apparence=apparence;
		this.scale = scale;
		this.rotation = rotation;
		this.translation = position;
	}

	
	
	public MyItem(Mesh m) {
		this.apparence=m;
		scale = 1f;
		rotation = new Vector3f();
		translation = new Vector3f();
	}

	public Mesh getApparence() {
		return apparence;
	}


	public Matrix4f getWorldMatrix() {
        Matrix4f worldMatrix=new Matrix4f().identity().translate(translation).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
        return worldMatrix;
    }



	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float val) {
		scale=val;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}



	public Vector3f getTranslation() {
		return translation;
	}

	public void revolveAround(Vector3f center, float degX, float degY, float degZ){
		Vector4f pos = new Vector4f(translation,1f);
		pos.add(-center.x, -center.y,-center.z,0);
		Matrix4f trans = new Matrix4f();
		trans.rotateX((float) Math.toRadians(degX));
		trans.rotateY((float) Math.toRadians(degY));
		trans.rotateZ((float) Math.toRadians(degZ));
		trans.transform(pos);
		pos.add(center.x, center.y,center.z,0);
		translation = new Vector3f(pos.x,pos.y,pos.z);
	}

	public void repelBy(Vector3f center, float dist){
		Vector3f dif = new Vector3f(translation.x - center.x, translation.y - center.y, translation.z - center.z);
		float norme = dif.length();
		if(norme != 0){
			float move =(dist / norme) + 1;
			dif.mul(move);
			dif.add(center);
			translation = dif;
		}
	}
	
	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}
	
	public void update(){
		
	}
	
}
