package embla3d.engine.meshs;

import org.joml.Vector3f;

/**
 * More complex material classes:
 * ColorMapping, TextMapping, ProceduralMapping (material generator with reuse of images)
 */

public class Material {
    private static final float REFLECTANCE_DEFAULT_VALUE = 0f;
    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    //RGB vector
    private Vector3f mColor;
    // Reflectance should be between 0 and 1
    private float mReflectance;
    //texture
    private Texture mTexture;

    /**
     * Construct a material with the specified reflectance value and white
     *
     * @param pReflectance reflectance reflectance should be between 0 and 1
     */
    public Material(float pReflectance) {
        this();
        this.mReflectance = pReflectance;
    }

    /**
     * Construct a material with white color and an initial reflectance value of 1000f
     */
    public Material() {
        this.mColor = DEFAULT_COLOUR;
        this.mReflectance = REFLECTANCE_DEFAULT_VALUE;
        //TODO REMOVE test only
        this.mTexture = new Texture("/ressources/grassblock.png");
    }

    /**
     * Construct a material with the specified texture and an initial reflectance value of 1000f
     *
     * @param pTexture the texture
     */
    public Material(Texture pTexture) {
        this();
        this.mTexture = pTexture;
    }

    /**
     * Construct a material with the specified texture and the specified reflectance
     *
     * @param pTexture     the texture
     * @param pReflectance the reflectance
     */
    public Material(Texture pTexture, float pReflectance) {
        this();
        this.mTexture = pTexture;
        this.mReflectance = pReflectance;
    }

    /**
     * Construct a material with the specified color and the specified reflectance value
     *
     * @param pColor       basic RGB vector
     * @param pReflectance reflectance reflectance should be between 0 and 1
     */
    public Material(Vector3f pColor, float pReflectance) {
        this();
        this.mColor = pColor;
        this.mReflectance = pReflectance;
    }

    /**
     * Construct a material with the specified color and an initial reflectance value of 1000f
     *
     * @param pColor basic RGB vector
     */
    public Material(Vector3f pColor) {
        this();
        this.mColor = pColor;
        this.mReflectance = REFLECTANCE_DEFAULT_VALUE;
    }


    public Texture getTexture() {
        return mTexture;
    }

    public void setTexture(Texture pTexture) {
        mTexture = pTexture;
    }

    public boolean isTextured() {
        return this.mTexture != null;
    }

    public Vector3f getColor() {
        return mColor;
    }

    public float getReflectance() {
        return mReflectance;
    }

    public void setReflectance(float pReflectance) {
        mReflectance = pReflectance;
    }
}