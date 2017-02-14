package embla3d.engine.light;

import embla3d.engine.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SceneLight {
    // Maximum number of pointLight and spotLight which can be created.
    // If this value is modified, it is needed to change the value in the frag.sh
    public static final int MAX_POINTLIGHT = 5;
    public static final int MAX_SPOTLIGHT = 5;
    public float specularPower = 8;
    // Different lights used in the current scene
    private AmbientLight ambient;
    private DirectionalLight sun;
    private PointLight[] pointTable;
    private SpotLight[] spotTable;

    /**
     * Constructor without parameters, it used to create the maximum of point light and spot light.
     */
    public SceneLight() {
        removeAmbient();
        removeSun();
        pointTable = new PointLight[MAX_POINTLIGHT];
        for (int i = 0; i < MAX_POINTLIGHT; i++)
            pointTable[i] = new PointLight();
        spotTable = new SpotLight[MAX_SPOTLIGHT];
        for (int i = 0; i < MAX_SPOTLIGHT; i++)
            spotTable[i] = new SpotLight();
    }

    public void removeAmbient() {
        this.ambient = new AmbientLight();
    }

    public void removeSun() {
        this.sun = new DirectionalLight();
    }

    /**
     * setters and  method remove for the ambient and directional light
     */

    public void setAmbient(AmbientLight ambient) {
        this.ambient = ambient;
    }

    public DirectionalLight getSun() {
        return this.sun;
    }

    public void setSun(DirectionalLight sun) {
        this.sun = sun;
    }

    public void setPointTable(PointLight point, int pos) {
        this.pointTable[pos] = point;
    }

    public void setSpotTable(SpotLight spot, int pos) {
        this.spotTable[pos] = spot;
    }

    /**
     * Set to the render the different light
     *
     * @param sh         shaderProgram
     * @param viewMatrix viewMatrix
     */

    public void render(ShaderProgram sh, Matrix4f viewMatrix) {
        sh.setUniform("ambientLight", ambient);
        sh.setUniform("specularPower", specularPower);

        // Process Point Lights
        for (int i = 0; i < MAX_POINTLIGHT; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointTable[i]);
            Vector3f lightPos = new Vector3f(currPointLight.getPosition());
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sh.setUniform("pointLights", currPointLight, i);
        }

        // Process Spot Ligths
        for (int i = 0; i < MAX_SPOTLIGHT; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotTable[i]);
            Vector4f dir = new Vector4f(currSpotLight.conedir, 0);
            dir.mul(viewMatrix);
            currSpotLight.conedir = new Vector3f(dir.x, dir.y, dir.z);
            currSpotLight.cutoffAngle = (float) Math.cos(Math.toRadians(currSpotLight.cutoffAngle));
            Vector3f lightPos = new Vector3f(currSpotLight.getPosition());
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            sh.setUniform("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(sun);
        Vector4f dir = new Vector4f(currDirLight.direction, 0);
        dir.mul(viewMatrix);
        currDirLight.direction = new Vector3f(dir.x, dir.y, dir.z);
        sh.setUniform("directionalLight", currDirLight);
    }

    /**
     * getters and setters
     */

    public void setPointLight(PointLight pl, int pos) {
        pointTable[pos] = pl;
    }

    public void setSpotLight(SpotLight sl, int pos) {
        spotTable[pos] = sl;
    }

    public AmbientLight getAmbientLight() {
        return ambient;
    }

    public int getMaxSpotLight() {
        return MAX_POINTLIGHT;
    }

    public PointLight[] getPointTable() {
        return pointTable;
    }

    /**
     * setters
     */

    public void setPointTable(PointLight[] pointTable) {
        this.pointTable = pointTable;
    }

    public SpotLight[] getSpotTable() {
        return spotTable;
    }

    public void setSpotTable(SpotLight[] spotTable) {
        this.spotTable = spotTable;
    }

    public float getSpecularPower() {
        return specularPower;
    }

    public void setSpecularPower(float x) {
        specularPower = x;
    }
}
