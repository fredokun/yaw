package yaw.engine.meshs.strategy;

import yaw.engine.meshs.MeshDrawingStrategy;
import yaw.engine.meshs.Mesh;

import static org.lwjgl.opengl.GL11.*;


public class DefaultDrawingStrategy implements MeshDrawingStrategy {


    @Override
    public void drawMesh(Mesh pMesh) {
        // Draw the mVertices
        glDrawElements(GL_TRIANGLES, pMesh.getIndices().length, GL_UNSIGNED_INT, 0);
    }

}
