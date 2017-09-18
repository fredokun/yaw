package yaw.engine.meshs.strategy;

import yaw.engine.meshs.Mesh;
import yaw.engine.meshs.MeshDrawingStrategy;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;


public class BoundingBoxDrawingStrategy implements MeshDrawingStrategy {

    @Override
    public void drawMesh(Mesh pMesh) {
        glEnable(GL_POLYGON_OFFSET_FILL);
        glEnable(GL_PROGRAM_POINT_SIZE);
        glPolygonOffset(1, 0);
        glDrawElements(GL_LINES, pMesh.getIndices().length, GL_UNSIGNED_INT, 0);
    }
}
