package gameEngine.light;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import gameEngine.ShaderProgram;

public class SceneLight {
	//Maximum of pointLight and spotLight which can be create.
	//If this value is modify, it's needed to change the value in the frag.sh
	public static final int maxPointlight = 5;
	public static final int maxSpotlight = 5;
	
	//Different light used in the current scene
	private AmbiantLight ambiant;
	private DirectionnalLight sun;
	private PointLight[] pointTable;
	private SpotLight[] spotTable;
	
	public float specularPower = 32;
	
	public SceneLight(){
		ambiant = new AmbiantLight();
		sun = new DirectionnalLight();
		pointTable = new PointLight [maxPointlight];
		for(int i=0; i< maxPointlight;i++)
			pointTable[i] = new PointLight();
		spotTable = new SpotLight [maxSpotlight];
		for(int i=0; i< maxSpotlight;i++)
			spotTable[i] = new SpotLight();
	}

	public void setAmbiant(AmbiantLight ambiant) {
		this.ambiant = ambiant;
	}

	public void setSun(DirectionnalLight sun) {
		this.sun = sun;
	}

	public void setPointTable(PointLight[] pointTable) {
		this.pointTable = pointTable;
	}

	public void setSpotTable(SpotLight[] spotTable) {
		this.spotTable = spotTable;
	}
	
	public void setPointTable(PointLight point, int pos) {
		this.pointTable[pos] = point;
	}

	public void setSpotTable(SpotLight spot, int pos) {
		this.spotTable[pos] = spot;
	}
	
	public void render(ShaderProgram sh,Matrix4f viewMatrix){
        sh.setUniform("ambientLight", ambiant);
        sh.setUniform("specularPower", specularPower);

        // Process Point Lights
        for (int i = 0; i < maxPointlight; i++) {
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
        for (int i = 0; i < maxSpotlight; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotTable[i]);
            Vector4f dir = new Vector4f(currSpotLight.conedir, 0);
            dir.mul(viewMatrix);
            currSpotLight.conedir=new Vector3f(dir.x, dir.y, dir.z);

            Vector3f lightPos = new Vector3f(currSpotLight.getPosition());
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            sh.setUniform("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionnalLight currDirLight = new DirectionnalLight(sun);
        Vector4f dir = new Vector4f(currDirLight.direction, 0);
        dir.mul(viewMatrix);
        currDirLight.direction=new Vector3f(dir.x, dir.y, dir.z);
        sh.setUniform("directionalLight", currDirLight);
    }
	
	public void setPointLight(PointLight pl, int pos){
		pointTable[pos]=pl;
	}
	
	public void setPointLight(SpotLight sl, int pos){
		spotTable[pos]=sl;
	}
}
