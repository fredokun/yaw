package embla3d.engine.items;

import org.joml.Vector3f;

import embla3d.engine.meshs.Mesh;

public class GenericItem extends MyItem{

    public GenericItem(Mesh appearance, float scale, Vector3f rotation, Vector3f position) {
	    super(appearance, scale, rotation, position);
    }
	
    public GenericItem(GenericItem source){
	super(source);
    }
	
    public GenericItem(Mesh appearance, float scale, float rx, float ry, float rz, float px, float py, float pz) {
	    super(appearance, scale, new Vector3f(rx, ry, rz), new Vector3f(px, py, pz));
    }

    public void update(){

    }
}
