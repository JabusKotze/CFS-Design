package materials;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.util.*;

public class MaterialDatabase {
	//private MaterialProperties[] matProp = new MaterialProperties[1];
	private Map<String,MaterialProperties> matProp;
	

	
	public MaterialDatabase(){
		matProp = new HashMap<String,MaterialProperties>();
		matProp.put("550Mpa", new MaterialProperties("550Mpa",550.0,550.0,200000.0,0.3,80000));
	}	
	
	public void addMaterial(String matName, MaterialProperties mp){
		matProp.put(matName, mp);	
	}
	
	public Map<String,MaterialProperties> getMaterials(){
		return matProp;
	}
	
	public MaterialProperties getMatProp(String matName){		
		return matProp.get(matName);
	}

}
