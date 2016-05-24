package gameEngine.items;

import gameEngine.World;
import gameEngine.meshGenerator.BlockGenerator;
import gameEngine.meshGenerator.GroundGenerator;
import gameEngine.meshGenerator.HalfBlockGenerator;
import gameEngine.meshGenerator.OctaedreRegulierGenerator;
import gameEngine.meshGenerator.PyramidGenerator;
import gameEngine.meshGenerator.TetraedreRegGenerator;
import gameEngine.meshs.Material;
import gameEngine.meshs.Mesh;

import org.joml.Vector3f;


public class ItemManagement {
	public static GenericItem createBlock(World world,float red, float green, float blue,float xLength,float yLength,float zLength,float scale){
		Material material = new Material( new Vector3f(red,green,blue),0.f);
		Mesh apparence=BlockGenerator.generate(xLength, yLength, zLength, material);
		GenericItem item=new GenericItem(apparence,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createHalfBlock(World world,float red, float green, float blue,float xLength,float yLength,float zLength,float scale){
		Material material = new Material( new Vector3f(red,green,blue),0.f);
		Mesh apparence=HalfBlockGenerator.generate(xLength, yLength, zLength, material);
		GenericItem item=new GenericItem(apparence,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createPyramid(World world,float red, float green, float blue,float xLength,float yLength,float zLength,float scale){
		Material material = new Material( new Vector3f(red,green,blue),0.f);
		Mesh apparence=PyramidGenerator.generate(xLength, yLength, zLength, material);
		GenericItem item=new GenericItem(apparence,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createTetraedreReg(World world,float red, float green, float blue,float scale){
		Material material = new Material( new Vector3f(red,green,blue),0.f);
		Mesh apparence=TetraedreRegGenerator.generate(material);
		GenericItem item=new GenericItem(apparence,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createOctaedreReg(World world,float red, float green, float blue,float scale){
		Material material = new Material( new Vector3f(red,green,blue),0.f);
		Mesh apparence=OctaedreRegulierGenerator.generate(material);
		GenericItem item=new GenericItem(apparence,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createGround(World world,float red, float green,float blue,float width,float length){
		Material material = new Material( new Vector3f(red,green,blue),0.f);
		Mesh apparence=GroundGenerator.generate(width,length,0,material);
		GenericItem item=new GenericItem(apparence,1.0f,new Vector3f(),new Vector3f(0f,-2f,0f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem clone(World world, GenericItem i){
		GenericItem item = new GenericItem(i);
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static void removeItem(World world,GenericItem item){
		world.getSceneVertex().removeItem(item);
		for(GroupItem g : item.getGroupes()){
			g.remove(item);
		}
	}
	
	public static GroupItem createGroup(World world){
		GroupItem group=new GroupItem();
		world.getListGroup().add(group);
		return group;
	}
	
	public static void deleteGroupe(World world, GroupItem g){
		world.getListGroup().remove(g);
		for(MyItem i : g.getItems())
			i.removeGroupe(g);
	}
}
