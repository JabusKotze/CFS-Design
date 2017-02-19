package guiMemory;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

public class Moments {
			private double maxAppliedMoment,Mmax,M1,M2,M3,M4,M5;
			
			public Moments(){
				
			}
			public void setM1(double m1){
				M1 = m1;
			}
			public void setM2(double m1){
				M2 = m1;
			}
			public void setM3(double m1){
				M3 = m1;
			}
			public void setM4(double m1){
				M4 = m1;
			}
			public void setM5(double m1){
				M5 = m1;
			}
			public void setMaxAppliedMoment(double m1){
				maxAppliedMoment = m1;
			}
			public void setMmax(double m1){
				Mmax = m1;
			}
			
			
			public double getM1(){
				return M1;
			}
			public double getM2(){
				return M2;
			}
			public double getM3(){
				return M3;
			}
			public double getM4(){
				return M4;
			}
			public double getM5(){
				return M5;
			}
			public double getMaxAppliedMoment(){
				return maxAppliedMoment;
			}
			public double getMmax(){
				return Mmax;
			}
			
		
}
