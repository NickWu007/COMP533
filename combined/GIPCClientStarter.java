package combined;

import java.rmi.RemoteException;

import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;
import gipc.AGIPCEchoClient;
import gipc.customization.ACustomCounterClient;
import gipc.customization.ACustomDuplexObjectInputPortFactory;
import gipc.customization.ACustomDuplexReceivedCallInvokerFactory;
import gipc.customization.ACustomSentCallCompleterFactory;
import gipc.customization.ACustomSerializerFactory;
import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.rpc.ACachingAbstractRPCProxyInvocationHandler;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import inputport.rpc.duplex.DuplexReceivedCallInvokerSelector;
import inputport.rpc.duplex.DuplexSentCallCompleterSelector;
import port.trace.objects.ReceivedMessageDequeued;
import port.trace.objects.ReceivedMessageQueueCreated;
import port.trace.objects.ReceivedMessageQueued;
import rmi.ADistributedEchoee;
import rmi.Echoee;
import rmi.Echoer;
import serialization.SerializerSelector;
import util.trace.ImplicitKeywordKind;
import util.trace.TraceableInfo;
import util.trace.Tracer;

public class GIPCClientStarter implements Runnable{

	AWTSimulation simulation;
	static GIPCRegistry gipcRegistry;
	AGIPCEchoClient client;
	
	public GIPCClientStarter(AWTSimulation simulation) {
		this.simulation = simulation;
	}
	
	public void changeBroadcastMode(BroadcastModeVal newMode) throws RemoteException, InterruptedException {
		if (!Config.getBroadcastMode().isChanging()) {
			Config.getBroadcastMode().setBroadcastMode(newMode);
			if (newMode == BroadcastModeVal.ATOMIC) simulation.simulation.setConnectedToSimulation(false);
		}
		
		this.client.changeBroadcastMode(newMode);
	}
	
	public void changeIPCMode(IPCVal newMode) throws RemoteException, InterruptedException {
		if (Config.getUseSameIPC()) this.client.changeIPCMode(newMode);
		else {
			Config.getIPC().setIPCMode(newMode);
			Config.getIPC().setChanging(false);
		}
	}
	
	public void changeConsensusModeForBroadcast(boolean newMode) throws RemoteException{
		this.client.changeConsensusModeForBroadcast(newMode);
	}
	
	public void changeConsensusModeForIPC(boolean newMode) throws RemoteException{
		this.client.changeConsensusModeForIPC(newMode);
	}
	
	@Override
	public void run() {
		try {
			Tracer.showInfo(true);
			Tracer.setDisplayThreadName(true); 
			TraceableInfo.setPrintTraceable(true);
			TraceableInfo.setPrintSource(true);
			
			Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_CLASS_NAME);
			Tracer.setKeywordPrintStatus(ReceivedMessageQueueCreated.class, true);
			Tracer.setKeywordPrintStatus(ReceivedMessageQueued.class, true);
			Tracer.setKeywordPrintStatus(ReceivedMessageDequeued.class, true);
			
			ACustomCounterClient.setFactories();
			ACachingAbstractRPCProxyInvocationHandler.setInvokeObjectMethodsRemotely(false);
			String clientName = "client-" + System.currentTimeMillis();
			gipcRegistry = GIPCLocateRegistry.getRegistry(Constants.GIPC_REGISTRY_HOST_NAME, Constants.GIPC_REGISTRY_PORT_NAME, clientName);
				
			Echoee echoee = new ADistributedEchoee(simulation.simulation);
			Echoer echoer = (Echoer)gipcRegistry.lookup(Echoer.class, Constants.GIPC_ECHOER_NAME);	
			echoer.registerForListen(echoee);
			client = new AGIPCEchoClient(echoer, echoee);
			simulation.bindGIPCClient(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
