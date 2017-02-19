package section;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import constants.GeneralConstants;
//this class initiates the attributes for a Lipped C section profile
public class LippedCSection {

	private String name;
	private double height, width, coatedThickness, lipLength, mass, area, centroidY, iXX, sectionModulusX, polarRX, iYY, sectionModulusYLipTop, sectionModulusYWebTop, polarRY, j ;
	private GeneralConstants gC = new GeneralConstants();  // Refers to the the general constants database
	private double monoSBy; //monosymmetric section constant about the y
	
	public LippedCSection(String name, double height, double width, double coatedThickness, double lipLength, double mass, double area, double centroidY, double iXX, double sectionModulusX, double polarRX, double iYY, double sectionModulusYLipTop, double sectionModulusYWebTop, double polarRY, double j,double by ){
		this.name =name;
		this.height =height;
		this.width =width;
		this.coatedThickness =coatedThickness;
		this.lipLength = lipLength;
		this.mass = mass;
		this.area = area;
		this.centroidY = centroidY;
		this.iXX = iXX;
		this.sectionModulusX = sectionModulusX;
		this.polarRX = polarRX;
		this.iYY = iYY;
		this.sectionModulusYLipTop = sectionModulusYLipTop;
		this.sectionModulusYWebTop = sectionModulusYWebTop;
		this.polarRY = polarRY;
		this.j = j;
		this.monoSBy = by;
	
	}
	
	public String getName(){
		return name;
	}	
	
	public double getHeight(){
		return height;
	}	
	
	public double getWidth(){
		return width;
	}	
	
	public double getBaseThickness(){
		return coatedThickness - gC.getBTReduction();
	}	
	
	public double getNominalThickness(){
		return coatedThickness;
	}
	
	public double getLipLength(){
		return lipLength;
	}
	
	public double getangleRadius(){		
		return getBaseThickness()*gC.getRadiusThicknessRatio();
	}
	
	public double getMass(){
		return mass;
	}
	
	public double getArea(){
		if(area == 0.0){
			return calcArea();
		}else{
		return area*1000;
		}
	}
	
	public double getCentroidY(){
		if(centroidY == 0.0){
			return calcCentroidX();
		}else{
		return centroidY;
		}
	}
	
	public double getSectionModulusX(){
		if(sectionModulusX == 0.0){
			return calcZex();
		}else{
		return sectionModulusX*1000;
		}
	}
	
	public double getIXX(){
		if(iXX == 0.0){
			return calcIXX();
		}else{
		return iXX*1000000;
		}
	}
	
	public double getPolarRX(){
		if(polarRX == 0.0){
			return calcPolarRx();
		}else{
		return polarRX;
		}
	}
	
	public double getSectionModulusYLipTop(){
		if(sectionModulusYLipTop == 0.0){
			return calcZeyLipsideTop();
		}else{
		return sectionModulusYLipTop*1000;
		}
	}
	
	public double getSectionModulusYWebTop(){
		if(sectionModulusYWebTop == 0.0){
			return calcZeyWebsideTop();
		}else{
		return sectionModulusYWebTop*1000;
		}
	}
	
	public double getIYY(){
		if(iYY == 0.0){
			return calcIYY();
		}else{
		return iYY*1000000;
		}
	}
	
	public double getPolarRY(){
		if(polarRY == 0.0){
			return calcPolarRy();
		}else{
		return polarRY;
		}
	}
	
	public double getJ(){
		if(j == 0.0){
			return calcJ();
		}else{
		return j*1000;
		}
	}
	
	public double getWarping(){ // this formulae is from Figure E1 SANS
		double warp;
		double term1 = Math.pow(width, 2)*getBaseThickness()/6;
		double term2 = 4*Math.pow(lipLength, 3)+6*height*Math.pow(lipLength, 2)+3*Math.pow(height, 2)*lipLength+Math.pow(height, 2)*width;
		double term3 = Math.pow(getShearCentreY(), 2)*getIXX();
		warp = (term1*term2 - term3);
		
		//System.out.println("\n\nThe warping constant W = "+warp+" mm(10 6)");
		return warp;			// returns the warping value of the section
	}
	
