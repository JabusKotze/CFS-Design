package section;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import materials.*;

public class Parameters {
	private static SectionDatabase sd = new SectionDatabase();
	private static MaterialDatabase md = new MaterialDatabase();
	private static LoadFactorsCufsm lfcufsm = new LoadFactorsCufsm();
	public static SectionDatabase getSectionDatabase(){
		return sd;
	}
	public static MaterialDatabase getMaterialDatabase(){
		return md;
	}
	public static LoadFactorsCufsm getLoadFactorsCufsm(){
		return lfcufsm;
	}

}
