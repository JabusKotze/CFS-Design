package gui;
/*
 * @author JJR Kotze (email 15692183@sun.ac.za)
 * For Department of Civil Engineering, Stellenbosch University
 * 29 October 2012 (Version 1.0 of CFS design)
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
public class OpenCufsmButtonListener implements ActionListener {

	public OpenCufsmButtonListener(){
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Open CUFSM")){
			 Runtime rt = Runtime.getRuntime();
			 Process p = null;
			
			try
			{	
			String s = "cufsm4.exe";
			 p = rt.exec(s) ;
			 //InputStream in = p.getInputStream();
			 
			 
			 //in.read();					 
			
			}catch(Exception exc){/*handle exception*/
				System.out.println(exc);
				exc.printStackTrace();
			}			
			
		}
		
	}

}
