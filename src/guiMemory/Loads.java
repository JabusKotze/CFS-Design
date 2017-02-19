package guiMemory;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

public class Loads {
	private String loadDistr,loadApp,bracing;
	
	public Loads(){
		
	}
	public void setLoadDistr(String loadDist){
		loadDistr = loadDist;
	}
	public void setLoadApp(String loadapp){
		loadApp = loadapp;
	}
	public void setBracing(String brace){
		bracing = brace;
	}
	public String getDistr(){
		return loadDistr;
	}
	public String getLoadApp(){
		return loadApp;
	}
	public String getBracing(){
		return bracing;
	}
}
