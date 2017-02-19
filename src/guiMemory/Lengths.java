package guiMemory;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

public class Lengths {
		public double lx,ly,lz;
		public int multiples;
		
		public Lengths(){
					
		}
		public void setLx(double Lx){
			lx = Lx;
		}
		public void setLy(double Ly){
			ly = Ly;
		}
		public void setLz(double Lz){
			lz = Lz;
		}
		public void setMultiples(int mult){
			multiples = mult;
		}
		
		public double getLx(){
			return lx;
		}
		public double getLy(){
			return ly;
		}
		public double getLz(){
			return lz;
		}
		public int getMult(){
			return multiples;
		}
}
