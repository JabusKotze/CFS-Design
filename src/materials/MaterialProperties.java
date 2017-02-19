package materials;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */


public class MaterialProperties {
	
	private double yield, tensileStrength;
	private double youngs;
	private double poisson, G;	 
	private String matName;
	
	
	public MaterialProperties(String matName, double fy,double fu, double youngs, double poisson, double G){
		this.matName = matName;
		this.yield = fy;
		this.youngs = youngs;
		this.poisson = poisson;		
		this.G =G;		
		this.tensileStrength = fu;
	}
	
	
	/*
	 * The following method returns the yield stress
	 * According SANS 517:2009 Clause 5.2.2 the yield stress should be reduced
	 * if the nominal thickness is less than 0.9mm for 550MPa strength material
	 * the yield stress would be referred to as 495MPa.
	 * And if the thickness is less than 0.6mm the yield stress should be taken as 410MPa
	 * 
	 * When the material yield stress is not known the yield stress should then be taken as 200MPa.
	 */
	public double getFy(double thickness){
		
		if(matName.equals("550Mpa")){ 
			if(thickness<=0.9 && thickness>0.6){
				return 495.0;  				//returns 495MPa if the thickness is less than 0.9mm and greater than 0.6mm
			}else if(thickness>0.9){
				return yield; 				//returns 550Mpa if the thickness is greater than 0.9mm
			}else if(thickness<=0.6){
				return 410.0; 				//Returns 410MPa if the thickness is less than 0.6mm for 550Mpa material.
			}else{
				return yield;
			}
		}else if(yield == 0.0){
			return 200.0;					//if the yield stress could not be verified it returns a value of 200Mpa
		}else{
			return yield;   				// returns the original yield strength given
		}
		
		
	}
	
	
	
	
	public double getFu(double thickness){
		if(matName.equals("550Mpa")){
			if(thickness<=0.9 && thickness>0.6){
				return 495.0;				//returns 495MPa if the thickness is less than 0.9mm and greater than 0.6mm
			}else if(thickness>0.9){
				return tensileStrength;  	//returns 550Mpa if the thickness is greater than 0.9mm
			}else{
				return 410.0;				//Returns 410MPa if the thickness is less than 0.6mm for 550Mpa material.
			}
		}else if(tensileStrength == 0.0){
			return 365.0;					//if the ultimate tensile stress could not be verified it returns a value of 365Mpa
		}else{
			return tensileStrength;    		// returns the original ultimate tensile strength given
		}
	}
	
	
	
	
	public double getYoung(){
		return youngs;		// Returns the Youngs Modulus of elasticity E
	}
	
	public double getPoisson(){
		return poisson;		//Returns the Poisson ratio
	}
	public double getG(){
		return G;			//Returns the shear Modulus G
	}
	public String getName(){
		return matName;		//returns the material name
	}
	
	public void printMatProp(double thickness){
		System.out.println("\nMATERIAL PROPERTIES FOR THE: "+matName+" IS AS FOLLOW");
		System.out.println("**********************************************************");
		System.out.println("The yield stress:                        fy = "+getFy(thickness)+" MPa");
		System.out.println("The Ultimate Tensile Stress:             fu = "+getFu(thickness)+" MPa");
		System.out.println("The youngs elastic modulus:               E = "+getYoung()+" MPa");
		System.out.println("The poisson ratio:                        v = "+getPoisson()+" mm/mm");
		System.out.println("The Shear Modulus:                        G = "+getG()+" MPa");
		System.out.println("***********************************************************");		
	}

}