	public double getShearCentreY(){ // this formulae is from Figure E1 SANS
		double m;
		double term1 = Math.pow(height, 2)*Math.pow(width, 2)*getBaseThickness()/getIXX();
		double term2 = 1/4+lipLength/(2*width)-2*Math.pow(lipLength, 3)/(3*Math.pow(height, 2)*width);	
		m = term1*term2;		
		// System.out.println("\n\nThe shear centre distance is Yo = "+m+" mm");
		return m;				//returns the shear centre distance from the web edge (m)
	}
	public double getMonosymmetricConstBy(){
		if(monoSBy == 0.0){
			return calcMonosymmetricConstantBy();
		}else{
			return monoSBy;
		}
	}
	
	
	/*
	 * THE FOLLOWING METHODS DETERMINES THE SECTION PROPERTIES IF THEY WERE NOT GIVVEN
	 * THUS SPECIFIED AS 0.0 WHEN INITIALISED
	 */
	public double calcIXX(){
		double t = getBaseThickness();
		double r = getangleRadius();
		double iXX;
		double flange = (width - 2*r)*Math.pow(t, 3)/12. + (width - 2*r)*t*Math.pow(height/2., 2);
		double lip = t*Math.pow(lipLength-r, 3)/12. + t*(lipLength-r)*Math.pow(height/2 - lipLength/2 - r/2,2);
		double web = t*Math.pow(height-2*r, 3)/12.;
		double cornerA = Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow((height-2*r)/2, 2);
		double cornerB = Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow((height-2*r)/2, 2);
		
		iXX = web+2*flange+2*lip +4*(cornerA - cornerB);
		
		return iXX;
	}
	
