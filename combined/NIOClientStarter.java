package combined;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;

import niotut.NioClient;
import niotut.RspHandler;
import util.trace.TraceableInfo;
import util.trace.Tracer;

public class NIOClientStarter implements Runnable{

	AWTSimulation simulation;
	NioClient client;
	
	public NIOClientStarter(AWTSimulation simulation) {
		this.simulation = simulation;
	}
	
	public void handleUserInput(String command) throws IOException{
		client.send(command.getBytes());
	}
	
	@Override
	public void run() {
		try {
	    	NioClient client = new NioClient(InetAddress.getByName(Constants.NIO_HOST_NAME), Constants.NIO_REGISTRY_PORT_NAME);
   	     	Thread t = new Thread(client);
   	     	t.setDaemon(true);
   	     	t.start();
   	 
   	     	RspHandler handler = new RspHandler(simulation.simulation);
   	     	
   	     	// Start a new connection
   			client.socket = client.initiateConnection();
   			
   			// Register the response handler
   			client.rspHandlers.put(client.socket, handler);
   			simulation.bindNIOClient(client);
   			client.selector.wakeup();
	    	while (true) {
	   	     	handler.waitForResponse();
	    	}
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}

}
