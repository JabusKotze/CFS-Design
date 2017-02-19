package section;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.util.*;
public class SectionDatabase {
	
	private Map<String,LippedCSection> lcSection;
	private Map<String, LippedZSection> zlSection;
	private Map<String, GeneralSection> genSection;
	
	public SectionDatabase(){	
		lcSection = new HashMap<String,LippedCSection>();
		zlSection = new HashMap<String, LippedZSection>();
		genSection = new HashMap<String, GeneralSection>();
		
		lcSection.put("CL 75x50x20x2.0",new LippedCSection("CL 75x50x20x2.0" ,75.0 ,50.0 ,2.0 ,20.0 ,3.14 ,0.4 ,20.8 ,0.36 ,9.59 ,30.0 ,0.15 ,5.12 ,0.0 ,19.3 ,0.534,0.0));
		lcSection.put("CL 175x50x20x2.0", new LippedCSection("CL 175x50x20x2.0",175.0 ,50.0 ,2.0 ,20.0,4.71 ,0.6 ,14.2 ,2.65 ,30.3 ,66.4 ,0.202 ,5.64 ,0.0 ,18.3 ,0.8,0.0));
		lcSection.put("CL 175x50x20x2.0 new", new LippedCSection("CL 175x50x20x2.0 new",175.0 ,50.0 ,2.0 ,20.0,4.71 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0,0.0));
		lcSection.put("CL 89x41x10x0.8",new LippedCSection("CL 89x41x10x0.8", 89.0,41.0,0.8,10.0,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0,0.0,0.0));
		
		zlSection.put("ZL 100x50x20x2.0", new LippedZSection("ZL 100x50x20x2.0",100.0,50.0,2.0,20.0,3.53,0.45,0.705,14.1,39.6,0.307,6.26,26.1,14.9,0.6));
		zlSection.put("ZL 89x41x10x0.8", new LippedZSection("CL 89x41x10x0.8", 89.0,41.0,0.8,10.0,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0,0.0,0.0,0.0));
		genSection.put("GS Num1", new GeneralSection("GS Num1",100.0,2.0,12.3,10.0));		
	}
	

	/*
	 * LippedCSection(name,height,width,coatedThickness,lipLength,mass,area,centroidY,iXX,sectionModulusX,polarRX,iYY,sectionModulusYLipTop,sectionModulusYWebTop,polarRY,J)
	 * The following attributes should be passed on: name,height,width,coatedThickness,lipLength,mass
	 * The rest of the attributes could be given and would be more exact; but if they are not known insert 
	 * a value of 0.0 and it will be calculated.
	 * 
	 * Also Note if the section was not found it will send back the first section in the array
	 * 
	 * NOTE:
	 * Area is given as ex: 0.6 if the area is 600mm2 ; thus for area divide by 1000
	 * Moment of inertia is given as ex: 2.65 if the I=2650000mm4 ; thus divide by (10)6
	 * Section Modulus is given as ex: 30.0 if the Ze = 30000mm3  ; thus divide by 1000
	 * Torsion constant (J) is given as ex 0.8 if the J = 800mm4  ; thus divide by 1000
	 */
	
	public void addLCSection(String secname, LippedCSection lc){
		lcSection.put(secname, lc);	
	}
	
	public Map<String,LippedCSection> getLCsections(){
		return lcSection;
	}
	
	public LippedCSection getLCSection(String name){	
		return lcSection.get(name);		
	}
	
	/*
	 * LippedZSection(name,height,width,coatedThickness,lipLength,radius,mass,area,iXX,sectionModulusX,polarRX,iYY,sectionModulusY,polarRY,J)
	 * The following attributes should be passed on: name,height,width,coatedThickness,lipLength,radius ,mass
	 * The rest of the attributes could be given and would be more exact; but if they are not known insert 
	 * a value of 0.0 and it will be calculated. 
	 * 
	 * Also Note if the section was not found it will send back the first section in the array
	 * 
	 * NOTE:
	 * Area is given as ex: 0.6 if the area is 600mm2 ; thus for area divide by 1000
	 * Moment of inertia is given as ex: 2.65 if the I=2650000mm4 ; thus divide by (10)6
	 * Section Modulus is given as ex: 30.0 if the Ze = 30000mm3  ; thus divide by 1000
	 * Torsion constant (J) is given as ex 0.8 if the J = 800mm4  ; thus divide by 1000
	 */
	public void addZLSection(String secname, LippedZSection lc){
		zlSection.put(secname, lc);	
	}
	
	public Map<String,LippedZSection> getZLsections(){
		return zlSection;
	}
	public LippedZSection getZLSection(String name){
		return zlSection.get(name);
	}
	
	/*
	 * GeneralSection(String name, double area, double coatedThickness, double I, double y)
	 * 
	 * 
	 * 
	 */
	public void addGeneralSection(String secname, GeneralSection lc){
		genSection.put(secname, lc);	
	}
	
	public Map<String,GeneralSection> getGeneralsections(){
		return genSection;
	}
	public GeneralSection getGSSection(String name){
		return genSection.get(name);
	}
}
