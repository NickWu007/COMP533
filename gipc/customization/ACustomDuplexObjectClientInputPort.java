package gipc.customization;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.ReceiveRegistrarAndNotifier;
import inputport.datacomm.duplex.DuplexClientInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectClientInputPort;
import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import port.trace.objects.ReceivedMessageDequeued;

public class ACustomDuplexObjectClientInputPort extends ADuplexObjectClientInputPort {

	private BlockingQueue pendingWaits;
	
	public ACustomDuplexObjectClientInputPort(
			DuplexClientInputPort<ByteBuffer> aBBClientInputPort) {
		super(aBBClientInputPort);
	}
	
	protected ReceiveRegistrarAndNotifier<Object> createReceiveRegistrarAndNotifier() {
		if (this.pendingWaits == null) {
			this.pendingWaits = new ArrayBlockingQueue(2048);
		}
		ACustomClientReceiveNotifier notifier =  new ACustomClientReceiveNotifier(this.pendingWaits);
		this.pendingWaits = notifier.pendingWaits;
		return notifier;
	}
	
	@Override
	public void send(String aDestination, Object aMessage) {
		System.out.println (aDestination + "<-" + aMessage);
		super.send(aDestination, aMessage);	
	}
	@Override
	public ReceiveReturnMessage<Object> receive(String aSource) {
		System.err.println("Receive started");
		try {
			Object message = pendingWaits.take();
			ReceivedMessageDequeued.newCase(this, pendingWaits, "client receives");
			ReceiveReturnMessage retVal = new AReceiveReturnMessage(aSource, message);
			return retVal;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
