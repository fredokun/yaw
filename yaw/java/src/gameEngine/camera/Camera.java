package gameEngine.camera;


import static org.lwjgl.glfw.GLFW.*;
import gameEngine.Input;
import gameEngine.Window;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class Camera {

	private Matrix4f perspectiveMat;
	public Vector3f position;

	//angle of the field of view
	private float fieldOfView;
	//Scope of vision min and max.
	private  float zNear = 0.01f;
	private  float zFar = 1000.f;
	
	float rot = 0.00f;
	/* TODO
	private float pitch;
	private float yaw;
	private float roll;
*/
	public Camera(float fieldOfView,float zNear,float zFar){
		this.perspectiveMat = new Matrix4f().perspective(fieldOfView, (float) Window.aspectRatio(), zNear, zFar);
		this.fieldOfView = fieldOfView;
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = new Vector3f(0f,0f,0f);
	}
	
	public Camera(float zNear,float zFar){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f), 
				(float) Window.aspectRatio(), zNear, zFar);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = new Vector3f(0f,0f,0f);

	}
	
	public Camera(){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
				(float) Window.aspectRatio(), 0.01f, 1000.f);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= 0.01f;
		this.zNear = 1000.f;
		this.position = new Vector3f(0f,0f,0f);
	}
	
	public Camera(float fieldOfView,float zNear,float zFar, Vector3f position){
		this.perspectiveMat = new Matrix4f().perspective(fieldOfView, 
			(float) Window.aspectRatio(), zNear, zFar).translate(position);
		this.fieldOfView = fieldOfView;
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = position;
	}
	
	public Camera(float fieldOfView,float zNear,float zFar, float px, float py, float pz){
		this.position = new Vector3f(px, py, pz);
		this.perspectiveMat = new Matrix4f().perspective(fieldOfView, 
			(float) Window.aspectRatio(), zNear, zFar).translate(this.position);
		this.fieldOfView = fieldOfView;
		this.zFar= zFar;
		this.zNear = zNear;
	}
	
	public Camera(float zNear,float zFar, Vector3f position){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f), 
				(float) Window.aspectRatio(), zNear, zFar).translate(position);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= zFar;
		this.zNear = zNear;
		this.position = position;
	}
	
	public Camera(Vector3f position){
		this.perspectiveMat = new Matrix4f().perspective((float) Math.toRadians(60.0f),
				(float) Window.aspectRatio(), 0.01f, 1000.f).translate(position);
		this.fieldOfView = (float) Math.toRadians(60.0f);
		this.zFar= 0.01f;
		this.zNear = 1000.f;
		this.position = position;
}
	
	public Matrix4f getCameraMat() {
		return perspectiveMat;
	}

	public void setCameraMat(Matrix4f cameraMat) {
		this.perspectiveMat = cameraMat;
	}

	public void setPosition(Vector3f pos){
		this.position = pos;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRot() {
		return rot;
	}

//	public float getPitch() {
//		return pitch;
//	}
//
//	public float getYaw() {
//		return yaw;
//	}
//
//	public float getRoll() {
//		return roll;
//	}

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
		viewMatrix.translate(negativeCameraPos);
		return viewMatrix;

	}

	public void updateCameraMat(){
		perspectiveMat=new Matrix4f().perspective(fieldOfView,
				(float) Window.aspectRatio(), zNear, zFar);
		perspectiveMat = perspectiveMat.mul(setupViewMatrix());
	}
	
	public void rotateCamera(float angle,Vector3f v){
		perspectiveMat.rotate(angle, v);
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

