package guiMemory;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */

public class GetMemory {
		
		private static Lengths L = new Lengths();
		private static Moments M = new Moments();
		private static Loads loads = new Loads();
		private static Compression comp = new Compression();
		
		
		public static Lengths getLengths(){
			return L;
		}
		public static Moments getMoments(){
			return M;
		}
		public static Loads getLoads(){
			return loads;
		}
		public static Compression getComp(){
			return comp;
		}
}
