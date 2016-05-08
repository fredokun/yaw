package gameEngine;

import basicMeshes.CubeItem;

public class MainWorld {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		World world=new World();
		world.init();
		(new Thread(world)).start();
		CubeItem c1 =world.CreateCube(0,1,0);
		c1.rotate(1, 1, 1);
		c1.translate(0,0,-5);
		world.CreateCube(1,0,1);
	}

}
