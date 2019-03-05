package yaw.engine.light;

import yaw.engine.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SceneLight {
    // Maximum number of pointLight and spotLight which can be created.
    // If this value is modified, it is needed to change the value in the frag.sh
    public static final int MAX_POINTLIGHT = 5;
    public static final int MAX_SPOTLIGHT = 5;
    public float mSpecularPower = 8;
    // Different lights used in the current scene
    private AmbientLight mAmbient;
    private DirectionalLight mSun;
    private PointLight[] mPointTable;
    private SpotLight[] mSpotTable;

    /**
     * Constructor without parameters, it used to create the maximum of point light and spot light.
     */
    public SceneLight() {
        removeAmbient();
        removeSun();
        this.mPointTable = new PointLight[MAX_POINTLIGHT];
        for (int i = 0; i < MAX_POINTLIGHT; i++)
            this.mPointTable[i] = new PointLight();
        this.mSpotTable = new SpotLight[MAX_SPOTLIGHT];
        for (int i = 0; i < MAX_SPOTLIGHT; i++)
            this.mSpotTable[i] = new SpotLight();
    }

    public void removeAmbient() {
        this.mAmbient = new AmbientLight();
    }

    public void removeSun() {
        this.mSun = new DirectionalLight();
    }

    public void setPointTable(PointLight point, int pos) {
        this.mPointTable[pos] = point;
    }

    public void setSpotTable(SpotLight spot, int pos) {
        this.mSpotTable[pos] = spot;
    }

    /**
     * Set to the render the different light
     *
     * @param sh         shaderProgram
     * @param viewMatrix viewMatrix
     */

    public void render(ShaderProgram sh, Matrix4f viewMatrix) {
        sh.setUniform("ambientLight", mAmbient);
        sh.setUniform("specularPower", mSpecularPower);

        // Process Point Lights
        for (int i = 0; i < MAX_POINTLIGHT; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(mPointTable[i]);
            Vector3f lightPos = new Vector3f(currPointLight.getPosition());
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            //update light position
            currPointLight.setPosition(lightPos);

            sh.setUniform("pointLights", currPointLight, i);
        }

        // Process Spot Light
        for (int i = 0; i < MAX_SPOTLIGHT; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(mSpotTable[i]);
            Vector4f dir = new Vector4f(currSpotLight.mConedir, 0);
            dir.mul(viewMatrix);
            currSpotLight.mConedir = new Vector3f(dir.x, dir.y, dir.z);
            currSpotLight.mCutoffAngle = (float) Math.cos(Math.toRadians(currSpotLight.mCutoffAngle));
            Vector3f lightPos = new Vector3f(currSpotLight.getPosition());
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            //update spot light
            currSpotLight.setPosition(lightPos);

            sh.setUniform("spotLight", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(mSun);
        Vector4f dir = new Vector4f(currDirLight.mDirection, 0);
        dir.mul(viewMatrix);
        currDirLight.mDirection = new Vector3f(dir.x, dir.y, dir.z);
        sh.setUniform("directionalLight", currDirLight);
    }

    /**
     * getters and setters
     */

    public void setPointLight(PointLight pl, int pos) {
        mPointTable[pos] = pl;
    }

    public void setSpotLight(SpotLight sl, int pos) {
        mSpotTable[pos] = sl;
    }

    public DirectionalLight getSun() {
        return this.mSun;
    }

    public void setSun(DirectionalLight sun) {
        this.mSun = sun;
    }

    public AmbientLight getAmbientLight() {
        return mAmbient;
    }

    public int getMaxSpotLight() {
        return MAX_POINTLIGHT;
    }

    public PointLight[] getPointTable() {
        return mPointTable;
    }

    /**
     * setters
     */

    public void setPointTable(PointLight[] pointTable) {
        this.mPointTable = pointTable;
    }

    public SpotLight[] getSpotTable() {
        return mSpotTable;
    }

    public void setSpotTable(SpotLight[] spotTable) {
        this.mSpotTable = spotTable;
    }

    public float getSpecularPower() {
        return mSpecularPower;
    }

    public void setSpecularPower(float x) {
        mSpecularPower = x;
    }

    /**
     * setters and  method remove for the ambient and directional light
     */

    public void setAmbient(AmbientLight ambient) {
        this.mAmbient = ambient;
    }
}
