package gipc.customization;

import util.trace.Tracer;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import inputport.datacomm.ReceiveListener;
import port.trace.objects.ReceivedMessageQueueCreated;
import port.trace.objects.ReceivedMessageQueued;

public class ACustomServerReceiveNotifier extends AReceiveRegistrarAndNotifier{
	
	protected Map pendingWaits;
	protected MessageQueue generalQueue;
	
	public ACustomServerReceiveNotifier(Map pendingWaits, MessageQueue generalQueue) {
		this.pendingWaits = pendingWaits;
		this.generalQueue = generalQueue;
	}
	
	@Override
	public void notifyPortReceive (String aSource, Object aMessage) {	
		System.out.println ("ACustomReceiveServerNotifier: " + aSource + "->" + aMessage);
		synchronized (this.pendingWaits) {
			try {
				if (aSource == null || aSource.equals("*")) {
					// Put into general queue
					this.generalQueue.put(aMessage);
					ReceivedMessageQueued.newCase(this, generalQueue, "");
				} else {
					MessageQueue queue = (MessageQueue) pendingWaits.get(aSource);
					if (queue == null) {
						// First message from source, create new entry
						// But message still goes into the general queue
						MessageQueue newQueue = new MessageQueue(2048);
						pendingWaits.put(aSource, newQueue);
						ReceivedMessageQueueCreated.newCase(this, newQueue, aSource);
						this.generalQueue.put(aMessage);
						ReceivedMessageQueued.newCase(this, generalQueue, "");
					} else if (queue.hasThreadWaiting()) {
						// Put into specific queue
						queue.put(aMessage);
						ReceivedMessageQueued.newCase(this, queue, aSource);
					} else {
						// Put into general queue
						this.generalQueue.put(aMessage);
						ReceivedMessageQueued.newCase(this, generalQueue, "");
					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.notifyPortReceive(aSource, aMessage);
	}
}
