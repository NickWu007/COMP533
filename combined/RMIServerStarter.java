package combined;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import rmi.ADistributedEchoer;
import rmi.Echoer;

public class RMIServerStarter implements Runnable{

	@Override
	public void run() {
		try {
			Registry rmiRegistry = LocateRegistry.createRegistry(Constants.RMI_REGISTRY_PORT_NAME);
			Echoer echoer = new ADistributedEchoer();
			UnicastRemoteObject.exportObject(echoer, 0);
			rmiRegistry.rebind(Constants.RMI_ECHOER_NAME, echoer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
