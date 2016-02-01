package testArchitecture;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector3f;

public class Scene {

	protected HashMap<Mesh, ArrayList<MyItem>> mapMesh;


	public Scene(){
		mapMesh = new HashMap<Mesh, ArrayList<MyItem>>();
	}

	public void cleanUp() {
		for(Mesh m : mapMesh.keySet()){
			m.cleanUp();
		}
	}

	public void draw(ShaderProgram sh) {
		for(Mesh m: mapMesh.keySet()){
			m.draw(mapMesh.get(m),sh);
		}
	}

	public void add(MyItem item){
		if(mapMesh.keySet().contains(item.getApparence()))
			mapMesh.get(item.getApparence()).add(item);
		else{
			ArrayList<MyItem> l = new ArrayList<MyItem>();
			l.add(item);
			mapMesh.put(item.getApparence(),l);
		}
	}

	public void add(Mesh m){
		MyItem item = new MyItem(m);
		this.add(item);
	}
	
	public void add(float[] vertices, float[] couleur, int[] indices){
		Mesh newMesh = new Mesh(vertices,couleur,indices);
		MyItem item = new MyItem(newMesh);
		this.add(item);
	}
	
	public void add(float[] vertices, float[] couleur, int[] indices,
			float scale, Vector3f rotation, Vector3f position) {
		Mesh newMesh = new Mesh(vertices,couleur,indices);
		MyItem newItem = new MyItem(newMesh, scale, rotation, position);
		this.add(newItem);
	}

	public void add(Mesh apparence, float scale, Vector3f rotation,
			Vector3f position) {
		MyItem newItem = new MyItem(apparence, scale, rotation, position);
		this.add(newItem);
	}
}
