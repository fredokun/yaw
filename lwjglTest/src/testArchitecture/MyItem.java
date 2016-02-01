package testArchitecture;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
	
	
}
