package gameEngine;

import gameEngine.items.MyItem;
import gameEngine.meshs.Material;
import gameEngine.meshs.Mesh;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SceneVertex {
	public static boolean itemAdded=false;
	protected HashMap<Mesh, ArrayList<MyItem>> meshMap;
	protected ArrayList<Mesh> notInit;

	public SceneVertex(){
		meshMap = new HashMap<Mesh, ArrayList<MyItem>>();
		notInit = new ArrayList<Mesh>();
	}

	public void removeItem(MyItem item){
		ArrayList<MyItem> temp=meshMap.get(item.getAppearance());
		temp.remove(item);
	}

	public ArrayList<MyItem> getItemsList(){
		ArrayList<MyItem> items=new ArrayList<MyItem>();
		for(Mesh m:meshMap.keySet())
			items.addAll(meshMap.get(m));
		return items;
	}
	
	public HashMap<Mesh, ArrayList<MyItem>> getMeshMap() {
		return meshMap;
	}

	public void cleanUp() {
		for(Mesh m : meshMap.keySet()){
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
		ArrayList<Mesh> rmListe = new ArrayList<Mesh>();
		for(Mesh m: meshMap.keySet()){
			ArrayList<MyItem> items = meshMap.get(m);
			if(items.isEmpty()){
				rmListe.add(m);
			}else{
				m.draw(items,sh, viewMatrix);
			}
		}
		for(Mesh m : rmListe){
			m.cleanUp();
			meshMap.remove(m);
		}
	}

	public void update() {
		for(Mesh m: meshMap.keySet()){
			for(MyItem i : meshMap.get(m))
				i.update();
		}
	}


	public void add(MyItem item){
		synchronized(this){
			itemAdded=true;
			if(meshMap.keySet().contains(item.getAppearance()))
				meshMap.get(item.getAppearance()).add(item);
			else{
				ArrayList<MyItem> l = new ArrayList<MyItem>();
				l.add(item);
				meshMap.put(item.getAppearance(),l);
				notInit.add(item.getAppearance());
			}
		}
	}

	public void add(Mesh m){
		MyItem item = new MyItem(m);
		this.add(item);
	}

	public void add(float[] vertices, Material material , float[] normals, int[] indices){
		Mesh newMesh = new Mesh(vertices,material, normals,indices);
		MyItem item = new MyItem(newMesh);
		this.add(item);
	}

	public void add(float[] vertices, Material material, float[] normals, int[] indices,
			float scale, Vector3f rotation, Vector3f position) {
		Mesh newMesh = new Mesh(vertices,material,normals,indices);
		MyItem newItem = new MyItem(newMesh, scale, rotation, position);
		this.add(newItem);
	}

	public void add(float[] vertices, Material material , float[] normals, int[] indices, int weight){
		Mesh newMesh = new Mesh(vertices,material, normals,indices, weight);
		MyItem item = new MyItem(newMesh);
		this.add(item);
	}

	public void add(float[] vertices, Material material, float[] normals, int[] indices, int weight,
			float scale, Vector3f rotation, Vector3f position) {
		Mesh newMesh = new Mesh(vertices,material,normals,indices,weight);
		MyItem newItem = new MyItem(newMesh, scale, rotation, position);
		this.add(newItem);
	}
	
	public void add(Mesh apparence, float scale, Vector3f rotation,
			Vector3f position) {
		MyItem newItem = new MyItem(apparence, scale, rotation, position);
		this.add(newItem);
	}
	
	public void clone(MyItem i){
		this.add(new MyItem(i));
	}
}
