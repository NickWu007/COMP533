package combined;

import java.io.IOException;

import niotut.EchoWorker;
import niotut.NioServer;

public class NIOServerStarter implements Runnable{

	@Override
	public void run() {
		try {
			EchoWorker worker = new EchoWorker();
			new Thread(worker).start();
			new Thread(new NioServer(null, Constants.NIO_REGISTRY_PORT_NAME, worker)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
