package gameEngine.items;

import gameEngine.meshs.Material;

import java.util.ArrayList;

import org.joml.Vector3f;

public class ItemGroup {
	private ArrayList<MyItem> items;
	public Vector3f center;
	private int weight;
	
	public ItemGroup(){
		items = new ArrayList<MyItem>();
		center = new Vector3f();
		weight =0;
	}
	
	public ItemGroup(ArrayList<MyItem> objs){
		items = objs;
		weight =0;
		double x=0,y=0,z=0;
		for(MyItem i : objs){
			int w = i.getAppearance().getWeight();
			x+= i.getPosition().x*w;
			y+= i.getPosition().y*w;
			z+= i.getPosition().z*w;
			weight += w;
		}
		x/= weight;
		y/= weight;
		z/= weight;
		center = new Vector3f((float)x,(float)y,(float)z);
	}
	
	public void updateCenter(){
		double x=0,y=0,z=0;
		for(MyItem i : items){
			int w = i.getAppearance().getWeight();
			x+= i.getPosition().x*w;
			y+= i.getPosition().y*w;
			z+= i.getPosition().z*w;
		}
		x/= weight;
		y/= weight;
		z/= weight;
		center = new Vector3f((float)x,(float)y,(float)z);
	}
	
	public void add(MyItem i){
		int nWeight = weight + i.getAppearance().getWeight();
		double ratioGr = weight / (double)nWeight, ratioI = i.getAppearance().getWeight()/(double)nWeight;
		weight = nWeight;
		Vector3f newCenter = new Vector3f((float)((center.x*ratioGr) + (i.getPosition().x*ratioI)), (float)((center.y*ratioGr) + (i.getPosition().y*ratioI)),(float)((center.z*ratioGr) + (i.getPosition().z*ratioI)));
		center = newCenter;
		items.add(i);
		i.addToGroup(this);
	}
	
	public void remove(MyItem i){
		items.remove(i);
		i.removeFromGroup(this);
		this.weight-= i.getAppearance().getWeight();
		updateCenter();
	}
	
	public void translate(Vector3f translation){
		for(MyItem i : items)
			i.getPosition().add(translation);
	}
	
	public void translate(float x,float y,float z){
		Vector3f translation=new Vector3f(x,y,z);
		for(MyItem i : items)
			i.getPosition().add(translation);
	}
	
	public void rotate(Vector3f rotation){
		for(MyItem i : items){
			i.getRotation().add(rotation);
			i.revolveAround(center, rotation.x, rotation.y, rotation.z);
		}
	}
	
	public void rotate(float x,float y,float z){
		Vector3f rotation=new Vector3f(x,y,z);
		for(MyItem i : items){
			i.getRotation().add(rotation);
			i.revolveAround(center, rotation.x, rotation.y, rotation.z);
		}
	}
	
	public void setPosition(float x, float y, float z){
		setPosition(new Vector3f(x,y,z));
	}
	
	public void setPosition(Vector3f pos){
		float x =  pos.x -center.x, y = pos.y-center.y, z = pos.z - center.z;
		for(MyItem i : items){
			i.translate(x, y, z, this);
		}
		center = pos;
	}
	
	public void separate(float dist){
		for(MyItem i : items)
			i.repelBy(center, dist);
	}
	public void setColor(float r,float g,float b){
		for(MyItem i : items)
			i.getAppearance().setMaterial(new Material( new Vector3f(r,g,b),0.f));
	}
	public void setColor(Vector3f color){
		for(MyItem i : items)
			i.getAppearance().setMaterial(new Material(color,0.f));
	}
	public void multScale(float val){
		for(MyItem i : items)
			i.setScale(i.getScale()*val);
	}
	public ArrayList<MyItem> getItems(){
		return items;
	}
}
