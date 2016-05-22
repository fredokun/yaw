package gameEngine.light;

import org.joml.Vector3f;

import gameEngine.World;

public class LightManagement {
	
	public static DirectionnalLight setSunLight(World world,float r,float g,float b,
			float intensity,float x,float y,float z){
		DirectionnalLight sun=new DirectionnalLight(new Vector3f(r,g,b), intensity, new Vector3f(x,y,z));
		world.getSceneLight().setSun(sun);
		return sun;
	}
	
	public static void setDirectionSun(DirectionnalLight sun, float x,float y,float z){
		sun.setDirection(new Vector3f(x,y,z));
	}

	public static void setAmbiantLight(World world,float r,float  g ,float b,float intensity){
		AmbiantLight ambiant=new AmbiantLight(new Vector3f(r,g,b),intensity);
		world.getSceneLight().setAmbiant(ambiant);
		//return ambiant;
	}

	public static void addSpotLight(World world,float r,float g,float b,float x,float y,float z,float intensity
			,float constantA,float linearAtt,float quadraticAtt,float xcone,float ycone,float zcone,float cutoffAngle,int pos){
		SpotLight sl=new SpotLight(new Vector3f(r,g,b),new Vector3f(x,y,z)
		,intensity,constantA,linearAtt,quadraticAtt,new Vector3f(xcone,ycone,zcone),cutoffAngle);
		world.getSceneLight().setSpotLight(sl, pos);
		//return sl;
	}

	public static void addPointLight(World world,float r,float g,float b,float x,float y,float z,float intensity
			,float constantA,float linearAtt,float quadraticAtt,int pos){
		PointLight pl=new PointLight(new Vector3f(r,g,b),new Vector3f(x,y,z),intensity,constantA,linearAtt,quadraticAtt);
		world.getSceneLight().setPointLight(pl, pos);
		//return pl;
	}
}