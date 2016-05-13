package gameEngine.items;

import gameEngine.meshs.Material;
import gameEngine.meshs.Mesh;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MyItem {
	private Mesh appearance;

	private float scale;
	private Vector3f rotation;
	private Vector3f translation;

	public MyItem(Mesh apparence, float scale, Vector3f rotation,
			Vector3f position) {
		super();
		this.appearance=apparence;
		this.scale = scale;
		this.rotation = rotation;
		this.translation = position;
	}

	public MyItem(MyItem source){
		this.appearance = source.appearance;
		this.scale= source.scale;
		this.rotation = new Vector3f(source.rotation);
		this.translation = new Vector3f(source.translation);
	}

	public MyItem(Mesh m) {
		this.appearance=m;
		scale = 1f;
		rotation = new Vector3f();
		translation = new Vector3f();
	}

	public Mesh getAppearance() {
		return appearance;
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

	public Vector3f getPosition(){
		return translation;
	}

	public void setPosition(float x,float y,float z){
		this.translation=new Vector3f(x,y,z);
	}

	public void setPosition(Vector3f pos){
		this.translation=pos;
	}
	public void setColor(float r,float g,float b){
		this.getAppearance().setMaterial(new Material( new Vector3f(r,g,b),0.f));
	}
	public void setColor(Vector3f color){
		this.getAppearance().setMaterial(new Material(color,0.f));
	}
	public void translate(float x,float y,float z){
		this.setTranslation(getTranslation().add(x, y, z)); 
	}
	public void setReflectance(float refl){
		this.getAppearance().getMaterial().reflectance=refl;
	}
	
	public float getReflectance(){
		return this.getAppearance().getMaterial().reflectance;
	}
	public Vector3f getColor(){
		return this.getAppearance().getMaterial().color;
	}
	public void rotate(float x,float y,float z){
		this.setRotation(getRotation().add(x,y,z));	
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

	public MyItem clone(){
		return new MyItem(this);
	}

	public void update(){

	}

}
