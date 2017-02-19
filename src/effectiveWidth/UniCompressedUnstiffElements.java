package effectiveWidth;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
/*
 * THIS CLASS CONTAINS THE METHODS TO DETERMINE THE EFFECTIVE WIDTH FOR 
 * UNIFORMLY COMPRESSED UNSTIFFENED ELEMENTS
 * 
 * THUS ONLY DIFFERENCE FROM CLASS UniCompressedStifElements is that k = 0.43; instead of k= 4.
 */
public class UniCompressedUnstiffElements {
			private double stressF;
			private double k,p,t,b;
			private String secName,matName;
			private UniCompressedStifElements uCSE;
	
			public UniCompressedUnstiffElements(double stressF,double t, double b, String matName, String secName){
				this.secName = secName;
				this.matName = matName;				
				this.t = t;
				this.b = b;
				this.stressF = stressF;				
			}
			
			public double calcEffwidth(){
				double effWidth;
				k =0.43;
				p=0.0;
				uCSE = new UniCompressedStifElements(stressF,k,p,t,b,matName,secName);
				effWidth = uCSE.effectiveWidth();
				
				return effWidth;
			}
}
