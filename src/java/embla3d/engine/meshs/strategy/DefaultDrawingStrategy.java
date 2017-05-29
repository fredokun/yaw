package embla3d.engine.meshs.strategy;

import embla3d.engine.meshs.Mesh;
import embla3d.engine.meshs.MeshDrawingStrategy;

import static org.lwjgl.opengl.GL11.*;


public class DefaultDrawingStrategy implements MeshDrawingStrategy {


    @Override
    public void drawMesh(Mesh pMesh) {
        // Draw the mVertices
        glDrawElements(GL_TRIANGLES, pMesh.getIndices().length, GL_UNSIGNED_INT, 0);
    }

}
