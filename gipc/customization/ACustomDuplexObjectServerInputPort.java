package gipc.customization;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import inputport.datacomm.ReceiveRegistrarAndNotifier;
import inputport.datacomm.duplex.DuplexServerInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectServerInputPort;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import port.trace.objects.ReceivedMessageDequeued;
import port.trace.objects.ReceivedMessageQueueCreated;

public class ACustomDuplexObjectServerInputPort extends ADuplexObjectServerInputPort{
	
	protected Map pendingWaits;
	protected MessageQueue generalQueue;

	public ACustomDuplexObjectServerInputPort(
			DuplexServerInputPort<ByteBuffer> aBBDuplexServerInputPort) {
		super(aBBDuplexServerInputPort);
	}
	protected ReceiveRegistrarAndNotifier<Object> createReceiveRegistrarAndNotifier() {
		if (this.pendingWaits == null) {
			this.pendingWaits = new HashMap();
		}
		
		if (this.generalQueue == null) {
			this.generalQueue = new MessageQueue(2048);
		}
		ACustomServerReceiveNotifier notifier =  new ACustomServerReceiveNotifier(this.pendingWaits, this.generalQueue);
		this.pendingWaits = notifier.pendingWaits;
		this.generalQueue = notifier.generalQueue;
		return notifier;
	}
	
	@Override
	public ReceiveReturnMessage<Object> receive(String aSource) {
		System.err.println("Receive started");
		try {
			Object message;
			if (this.getSender() == null) {
				System.out.println("waiting on genreal queue.");
				message = this.generalQueue.take();
				ReceivedMessageDequeued.newCase(this, pendingWaits, "generalQueue");
			} else {
				MessageQueue queue = (MessageQueue) pendingWaits.get(aSource);
				if (queue == null) {
					System.out.println("waiting on genreal queue.");
					message = this.generalQueue.take();
					ReceivedMessageDequeued.newCase(this, pendingWaits, "generalQueue");
				} else {
					System.out.println("waiting on queue for " + aSource);
					message = queue.take();
					ReceivedMessageDequeued.newCase(this, pendingWaits, "queue for " + aSource);
				}
			}
			
			ReceiveReturnMessage retVal = new AReceiveReturnMessage(aSource, message);
			return retVal;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
