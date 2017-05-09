package combined;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rmi.ADistributedEchoee;
import rmi.AEchoClient;
import rmi.Echoee;
import rmi.Echoer;

public class RMIClientStarter implements Runnable{

	AWTSimulation simulation;
	AEchoClient client;
	
	public RMIClientStarter(AWTSimulation simulation) {
		this.simulation = simulation;
	}
	
	@Override
	public void run() {
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(Constants.RMI_REGISTRY_HOST_NAME, Constants.RMI_REGISTRY_PORT_NAME);
			
			Echoee echoee = new ADistributedEchoee(simulation.simulation);
			UnicastRemoteObject.exportObject(echoee, 0);
			
			Echoer echoer = (Echoer) rmiRegistry.lookup(Constants.RMI_ECHOER_NAME);	
			
			echoer.registerForListen(echoee);
			
			client = new AEchoClient(echoer, echoee);
			simulation.bindRMIClient(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