	public double calcCentroidX(){
		double t = getBaseThickness();
		double r = getangleRadius();
		double ax;
		double flange = width/2.*((width - 2.*r)*t);
		double lips = width*((lipLength - r)*t);
		
		double cornersTop1 = (width - r + 4.*(r+t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r+t/2., 2)/4.));
		double cornersTop2 = (width - r + 4.*(r-t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r-t/2., 2)/4.));
		
		double cornersBottom1 = (r - 4.*(r+t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r+t/2., 2)/4.));
		double cornersBottom2 = (r - 4.*(r-t/2.)/(3.*Math.PI))*((Math.PI*Math.pow(r-t/2., 2)/4.));
		
		double bottom = getArea();
		ax = (2*flange + 2*lips + 2*(cornersTop1 - cornersTop2) + 2*(cornersBottom1 - cornersBottom2))/bottom;
		
		return ax;
	}
	
	public double calcIYY(){
		double t = getBaseThickness();
		double r = getangleRadius();
		double iYY;
		double flange = t*Math.pow(width - 2*r, 3)/12. + t*(width - 2.*r)*Math.pow(width/2. - getCentroidY(),2);
		double lip = (lipLength - r)*Math.pow(t,3)/12. + t*(lipLength - r)*Math.pow(width - getCentroidY(),2);
		double web = (height-2*r)*Math.pow(t, 3)/12. + t*(height -r)*Math.pow(getCentroidY(),2);
		double cornerA1 = Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(width-r-getCentroidY(), 2);
		double cornerA2 = Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(width-r-getCentroidY(), 2);
		double cornerB1 = Math.PI*Math.pow(r+t/2., 4)/16. + Math.PI*(Math.pow(r+t/2., 2)/4)*Math.pow(getCentroidY()-r, 2);
		double cornerB2 = Math.PI*Math.pow(r-t/2., 4)/16. + Math.PI*(Math.pow(r-t/2., 2)/4)*Math.pow(getCentroidY()-r, 2);
		iYY = 2*flange + 2*lip + web + 2*(cornerA1 - cornerA2) + 2*(cornerB1 - cornerB2);
		
		return iYY;
	}
	
	public double calcArea(){
		double t = getBaseThickness();
		double r = getangleRadius();
		double areaWeb = (height - 2.*r)*t;
		double areaFlanges = 2.*(width - 2.*r)*t;
		double areaLips = 2.*(lipLength - r)*t;
		double angleArea = 4.*(Math.PI*Math.pow(r+t/2., 2)/4. - Math.PI*Math.pow(r - t/2., 2)/4.);
		double area = areaWeb + areaFlanges + areaLips + angleArea;
		return area;
	}
	
	public double calcZex(){ //section modulus about the x axis
		double zex = getIXX()/(height/2);
		return zex;
	}
	public double calcZeyLipsideTop(){ // this is when bending occurs when the lips are at the top
		double zey = getIYY()/(width-getCentroidY());
		return zey;
	}
	public double calcZeyWebsideTop(){ //this is when bending occurs when the web is placed at the top
		double zey = getIYY()/getCentroidY();
		return zey;
	}
	public double calcPolarRx(){
		double rx = Math.pow(getIXX()/getArea(),0.5);
		return rx;
	}
	public double calcPolarRy(){
		double ry = Math.pow(getIYY()/getArea(),0.5);
		return ry;
	}
	public double calcJ(){
		double t = getBaseThickness();
		double J;
		double term1 = 2*(lipLength-t/2) + 2*(width-t) + height;
		double term2 = Math.pow(t, 3)/3;
		J = term1*term2;
		return J;
	}
	
	public double calcMonosymmetricConstantBy(){
		double x = -1.*getCentroidY();		
		double xo = -1.*(getCentroidY()+getShearCentreY());
		double t = getBaseThickness();
		double Iy = getIYY();
		double w = width+t;
		double h = height+t;
		double l = lipLength + t/2.;
		double Bw = 1./12.*(t*x*Math.pow(h,3)) + t*Math.pow(x, 3)*h;
		double Bf = 1./2.*t*(Math.pow(w + x,4)-Math.pow(x,4)) + 1./4.*Math.pow(h,2)*t*(Math.pow(w+x,2)-Math.pow(x,2));
		double Bl = 2*l*t*Math.pow(x+w, 3) + 2./3.*t*(x+w)*(Math.pow(h/2.,3)-Math.pow(h/2. - l,3));
		
		double By =(Bw + Bf + Bl)/Iy - 2*xo;
		
		return By;
		
	}	
	
	/*
	 * This method prints out the information regarding the section applied.
	 */
	public void printSectionProperties(){
		System.out.println("THE FOLLOWING ARE THE PROPERTIES OF THE "+name+" LIPPED CHANNEL SECTION");
		System.out.println("************************************************************************");
		System.out.println("The height of the section                      h = "+getHeight()+" mm");
		System.out.println("The width of the section                       w = "+getWidth()+" mm");
		System.out.println("The Lip Length of the section                  d = "+getLipLength()+" mm");
		System.out.println("The Base Metal thickness is                    t = "+getBaseThickness()+" mm");
		System.out.println("The area of the section is                     A = "+getArea()+" mm2");
		System.out.println("The X centroid of the section                 ay = "+getCentroidY()+" mm");
		System.out.println("The Second Moment about x-x                  IXX = "+getIXX()+" mm6");
		System.out.println("The Section Modulus about x-x                Zex = "+getSectionModulusX()+" mm3");
		System.out.println("The Polar radius about x-x                    rx = "+getPolarRX()+" mm");
		System.out.println("The Second Moment about y-y                  IYY = "+getIYY()+" mm6");
		System.out.println("The Section Modulus about y-y: lip at top    Zey = "+getSectionModulusYLipTop()+" mm3");
		System.out.println("The Section Modulus about y-y: Web at top    Zey = "+getSectionModulusYWebTop()+" mm3");
		System.out.println("The Polar radius abou y-y                     ry = "+getPolarRY()+" mm");
		System.out.println("The Torsion Moment: St Venant                  J = "+getJ()+" mm4");
		System.out.println("The Warping Constant                           w = "+getWarping()+" mm9");
		System.out.println("The Shear Centre distance from web centre      m = "+getShearCentreY()+" mm");
		System.out.println("The MonoSymmetric constant:                   By = "+getMonosymmetricConstBy());
		System.out.println("**************************************************************************");
	}
}
