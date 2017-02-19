package gui;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

public class GetFrame {
	private static DSMBENDING dsmb = new DSMBENDING("DSM BENDING");
	private static DSMCOMPRESSION dsmc = new DSMCOMPRESSION("DSM COMPRESSION");
	public GetFrame(){
		
	}
	
	public static DSMBENDING getDSMB(){
		return dsmb;
	}
	public static DSMCOMPRESSION getDSMC(){
		return dsmc;
	}
}
