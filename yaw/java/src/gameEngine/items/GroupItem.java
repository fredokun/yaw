package gameEngine.items;

import gameEngine.meshs.Material;

import java.util.ArrayList;

import org.joml.Vector3f;

public class GroupItem {
	private ArrayList<MyItem> items;
	public Vector3f center;
	private int weight;
	
	public GroupItem(){
		items = new ArrayList<MyItem>();
		center = new Vector3f();
		weight =0;
	}
	
	public GroupItem(ArrayList<MyItem> objs){
		items = objs;
		weight =0;
		double x=0,y=0,z=0;
		for(MyItem i : objs){
			int w = i.getAppearance().getWeight();
			x+= i.getTranslation().x*w;
			y+= i.getTranslation().y*w;
			z+= i.getTranslation().z*w;
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
			x+= i.getTranslation().x*w;
			y+= i.getTranslation().y*w;
			z+= i.getTranslation().z*w;
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
		Vector3f newCenter = new Vector3f((float)((center.x*ratioGr) + (i.getTranslation().x*ratioI)), (float)((center.y*ratioGr) + (i.getTranslation().y*ratioI)),(float)((center.z*ratioGr) + (i.getTranslation().z*ratioI)));
		center = newCenter;
		items.add(i);
	}
	
	public void translate(Vector3f translation){
		for(MyItem i : items)
			i.getTranslation().add(translation);
	}
	
	public void translate(float x,float y,float z){
		Vector3f translation=new Vector3f(x,y,z);
		for(MyItem i : items)
			i.getTranslation().add(translation);
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
}
