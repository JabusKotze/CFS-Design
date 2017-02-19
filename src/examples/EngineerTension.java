package examples;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import capacityDesign.*;
/*
 * CONTAINS THE MAIN METHOD FOR DETERMINING THE TENSION CAPACITY
 */

public class EngineerTension {

	public static void main(String[] args) {
		
		//SUPPLY THESE PARAMETERS
		String secName = "CL 89x41x10x0.8";		//Section Name: choose from database
		String matName = "550Mpa";				//material Name: chooese fro database
		int numFasteners = 3;					//number of fasteners in single transverse row
		double kt = 0.85;						// kt value from clause 3.2.3.2 or Table 3.2 of SANS 10162-2:2011
		double appliedForce = 1.0;				//The applied force on the Section obtained from an analysis
		double holeDiameter = 2.5;				//the diameter of the punched holes
		
		//DESIGN FOR TENSION	
		Tension tension = new Tension(secName,matName,kt,numFasteners,holeDiameter,appliedForce);   		
		
		//PRINT RESULTS
		tension.printResultsTension();																
	}

}
