package yaw;

import yaw.engine.World;
import yaw.engine.items.ItemObject;
import yaw.engine.light.SpotLight;
import yaw.engine.meshs.MeshBuilder;
import yaw.engine.meshs.Texture;
import yaw.engine.skybox.Skybox;
import org.joml.Vector3f;

/**
 * The Main Class that launches our game engine. this exemple goal was to fix a bug with the lights movements compared to the camera movements
 * in this exemple, the camera is turning around a cube which is lighted by a different spotlight on four faces.
 */
public class LightTest {
    public static void main(String[] args) throws Exception {

        World world = new World(0, 0, 700, 700);/* Create the world with its dimensions. */

        world.launch(); /* Launches the thread responsible for the display and our game loop. */

        ItemObject c1 = world.createItemObject("1", 0f, 0f, 0f, 1
                , MeshBuilder.generateBlock(1, 1, 1));
        float[] tab = {0.0f, 0.0f, 0.0f}; // a cube

        c1.getMesh().getMaterial().setTexture(new Texture("/ressources/diamond.png"));


        world.getCamera().translate(-5, 0, 0);
        world.getCamera().rotate(0,90,0);


        /* Creating Light for Our World
        * Creating 4 different spotlights, one for each face of the cube except the top and the bottom
        * */
        world.getSceneLight().setSpotLight(new SpotLight(0, 255, 255, 0, 0, 5, 1, 0, 0.5f, 0, 0, 0, -1, 3f), 0);
        world.getSceneLight().setSpotLight(new SpotLight(0, 255, 0, -5, 0, 0, 1, 0, 0.5f, 0, 1, 0, 0, 3f), 1);
        world.getSceneLight().setSpotLight(new SpotLight(255, 0, 0, 5, 0, 0, 1, 0, 0.5f, 0, -1, 0, 0, 3f), 2);
        world.getSceneLight().setSpotLight(new SpotLight(255, 0, 255, 0, 0, -5, 1, 0, 0.5f, 0, 0, 0, 1, 3f), 3);

        //world.sc.add(GroundGenerator.generate(400,400,-2,new Material(new Vector3f(1,1,1))));

        /* A skybox will allow us to set a background to give the illusion that our 3D world is bigger. */
        world.setSkybox(new Skybox(500, 500, 500, new Vector3f(0, 0, 0)));





        float angle = 0;
        int nbtours = 0;

        while (nbtours <3) {
            angle =(angle+1)%360;
            if(angle == 359){
                nbtours++;
            }
            //world.getCamera().rotate(0, 5, 0); /* Allows to advance the edge in the scene 3D. */

            /*
             *  Updates the postion of the camera for the rotation
             *  the camera is rotating around the cube
             */
            float x = world.getCamera().getPosition().x;
            float z = world.getCamera().getPosition().z;
            float tmp = (float)Math.toRadians((double)angle);
            world.getCamera().setPosition((float)(5*Math.cos(tmp)),world.getCamera().getPosition().y, (float)(5*Math.sin(tmp)));
            world.getCamera().rotate(0,-1f,0);
            Thread.sleep(25); /* Allows to see the block (cube) move at constant rate. */
        }
    }

}
