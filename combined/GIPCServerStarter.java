package combined;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import gipc.customization.ACustomCounterClient;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import port.trace.objects.ReceivedMessageDequeued;
import port.trace.objects.ReceivedMessageQueueCreated;
import port.trace.objects.ReceivedMessageQueued;
import rmi.ADistributedEchoer;
import rmi.Echoer;
import util.trace.ImplicitKeywordKind;
import util.trace.TraceableInfo;
import util.trace.Tracer;

public class GIPCServerStarter implements Runnable{

	static Echoer echoer;
	static GIPCRegistry gipcRegistry;
	
	@Override
	public void run() {
		Tracer.showInfo(true);
		Tracer.setDisplayThreadName(true); 
		TraceableInfo.setPrintTraceable(true);
		TraceableInfo.setPrintSource(true);
		
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_CLASS_NAME);
		Tracer.setKeywordPrintStatus(ReceivedMessageQueueCreated.class, true);
		Tracer.setKeywordPrintStatus(ReceivedMessageQueued.class, true);
		Tracer.setKeywordPrintStatus(ReceivedMessageDequeued.class, true);
		
		ACustomCounterClient.setFactories();
		gipcRegistry = GIPCLocateRegistry.createRegistry(Constants.GIPC_REGISTRY_PORT_NAME);
		echoer = new ADistributedEchoer();			
		gipcRegistry.rebind(Constants.GIPC_ECHOER_NAME, echoer);	
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));
	}

}
