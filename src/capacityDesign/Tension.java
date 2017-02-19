package capacityDesign;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import section.*;
import materials.*;
import constants.*;
import java.text.*;
/*
 * TENSION CAPACITY DESIGN ACCORDING CLAUSE 3.2 OF SANS 10162-2:2011
 */

public class Tension {
	private DecimalFormat df = new DecimalFormat("#.###");
	private String secName,matName,digit2;	
	private int numFasteners;				//these are the amount of fasteners located in a single row(transverse spacing)
	private double kt, holeDiameter,appliedForce;
	private MaterialDatabase md = Parameters.getMaterialDatabase();
	private SectionDatabase sd  = Parameters.getSectionDatabase();
	private CapacityReductionFactors cRF = new CapacityReductionFactors();
	
	public Tension(String secName, String matName, double kt, int numFasteners, double holeDiameter, double appliedForce){
		this.secName = secName;
		this.matName = matName;
		this.kt =kt;			//Kt should be obtained from clause 3.2.3.2 or table 3.2 of SANS 10162-2:2011
		this.numFasteners = numFasteners;		//these are the amount of fasteners located in a single row(transverse spacing)
		this.holeDiameter = holeDiameter;
		this.appliedForce = appliedForce;
		
		digit2 = this.secName.substring(0,2);
	}
	
	public boolean willItFail(){
		boolean fail;
		
		if(ultimateSectionCapacity()/1000.<=Math.abs(appliedForce)){
			fail = true;
		}else{
			fail = false;
		}
		return fail;
	}
	
	public double ultimateSectionCapacity(){
		double N;
		N = cRF.getTension()*nominalSectionCapacity();
		return N;
	}
	
	public double nominalSectionCapacity(){
		double Nt;
		Nt = Math.min(grossNominalSectionCapacity(), netNiminalSectionCapacity());
		return Nt;
	}
	
	public double grossNominalSectionCapacity(){ //eqn 3.2.2(1)
		double Nt;
		Nt = getAg()*getFy();
		return Nt;
	}
	
	public double netNiminalSectionCapacity(){	//eqn 3.2.2(2)
		double Nt;
		Nt = 0.85*kt*getAn()*getFu();
		return Nt;
	}
	
	public double getAg(){		//get gross area of section
		double Ag;
		
		if(digit2.equals("CL")){
			Ag = sd.getLCSection(secName).getArea();			
		}else if(digit2.equals("ZL")){
			Ag = sd.getZLSection(secName).getArea();
		}else{
			Ag = sd.getGSSection(secName).getArea();
		}
		
		return Ag;		
	}
	
	public double getAn(){		//returns the net area of section after deduction of punched holes
		double An;
		double Ag = getAg();
		
		An = Ag - numFasteners*holeDiameter*getThickness();
		return An;
	}
	
	public double getFy(){	//get yield stress of section
		double fy;
		
		if(digit2.equals("CL")){
			fy = md.getMatProp(matName).getFy(getThickness());			
		}else if(digit2.equals("ZL")){
			fy = md.getMatProp(matName).getFy(getThickness());
		}else{
			fy = md.getMatProp(matName).getFy(getThickness());
		}
		
		return fy;	
	}
	
	public double getFu(){		//get ultimate tensile strength of section
		double fu;
		
		if(digit2.equals("CL")){
			fu = md.getMatProp(matName).getFu(getThickness());			
		}else if(digit2.equals("ZL")){
			fu = md.getMatProp(matName).getFu(getThickness());
		}else{
			fu = md.getMatProp(matName).getFu(getThickness());
		}
		
		return fu;
	}
	
	public double getThickness(){
		double t;
		
		if(digit2.equals("CL")){
			t = sd.getLCSection(secName).getBaseThickness();			
		}else if(digit2.equals("ZL")){
			t = sd.getZLSection(secName).getBaseThickness();
		}else{
			t = sd.getGSSection(secName).getBaseThickness();
		}
		return t;
	}
	
	public void printResultsTension(){
		String fail;
		if(willItFail()){
			fail =         "Not Ok: The section will fail for applied force of "+appliedForce+" kN";
		}else{
			fail =         "Ok: The section will not fail for applied force of "+appliedForce+" kN";
		}
		
		
		System.out.println("*************************************************************************");
		System.out.println("TENSION DESIGN FOR THE "+secName+":");
		System.out.println("*************************************************************************");
		System.out.println("PARAMETERS USED:");
		System.out.println("Ultimate Tensile Stress:                        Fu = "+df.format(getFu())+" MPa");
		System.out.println("Yield stress:                                   Fy = "+df.format(getFy())+" MPa");
		System.out.println("Gross Area:                                     Ag = "+df.format(getAg())+" mm^2");
		System.out.println("Net Area:                                       An = "+df.format(getAn())+" mm^2");
		System.out.println("*************************************************************************");
		System.out.println("Final Result:");
		System.out.println("The Gross Nominal Section Capacity:            Ntg = "+df.format(grossNominalSectionCapacity()/1000.)+" kN");
		System.out.println("The Netto Nominal Section Capacity:            Ntn = "+df.format(netNiminalSectionCapacity()/1000.)+" kN");
		System.out.println("The Nominal Section Capacity:                   Nt = "+df.format(nominalSectionCapacity()/1000.)+" kN");
		System.out.println("The Ultimate Section Capacity:                   N = "+df.format(ultimateSectionCapacity()/1000.)+" kN");
		System.out.println(fail);
		System.out.println("*************************************************************************");
		
		
	}
	
}
