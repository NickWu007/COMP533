package rmi;

import java.rmi.RemoteException;

import combined.Config;
import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;

public class AEchoClient implements EchoClient{
	
	Echoer echoer;
	Echoee echoee;
	
	public AEchoClient(Echoer echoer, Echoee echoee) {
		this.echoer = echoer;
		this.echoee = echoee;
	}
	public void changeConsensusModeForBroadcast(boolean newMode) throws RemoteException{
		this.echoer.changeConsensusModeForBroadcast(newMode);
	}
	
	public void changeConsensusModeForIPC(boolean newMode) throws RemoteException{
		this.echoer.changeConsensusModeForIPC(newMode);
	}
	
	public void handleUserInput(String command) throws InterruptedException, RemoteException {
		System.out.println("Client using RMI");
		if (Config.getBroadcastMode().getBroadcastMode() == BroadcastModeVal.ATOMIC) {
			this.echoer.atomicBroadcast(command);
		}
		if (Config.getBroadcastMode().getBroadcastMode() == BroadcastModeVal.NONATOMIC) {
			this.echoer.nonAtomicBroadcast(command, this.echoee);
		}
	}
}
