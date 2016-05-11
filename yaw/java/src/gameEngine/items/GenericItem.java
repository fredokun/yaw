package gameEngine.items;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import org.joml.Vector3f;
import gameEngine.Input;
import gameEngine.Material;
import gameEngine.Mesh;

public class GenericItem extends MyItem{

	public GenericItem(Mesh apparence, float scale, Vector3f rotation, Vector3f position) {
		super(apparence, scale, rotation, position);
	}
	public void setColor(float r,float g,float b){
		this.getAppearance().setMaterial(new Material( new Vector3f(r,g,b),0.f));
	}
	public void translate(float x,float y,float z){
		this.setTranslation(getTranslation().add(x, y, z)); 
	}
	
	public void setPosition(float x,float y,float z){
		this.setTranslation(new Vector3f(x,y,z));
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
