package combined;

import java.util.Scanner;

import inputport.datacomm.simplex.buffer.nio.AScatterGatherSelectionManager;
import port.trace.nio.NIOTraceUtility;

public class ServerStarter {
	public static void main(String[] args) {
		
//		NIOTraceUtility.setTracing();
//		Thread nioServer = new Thread(new NIOServerStarter());
//		nioServer.start();
		
//		Thread rmiServer = new Thread(new RMIServerStarter());
//		rmiServer.start();
//		port.sessionserver.ASessionServerLauncher.main(args);
		AScatterGatherSelectionManager.setMaxOutstandingWrites(100);
		Thread gipcServer = new Thread(new GIPCServerStarter());
		gipcServer.start();
		
		Scanner sc = new Scanner(System.in);
		while (true) {
			String input = sc.nextLine();
			if (input.equals("use synchronous")) {
				Config.setSynchronous(true);
			} else if (input.equals("not use synchronous")) {
				Config.setSynchronous(false);
			} else if (input.equals("show clients")){
			}
		}
	}
}
