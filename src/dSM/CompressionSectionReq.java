package dSM;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import materials.*;
import section.*;

/*
 * THIS CLASS CONTAINS THE METHODS TO CHECK IF THE GIVEN SECTION FALLS WITHIN THE LIMITS
 * PRESCRIBED IN TABLE 7.1.1 OF SANS:10162-2:2011
 */
public class CompressionSectionReq {
	
	private String secName, matName;
	private SectionDatabase sd = Parameters.getSectionDatabase();
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	
	public CompressionSectionReq(String secName, String matName){
		this.secName =secName;
		this.matName = matName;
	}
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A LIPPED CHANNEL WITH 90 DEGREE ANGLES
	 */	
	public boolean lippedChannelS(){
		double b = sd.getLCSection(secName).getWidth();
		double h = sd.getLCSection(secName).getHeight();
		double L = sd.getLCSection(secName).getLipLength();
		double t = sd.getLCSection(secName).getBaseThickness();
		double r = sd.getLCSection(secName).getangleRadius();
		double fy = md.getMatProp(matName).getFy(t);
		double E = md.getMatProp(matName).getYoung();
		
		boolean limit = true;
		
		
		if(h/t < 472.){
			
		}else{
			limit = false;
		}
		
		
		if(b/t < 159.){
			
		}else{
			limit = false;
		}
		
		if(4<L/t && L/t < 33){
			
		}else{
			limit = false;
		}
		
		if(0.7<h/b && h/b<5.0){
			
		}else{
			limit = false;
		}
		
		if(0.05 <L/b && L/b < 0.41){
			
		}else{
			limit = false;
		}
		
		if(E/fy > 340.){
			
		}else{
			limit = false;
		}
		
		if(r/t < 10){
			
		}else{
			limit = false;
		}
		
		return limit;		
	}
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A LIPPED CHANNEL WITH
	 * ONE OR TWO INTERMEDIATE STIFFENERS
	 */
	public boolean lippedChannelIntStiffener(){
		boolean limit = true;
		//limits still needs to be programmed
		return limit;
	}
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR A LIPPED Z SECTION 
	 * 
	 */
	public boolean lippedZsection(){
		double b = sd.getZLSection(secName).getWidth();
		double h = sd.getZLSection(secName).getHeight();
		double L = sd.getZLSection(secName).getLipLength();
		double t = sd.getZLSection(secName).getBaseThickness();
		double r = sd.getZLSection(secName).getangleRadius();
		double fy = md.getMatProp(matName).getFy(t);
		double E = md.getMatProp(matName).getYoung();
		
		boolean limit = true;
		
		
		if(h/t < 137.){
			
		}else{
			limit = false;
		}
		
		
		if(b/t < 56.){
			
		}else{
			limit = false;
		}
		
		if(0<L/t && L/t < 36.){
			
		}else{
			limit = false;
		}
		
		if(1.5<h/b && h/b<2.7){
			
		}else{
			limit = false;
		}
		
		if(0.0 <L/b && L/b < 0.73){
			
		}else{
			limit = false;
		}
		
		if(E/fy > 590.){
			
		}else{
			limit = false;
		}
		
		if(r/t < 10){
			
		}else{
			limit = false;
		}
		
		
		return limit;		
	}
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR RACK UPRIGHT sections
	 */
	public boolean rackUpright(){
		boolean limit = true;
		//limits still needs to be programmed
		return limit;
	}
	
	/*
	 * THE FOLLOWING METHOD RETURNS TRUE OR FALSE FOR HAT SECTIONS
	 */
	public boolean hatSection(){		
		boolean limit = true;
		//limits still needs to be programmed
		return limit;
	}
	
	
	
	

}
