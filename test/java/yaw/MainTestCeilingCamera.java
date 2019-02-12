package yaw;

import yaw.engine.World;
import yaw.engine.items.Item;
import yaw.engine.meshs.MeshBuilder;
import yaw.engine.meshs.Texture;
import yaw.engine.camera.Camera;

public class MainTestCeilingCamera {
    public static void main(String[] args) throws Exception {

        World world = new World(0, 0, 700, 700);/* Create the world with its dimensions. */

        float[] f = new float[]{0.f, 0.f, 0.f};
        (new Thread(world)).start();/* Launches the thread responsible for the display and our game loop. */

        for (int i = 0; i < 10; i++) {
            Item item = world.createItem(i + "", f, 1, MeshBuilder.generateBlock(1, 1, 1));
          //  item.translate((int) (Math.random() * 10)-5, (int) (Math.random() * 10)-5, (int) (Math.random() * 10)-5);
            item.translate(i,i,i);
            //item.rotate((int) (Math.random() * 90), (int) (Math.random() * 90), (int) (Math.random() * 90));

            if (i % 3 == 0)
                item.getAppearance().getMaterial().setTexture(new Texture("/ressources/grassblock.png"));
            else if (i % 3 == 1)
                item.getAppearance().getMaterial().setTexture(new Texture("/ressources/sand.png"));
            else
                item.getAppearance().getMaterial().setTexture(new Texture("/ressources/diamond.png"));
        }
        world.getCamera().translate(0.f, 30, 0);
        world.getCamera().rotate(-90,0,0);

        float z = -10;
        boolean sensinverse = false;

        while (true) {
        /*    if(sensinverse){
                z+=1;
                world.getCamera().translate(0,0,1);
                if(z>=10){
                    sensinverse = false;
                }
            }else{
                world.getCamera().translate(0,0,-1);
                z-=1;
                if(z<=-10){
                    sensinverse = true;
                }
            }
*/
            world.getCamera().rotate(0, 0.06f, 0); /* Allows to advance the edge in the scene 3D. */
            Thread.sleep(10); /* Allows to see the block (cube) move at constant rate. */
        }
    }

}
