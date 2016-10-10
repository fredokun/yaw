package gameEngine.items;

import gameEngine.World;
import gameEngine.meshGenerator.BlockGenerator;
import gameEngine.meshGenerator.GroundGenerator;
import gameEngine.meshGenerator.HalfBlockGenerator;
import gameEngine.meshGenerator.PyramidGenerator;
import gameEngine.meshGenerator.RegOctahedronGenerator;
import gameEngine.meshGenerator.RegTetrahedronGenerator;
import gameEngine.meshs.Material;
import gameEngine.meshs.Mesh;

import org.joml.Vector3f;


public class ItemManagement {
	public static GenericItem createBlock(World world,float red, float green, float blue,float xLength,float yLength,float zLength,float scale){
		Material material = new Material( new Vector3f(red,green,blue),1000.f);
		Mesh appearance=BlockGenerator.generate(xLength, yLength, zLength, material);
		GenericItem item=new GenericItem(appearance,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createHalfBlock(World world,float red, float green, float blue,float xLength,float yLength,float zLength,float scale){
		Material material = new Material( new Vector3f(red,green,blue),1000.f);
		Mesh appearance=HalfBlockGenerator.generate(xLength, yLength, zLength, material);
		GenericItem item=new GenericItem(appearance,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createPyramid(World world,float red, float green, float blue,float xLength,float yLength,float zLength,float scale){
		Material material = new Material( new Vector3f(red,green,blue),1000.f);
		Mesh appearance=PyramidGenerator.generate(xLength, yLength, zLength, material);
		GenericItem item=new GenericItem(appearance,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createRegTetrahedron(World world,float red, float green, float blue,float scale){
		Material material = new Material( new Vector3f(red,green,blue),1000.f);
		Mesh appearance=RegTetrahedronGenerator.generate(material);
		GenericItem item=new GenericItem(appearance,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createRegOctahedron(World world,float red, float green, float blue,float scale){
		Material material = new Material( new Vector3f(red,green,blue),1000.f);
		Mesh appearance=RegOctahedronGenerator.generate(material);
		GenericItem item=new GenericItem(appearance,scale,new Vector3f(),new Vector3f(0f,0f,-2f));
		world.getSceneVertex().add(item);
		return item;
	}
	
	public static GenericItem createGround(World world,float red, float green,float blue,float width,float length){
		Material material = new Material( new Vector3f(red,green,blue),1000.f);
		Mesh appearance=GroundGenerator.generate(width,length,0,material);
		GenericItem item=new GenericItem(appearance,1.0f,new Vector3f(),new Vector3f(0f,-2f,0f));
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
		for(ItemGroup g : item.getGroups()){
			g.remove(item);
		}
	}
	
	public static ItemGroup createGroup(World world){
		ItemGroup group=new ItemGroup();
		world.getGroupsList().add(group);
		return group;
	}
	
	public static void removeGroup(World world, ItemGroup g){
		world.getGroupsList().remove(g);
		for(MyItem i : g.getItems())
			i.removeFromGroup(g);
	}
	

}
