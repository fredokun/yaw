package embla3d.engine.light;

import org.joml.Vector3f;

import embla3d.engine.World;

/**
 * This class allows to create and add all the light in the world.
 */

public class LightManagement {
	/**
     * Create the directional light in the world.
     * The directional light created is like a sun.
	 *
	 * @param world     world
	 * @param r         Red color
	 * @param g         Green color
	 * @param b         Blue color
	 * @param intensity intensity
	 * @param x         Direction x
	 * @param y         Direction y
	 * @param z         Direction z
     *
	 * @return DirectionalLight
	 */
    public static DirectionalLight setSunLight(World world,float r,float g,float b,
					       float intensity,float x,float y,float z){
	DirectionalLight sun=new DirectionalLight(new Vector3f(r,g,b), intensity, new Vector3f(x,y,z));
	world.getSceneLight().setSun(sun);
	return sun;
    }

	/**
     * Remove the directional light in the world.
	 *
	 * @param world world
	 * @return DirectionalLight
	 */
    public static DirectionalLight removeSunLight(World world) {
	world.getSceneLight().removeSun();
	return world.getSceneLight().getSun();
    }

	/**
     * Set the direction
	 *
	 * @param sun sun
	 * @param x   Direction x
	 * @param y   Direction y
	 * @param z   Direction z
	 */
    public static void setSunDirection(DirectionalLight sun, float x,float y,float z){
	sun.setDirection(new Vector3f(x,y,z));
    }

	/**
     * Create the ambient light in the world.
	 *
	 * @param world     world
	 * @param r         Red color
	 * @param g         Green color
	 * @param b         Blue color
	 * @param intensity intensity
	 */
    public static void setAmbientLight(World world,float r,float  g ,float b,float intensity){
	AmbientLight ambiant=new AmbientLight(new Vector3f(r,g,b),intensity);
	world.getSceneLight().setAmbient(ambiant);
	//return ambiant;
    }

	/**
     * Create the spot light in the world.
	 *
	 * @param world         world
	 * @param r             Red color
	 * @param g             Green Color
	 * @param b             Blue color
	 * @param x             Position x
	 * @param y             Position y
	 * @param z             Position y
	 * @param intensity     intensity
	 * @param constantA     constantA
	 * @param linearAtt     linearAtt
	 * @param quadraticAtt  quadraticAtt
	 * @param xcone         xcone
	 * @param ycone         ycone
	 * @param zcone         zcone
	 * @param cutoffAngle   cutoffAngle
	 * @param pos           pos
     *
	 * @return              SpotLight
	 */
    public static SpotLight addSpotLight(World world,float r,float g,float b,float x,float y,float z,float intensity
					 ,float constantA,float linearAtt,float quadraticAtt,float xcone,float ycone,float zcone,float cutoffAngle,int pos){
	SpotLight sl=new SpotLight(new Vector3f(r,g,b),new Vector3f(x,y,z)
				   ,intensity,constantA,linearAtt,quadraticAtt,new Vector3f(xcone,ycone,zcone),cutoffAngle);
	world.getSceneLight().setSpotLight(sl, pos);
	return sl;
    }

	/**
     * Create a point light in the world.
	 *
	 * @param world         world
     * @param r             Red color
     * @param g             Green Color
     * @param b             Blue color
     * @param x             Position x
     * @param y             Position y
     * @param z             Position y
     * @param intensity     intensity
     * @param constantA     constantA
     * @param linearAtt     linearAtt
     * @param quadraticAtt  quadraticAtt
	 * @param pos           pos
     *
	 * @return PointLight
	 */
    public static PointLight addPointLight(World world,float r,float g,float b,float x,float y,float z,float intensity
					   ,float constantA,float linearAtt,float quadraticAtt,int pos){
	PointLight pl=new PointLight(new Vector3f(r,g,b),new Vector3f(x,y,z),intensity,constantA,linearAtt,quadraticAtt);
	world.getSceneLight().setPointLight(pl, pos);
	return pl;
    }
}
