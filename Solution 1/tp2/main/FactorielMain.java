package tp2.main;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class FactorielMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		startWithProfile();
	}
	
	public static void startWithProfile() {
		 Runtime rt = Runtime.instance();
	  	  ProfileImpl p = null;
	  	ContainerController cc; 
	  	  try{
	  		/**
	  		 * host : null value means use the default (i.e. localhost)
             * port - is the port number. A negative value should be used for using the default port number.
             * platformID - is the symbolic name of the platform, 
             * isMain : boolean
	  		 */
	  		
	  		  p  =  new  ProfileImpl(null,-1,"tdia04",false);
	  		
		    cc = rt.createAgentContainer(p);
		    
		    AgentController ac = cc.createNewAgent("FACT", "tp2.agent.FactAgent", null);
			ac.start();
			ac = cc.createNewAgent("MULT1", "tp2.agent.MultAgent", null);
			ac.start();
			ac = cc.createNewAgent("MULT2", "tp2.agent.MultAgent", null);
			ac.start();
			ac = cc.createNewAgent("STORE", "tp2.agent.StoreAgent", null);
			ac.start();
	  	  } catch(Exception ex) {
	  		  ex.printStackTrace();
	  	  }
	}
}
