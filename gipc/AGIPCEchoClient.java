package gipc;

import java.rmi.RemoteException;

import combined.Config;
import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;
import inputport.rpc.GIPCRegistry;
import rmi.Echoee;
import rmi.Echoer;

public class AGIPCEchoClient implements GIPCEchoClient{
	static GIPCRegistry gipcRegistry;
	Echoer echoer;
	Echoee echoee;
	long startTime;
	
	
	public AGIPCEchoClient(Echoer echoer, Echoee echoee)  throws RemoteException{
		this.echoee = echoee;
		this.echoer = echoer;
	}
	
	public void changeBroadcastMode(BroadcastModeVal newMode) throws RemoteException, InterruptedException{
		this.echoer.changeBroadcastScheme(newMode, this.echoee);
	}
	
	public void changeIPCMode(IPCVal newMode) throws RemoteException, InterruptedException{
		Config.getIPC().setIPCMode(newMode);
		this.echoer.changeIPCMode(newMode, this.echoee);
	}
	
	public void changeConsensusModeForBroadcast(boolean newMode) throws RemoteException{
		this.echoer.changeConsensusModeForBroadcast(newMode);
	}
	
	public void changeConsensusModeForIPC(boolean newMode) throws RemoteException{
		this.echoer.changeConsensusModeForIPC(newMode);
	}
	
	public void handleUserInput(String command) throws InterruptedException, RemoteException {
		if (Config.getBroadcastMode().getBroadcastMode() == BroadcastModeVal.ATOMIC) {
			this.echoer.atomicBroadcast(command);
		} else if (Config.getBroadcastMode().getBroadcastMode() == BroadcastModeVal.NONATOMIC) {
			this.echoer.nonAtomicBroadcast(command, this.echoee);
		} else if (Config.getBroadcastMode().getBroadcastMode() == BroadcastModeVal.LOCAL) {
		}
	}
}
