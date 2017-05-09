package gipc.customization;

import util.trace.Tracer;

import java.util.concurrent.BlockingQueue;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import inputport.datacomm.ReceiveListener;
import port.trace.objects.ReceivedMessageQueued;

public class ACustomClientReceiveNotifier extends AReceiveRegistrarAndNotifier{
	
	protected BlockingQueue pendingWaits;
	
	public ACustomClientReceiveNotifier(BlockingQueue pendingWaits) {
		this.pendingWaits = pendingWaits;
	}
	
	@Override
	public void notifyPortReceive (String aSource, Object aMessage) {	
		System.out.println ("ACustomClientReceiveNotifier: " + aSource + "->" + aMessage);
		
		try {
			ReceivedMessageQueued.newCase(this, pendingWaits, "notifyPortReceive");
			pendingWaits.put(aMessage);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.notifyPortReceive(aSource, aMessage);
	}
}
