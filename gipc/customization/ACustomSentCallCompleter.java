package gipc.customization;

import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ExplicitSourceReceive;
import inputport.rpc.duplex.ADuplexSentCallCompleter;
import inputport.rpc.duplex.DuplexRPCInputPort;
import inputport.rpc.duplex.DuplexSentCallCompleter;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;
import inputport.rpc.duplex.RPCReturnValue;
import port.trace.rpc.ReceivedObjectTransformed;
import port.trace.rpc.RemoteCallReceivedReturnValue;
import port.trace.rpc.RemoteCallWaitingForReturnValue;

public class ACustomSentCallCompleter extends ADuplexSentCallCompleter	{
	
	public ACustomSentCallCompleter(DuplexRPCInputPort aPort, LocalRemoteReferenceTranslator aRemoteHandler) {
		super(aPort, aRemoteHandler);		
	}	
	@Override
	protected void returnValueReceived(String aRemoteEndPoint, Object message) {
		System.out.println ("Processing return value of call:" + aRemoteEndPoint + "." + message);
//		super.returnValueReceived(aRemoteEndPoint, message);		
	}
	@Override
	public Object waitForReturnValue(String aRemoteEndPoint) {
		System.out.println ("waitForReturnValue called");
		Object retVal;
		while (true) {
			retVal = ((DuplexRPCInputPort)inputPort).receive(aRemoteEndPoint);		
			if (retVal != null && ((AReceiveReturnMessage)retVal).getMessage() instanceof RPCReturnValue) break;
		}
		System.out.println (aRemoteEndPoint +  "-->" + retVal);
		return retVal;
	}
	protected Object getReturnValueOfRemoteFunctionCall(String aRemoteEndPoint, Object aMessage) {
		System.out.println ("getReturnValueOfRemoteFunctionCall called");
		Object retVal = super.getReturnValueOfRemoteFunctionCall(aRemoteEndPoint, aMessage);
		System.out.println ("Returning:" + retVal);
		return retVal;
	}
	protected Object getReturnValueOfRemoteProcedureCall(String aRemoteEndPoint, Object aMessage) {
		RemoteCallWaitingForReturnValue.newCase(this);
		Object retVal = waitForReturnValue(aRemoteEndPoint);
		RemoteCallReceivedReturnValue.newCase(this, retVal);
		System.out.println ("Returning:" + retVal);
		return  retVal;
	}
}
