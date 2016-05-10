package gameEngine.items;

import static org.lwjgl.glfw.GLFW.*;
import gameEngine.Input;
import gameEngine.Material;
import gameEngine.Mesh;
import gameEngine.meshGenerator.BlockGenerator;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CubeItem extends MyItem {

	public CubeItem(Mesh apparence, float scale, Vector3f rotation, Vector3f position) {
		super(apparence, scale, rotation, position);
	}

	public CubeItem(Material m,float scale, Vector3f rotation, Vector3f position) {
		super(BlockGenerator.generate(1, 1, 1, m), scale, rotation, position);
	}
	
	public void translate(float x,float y,float z){
		this.setTranslation(getTranslation().add(x, y, z)); 
	}
	
	public void rotate(float x,float y,float z){
		this.setRotation(getRotation().add(x,y,z));	
	}
	public void update(){
		if(Input.isKeyDown(GLFW_KEY_X)){
			this.setTranslation(getTranslation().add(0.0f, 0.0f, -0.1f)); 
		}

		if(Input.isKeyDown(GLFW_KEY_C)){
			this.setTranslation(getTranslation().add(0.0f, 0.0f, 0.1f)); 
		}
		if(Input.isKeyDown(GLFW_KEY_Q)){
			//Matrix4f.translate(position).multiply(Matrix4f.rotateX(rot)
			this.setRotation(getRotation().add(0.0f,0.5f,0.0f));
		}

		if(Input.isKeyDown(GLFW_KEY_E)){
			//Matrix4f.translate(position).multiply(Matrix4f.rotateX(rot)
			this.setRotation(getRotation().add(0.0f,-0.5f,0.0f));		
			}
		
		if(Input.isKeyDown(GLFW_KEY_R)){
			//Matrix4f.translate(position).multiply(Matrix4f.rotateX(rot)
			this.setRotation(getRotation().add(0.5f,0.0f,0.0f));
		}

		if(Input.isKeyDown(GLFW_KEY_T)){
			//Matrix4f.translate(position).multiply(Matrix4f.rotateX(rot)
			this.setRotation(getRotation().add(-0.5f,0.0f,0.0f));		
			}

	}
}
