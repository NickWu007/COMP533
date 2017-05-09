package gipc.customization;
import java.util.concurrent.ArrayBlockingQueue;

public class MessageQueue extends ArrayBlockingQueue{

	private int counter;
	
	public MessageQueue(int capacity) {
		super(capacity);
		this.counter = 0;
	}
	
	public boolean hasThreadWaiting() {
		return this.counter > 0;
	}
	
	@Override
	public synchronized Object take() throws InterruptedException {
		this.counter++;
		Object rv = super.take();
		this.counter--;
		return rv;
	}

}
