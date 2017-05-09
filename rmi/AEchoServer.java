package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AEchoServer implements EchoServer{

	public static void main(String[] args) {
		try {
			Registry rmiRegistry = LocateRegistry.createRegistry(REGISTRY_PORT_NAME);
			Echoer echoer = new ADistributedEchoer();
			UnicastRemoteObject.exportObject(echoer, 0);
			rmiRegistry.rebind(ECHOER_NAME, echoer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
