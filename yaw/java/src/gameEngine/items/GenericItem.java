package gameEngine.items;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import org.joml.Vector3f;

import gameEngine.Input;
import gameEngine.meshs.Mesh;

public class GenericItem extends MyItem{

	public GenericItem(Mesh apparence, float scale, Vector3f rotation, Vector3f position) {
		super(apparence, scale, rotation, position);
	}
	
	public GenericItem(GenericItem source){
		super(source);
	}
	
	public GenericItem(Mesh apparence, float scale, float rx, float ry, float rz, float px, float py, float pz) {
		super(apparence, scale, new Vector3f(rx, ry, rz), new Vector3f(px, py, pz));
	}

	public void update(){
		if(Input.isKeyDown(GLFW_KEY_X)){
			this.setPosition(getPosition().add(0.0f, 0.0f, -0.1f)); 
		}

		if(Input.isKeyDown(GLFW_KEY_C)){
			this.setPosition(getPosition().add(0.0f, 0.0f, 0.1f)); 
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
