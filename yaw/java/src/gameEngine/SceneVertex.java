package gameEngine;

import gameEngine.items.MyItem;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SceneVertex {
	public static boolean itemAdded=false;
	protected HashMap<Mesh, ArrayList<MyItem>> mapMesh;
	protected ArrayList<Mesh> notInit;

	public SceneVertex(){
		mapMesh = new HashMap<Mesh, ArrayList<MyItem>>();
		notInit = new ArrayList<Mesh>();
	}

	public void removeItem(MyItem item){
		ArrayList<MyItem> temp=mapMesh.get(item.getAppearance());
		temp.remove(item);
		if(temp.isEmpty())
			mapMesh.remove(item.getAppearance());
	}

	public ArrayList<MyItem> getListItems(){
		ArrayList<MyItem> items=new ArrayList<MyItem>();
		for(Mesh m:mapMesh.keySet())
			items.addAll(mapMesh.get(m));
		return items;
	}
	
	public HashMap<Mesh, ArrayList<MyItem>> getMapMesh() {
		return mapMesh;
	}

	public void cleanUp() {
		for(Mesh m : mapMesh.keySet()){
			m.cleanUp();
		}
	}

	public void initMesh(){
		for(Mesh m:notInit){
			m.init();
		}
		notInit.clear();
	}
	public void draw(ShaderProgram sh, Matrix4f viewMatrix) {
		for(Mesh m: mapMesh.keySet()){
			m.draw(mapMesh.get(m),sh, viewMatrix);
		}
	}

	public void update() {
		for(Mesh m: mapMesh.keySet()){
			for(MyItem i : mapMesh.get(m))
				i.update();
		}
	}


	public void add(MyItem item){
		synchronized(this){
			itemAdded=true;
			if(mapMesh.keySet().contains(item.getAppearance()))
				mapMesh.get(item.getAppearance()).add(item);
			else{
				ArrayList<MyItem> l = new ArrayList<MyItem>();
				l.add(item);
				mapMesh.put(item.getAppearance(),l);
				notInit.add(item.getAppearance());
			}
		}
	}

	public void add(Mesh m){
		MyItem item = new MyItem(m);
		this.add(item);
	}

	public void add(float[] vertices, Material material , float[] normales, int[] indices){
		Mesh newMesh = new Mesh(vertices,material, normales,indices);
		MyItem item = new MyItem(newMesh);
		this.add(item);
	}

	public void add(float[] vertices, Material material, float[] normales, int[] indices,
			float scale, Vector3f rotation, Vector3f position) {
		Mesh newMesh = new Mesh(vertices,material,normales,indices);
		MyItem newItem = new MyItem(newMesh, scale, rotation, position);
		this.add(newItem);
	}

	public void add(float[] vertices, Material material , float[] normales, int[] indices, int weight){
		Mesh newMesh = new Mesh(vertices,material, normales,indices, weight);
		MyItem item = new MyItem(newMesh);
		this.add(item);
	}

	public void add(float[] vertices, Material material, float[] normales, int[] indices, int weight,
			float scale, Vector3f rotation, Vector3f position) {
		Mesh newMesh = new Mesh(vertices,material,normales,indices,weight);
		MyItem newItem = new MyItem(newMesh, scale, rotation, position);
		this.add(newItem);
	}
	
	public void add(Mesh apparence, float scale, Vector3f rotation,
			Vector3f position) {
		MyItem newItem = new MyItem(apparence, scale, rotation, position);
		this.add(newItem);
	}
}
