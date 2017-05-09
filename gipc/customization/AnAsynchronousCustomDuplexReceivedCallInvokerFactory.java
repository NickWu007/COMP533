package gipc.customization;

import inputport.datacomm.NamingSender;
import inputport.datacomm.duplex.DuplexInputPort;
import inputport.rpc.RPCRegistry;
import inputport.rpc.DuplexReceivedCallInvokerFactory;
import inputport.rpc.duplex.AnAsynchronousSingleThreadDuplexReceivedCallInvoker;
import inputport.rpc.duplex.DuplexReceivedCallInvoker;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;

public class AnAsynchronousCustomDuplexReceivedCallInvokerFactory 
	extends ACustomDuplexReceivedCallInvokerFactory{
	@Override
	public DuplexReceivedCallInvoker createDuplexReceivedCallInvoker(
			LocalRemoteReferenceTranslator aRemoteHandler,
			DuplexInputPort<Object> aReplier, RPCRegistry anRPCRegistry) {
		return new 
				AnAsynchronousSingleThreadDuplexReceivedCallInvoker(ACustomSyncDuplexReceivedCallInvokerFactory.createDuplexReceivedCallInvoker(aRemoteHandler, aReplier, anRPCRegistry));
	}
}