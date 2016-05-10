package gameEngine.camera;


import static org.lwjgl.glfw.GLFW.*;
import gameEngine.Input;
import gameEngine.Window;

import org.joml.Matrix4f;
import org.joml.Vector3f;


public class Camera {

	private Matrix4f perspectiveMat;
	public Vector3f position;

	//angle of the field of view
	private float fieldOfView;
	//Scope of vision min and max.
	private  float zNear = 0.01f;
	private  float zFar = 1000.f;
	
	public Vector3f orientation;

	public Camera(float fieldOfView,float zNear,float zFar){
		this.perspectiveMat = new Matrix4f().perspective(fieldOfView, (float) Window.aspectRatio(), zNear, zFar);
		this.fieldOfView = fieldOfView;
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = new Vector3f(0f,0f,0f);
		this.orientation = new Vector3f();
	}
	
	public Camera(float zNear,float zFar){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f), 
				(float) Window.aspectRatio(), zNear, zFar);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = new Vector3f(0f,0f,0f);
		this.orientation = new Vector3f();
	}
	
	public Camera(){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
				(float) Window.aspectRatio(), 0.01f, 1000.f);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= 0.01f;
		this.zNear = 1000.f;
		this.position = new Vector3f(0f,0f,0f);
		this.orientation = new Vector3f();
	}
	
	public Camera(float fieldOfView,float zNear,float zFar, Vector3f position){
		this.perspectiveMat = new Matrix4f().perspective(fieldOfView, 
			(float) Window.aspectRatio(), zNear, zFar).translate(position);
		this.fieldOfView = fieldOfView;
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = position;
		this.orientation = new Vector3f();
	}
	
	public Camera(float fieldOfView,float zNear,float zFar, float px, float py, float pz){
		this.position = new Vector3f(px, py, pz);
		this.perspectiveMat = new Matrix4f().perspective(fieldOfView, 
			(float) Window.aspectRatio(), zNear, zFar).translate(this.position);
		this.fieldOfView = fieldOfView;
		this.zFar= zFar;
		this.zNear = zNear;
		this.orientation = new Vector3f();
	}
	
	public Camera(float zNear,float zFar, Vector3f position){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f), 
				(float) Window.aspectRatio(), zNear, zFar).translate(position);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = position;
		this.orientation = new Vector3f();
	}
	
	public Camera(Vector3f position){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
				(float) Window.aspectRatio(), 0.01f, 1000.f).translate(position);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= 0.01f;
		this.zNear = 1000.f;
		this.position = position;
		this.orientation = new Vector3f();
}
	
	public Matrix4f getCameraMat() {
		return perspectiveMat;
	}

	public void setPosition(Vector3f pos){
		this.position = pos;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setOrientation(Vector3f pos){
		this.orientation = pos;
	}

	public Vector3f getOrientation() {
		return orientation;
	}
	
	public void update(){
		if(Input.isKeyDown(GLFW_KEY_W)){
			//System.out.println("W");
			position.y += 0.05f;
		}
		if(Input.isKeyDown(GLFW_KEY_S)){
			//System.out.println("S");
			position.y -= 0.05f;
		}
		if(Input.isKeyDown(GLFW_KEY_D)){
			//System.out.println("D");
			position.x += 0.05f;
		}
		if(Input.isKeyDown(GLFW_KEY_A)){
			//System.out.println("A");
			position.x -= 0.05f;
		}
	}

	//Generate the Matrix for the camera position
	public Matrix4f setupViewMatrix(){
		Matrix4f viewMatrix = new Matrix4f();
		//TODO pitch yaw roll
		Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
		viewMatrix.translate(negativeCameraPos).rotateX((float) Math.toRadians(-orientation.x)).rotateY((float) Math.toRadians(-orientation.y)).rotateZ((float) Math.toRadians(-orientation.z));
		return viewMatrix;

	}

	public void updateCameraMat(){
		perspectiveMat=new Matrix4f().perspective(fieldOfView,
				(float) Window.aspectRatio(), zNear, zFar);
		perspectiveMat = perspectiveMat.mul(setupViewMatrix());
	}

	public float getzNear() {
		return zNear;
	}

	public float getzFar() {
		return zFar;
	}
	
	public float getFieldOfView() {
		return fieldOfView;
	}
}