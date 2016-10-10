package embla3d.engine.light;

import org.joml.Vector3f;

import embla3d.engine.World;

public class LightManagement {
	
    public static DirectionalLight setSunLight(World world,float r,float g,float b,
					       float intensity,float x,float y,float z){
	DirectionalLight sun=new DirectionalLight(new Vector3f(r,g,b), intensity, new Vector3f(x,y,z));
	world.getSceneLight().setSun(sun);
	return sun;
    }

    public static DirectionalLight removeSunLight(World world) {
	world.getSceneLight().removeSun();
	return world.getSceneLight().getSun();
    }

    public static void setSunDirection(DirectionalLight sun, float x,float y,float z){
	sun.setDirection(new Vector3f(x,y,z));
    }

    public static void setAmbientLight(World world,float r,float  g ,float b,float intensity){
	AmbientLight ambiant=new AmbientLight(new Vector3f(r,g,b),intensity);
	world.getSceneLight().setAmbient(ambiant);
	//return ambiant;
    }

    public static SpotLight addSpotLight(World world,float r,float g,float b,float x,float y,float z,float intensity
					 ,float constantA,float linearAtt,float quadraticAtt,float xcone,float ycone,float zcone,float cutoffAngle,int pos){
	SpotLight sl=new SpotLight(new Vector3f(r,g,b),new Vector3f(x,y,z)
				   ,intensity,constantA,linearAtt,quadraticAtt,new Vector3f(xcone,ycone,zcone),cutoffAngle);
	world.getSceneLight().setSpotLight(sl, pos);
	return sl;
    }

    public static PointLight addPointLight(World world,float r,float g,float b,float x,float y,float z,float intensity
					   ,float constantA,float linearAtt,float quadraticAtt,int pos){
	PointLight pl=new PointLight(new Vector3f(r,g,b),new Vector3f(x,y,z),intensity,constantA,linearAtt,quadraticAtt);
	world.getSceneLight().setPointLight(pl, pos);
	return pl;
    }
}
