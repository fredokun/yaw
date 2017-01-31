package embla3d.engine.meshs;

import org.joml.Vector3f;

// More complex material classes:
// ColorMapping, TextMapping, ProceduralMapping (material generator with reuse of images)

public class Material {
    //RGB vector
    public Vector3f color;
    // Reflectance should be between 0 and 1
    public float reflectance;

    /**
     * Construct a material with the specified color and the specified reflectance value
     *
     * @param color       basic RGB vector
     * @param reflectance reflectance reflectance should be between 0 and 1
     */
    public Material(Vector3f color, float reflectance) {
        this.color = color;
        this.reflectance = reflectance;
    }

    /**
     * Construct a material with the specified color and an initial reflectance value of 1000f
     *
     * @param color basic RGB vector
     */
    public Material(Vector3f color) {
        this.color = color;
        this.reflectance = 1000f;
    }

    /**
     * Construct a material with the specified reflectance value and white
     *
     * @param reflectance reflectance reflectance should be between 0 and 1
     */
    public Material(float reflectance) {
        this.color = new Vector3f(1.f, 1.f, 1.f);
        this.reflectance = reflectance;
    }

    /**
     * Construct a material with white color and an initial reflectance value of 1000f
     */
    public Material() {
        this.color = new Vector3f(1.f, 1.f, 1.f);
        this.reflectance = 1000;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getReflectance() {
        return reflectance;
    }
}
