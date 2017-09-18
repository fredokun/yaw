package yaw.engine.light;

import org.joml.Vector3f;

/**
 * The spot light is a type of light models a light source that’s emitted from a point in space, but instead of emitting in all directions is restricted to a cone.
 * <p>
 * This class allows to create the spotlight. So, we need the class PointLight and to add the parameters conedir, cutoffAngle to realize a cone.
 **/

public class SpotLight extends PointLight {
    Vector3f mConedir;
    float mCutoffAngle;

    /**
     * Constructor with the specified color, position, intensity, constantAtt, linearAtt, quadraticAtt, conedir, cutoffAngle
     *
     * @param color        color
     * @param position     position
     * @param intensity    intensity
     * @param constantAtt  constantAtt
     * @param linearAtt    linearAtt
     * @param quadraticAtt quadraticAtt
     * @param conedir      direction conedir
     * @param cutoffAngle  angle
     */
    public SpotLight(Vector3f color, Vector3f position, float intensity,
                     float constantAtt, float linearAtt, float quadraticAtt, Vector3f conedir, float cutoffAngle) {
        super(color, position, intensity, constantAtt, linearAtt, quadraticAtt);
        this.mConedir = conedir;
        this.mCutoffAngle = cutoffAngle;
    }

    /**
     * Constructor with the specified color (cx,cy,cz), position (px,py,pz), intensity, constantAtt, linearAtt, quadraticAtt, cone direction (cdx,cdy,cdz) and angle
     *
     * @param red           Red value
     * @param green           Green value
     * @param blue           Blue value
     * @param px           Position x
     * @param py           Position y
     * @param pz           Position z
     * @param intensity    intensity
     * @param constantAtt  constantAtt
     * @param linearAtt    linearAtt
     * @param quadraticAtt quadraticAtt
     * @param cdx          Direction cone x
     * @param cdy          Direction cone y
     * @param cdz          Direction cone z
     * @param cutoffAngle  angle
     */
    public SpotLight(float red, float green, float blue, float px, float py, float pz, float intensity,
                     float constantAtt, float linearAtt, float quadraticAtt, float cdx, float cdy, float cdz, float cutoffAngle) {
        super(red, green, blue, px, py, pz, intensity, constantAtt, linearAtt, quadraticAtt);
        this.mConedir = new Vector3f(cdx, cdy, cdz);
        this.mCutoffAngle = cutoffAngle;
    }

    /**
     * Constructor without parameters
     */
    public SpotLight() {
        super();
        this.mConedir = new Vector3f(0, 0, -1);
        this.mCutoffAngle = 0;
    }

    /**
     * Constructor with the parameters source
     *
     * @param source source
     */
    public SpotLight(SpotLight source) {
        super(source);
        this.mConedir = source.mConedir;
        this.mCutoffAngle = source.mCutoffAngle;
    }

    /**
     * getters and setters
     */
    public Vector3f getConedir() {
        return mConedir;
    }

    public void setConeDir(float x, float y, float z) {
        this.mConedir = new Vector3f(x, y, z);
    }

    public void setConeDir(Vector3f cd) {
        this.mConedir = cd;
    }

    public float getCutoffAngle() {
        return mCutoffAngle;
    }

    public void setCutoffAngle(float coa) {
        this.mCutoffAngle = coa;
    }
}
