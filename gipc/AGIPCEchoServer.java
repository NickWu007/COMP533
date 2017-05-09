package gipc;

import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import rmi.ADistributedEchoer;
import rmi.Echoer;

public class AGIPCEchoServer implements GIPCEchoServer{
	static Echoer echoer;
	static GIPCRegistry gipcRegistry;
	static void init() {
		gipcRegistry = GIPCLocateRegistry.createRegistry(REGISTRY_PORT_NAME);
		echoer = new ADistributedEchoer();			
		gipcRegistry.rebind(ECHOER_NAME, echoer);	
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));

	}
	public static void main (String[] args) {		
		init();
//		RPCTraceUtility.setTracing();
	}
}
