package rmi;

import java.rmi.RemoteException;

import StringProcessors.HalloweenCommandProcessor;
import combined.Config;
import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;

public class ADistributedEchoee implements Echoee{
	
	public HalloweenCommandProcessor simulation;
	
	public ADistributedEchoee(HalloweenCommandProcessor simulation) {
		this.simulation = simulation;
	}
	
	public void processCmd(String cmd) throws RemoteException {
		if (Config.getBroadcastMode().isChanging()) return;
		String[] commands = cmd.split(",");
		for (String command: commands) {
			simulation.processCommand(command);
		}
	}
	
	public void notifyChangeBroadcastMode() throws RemoteException, InterruptedException {
		synchronized (Config.getBroadcastMode()) {
			Config.getBroadcastMode().setChanging(false);
			if (Config.getBroadcastMode().getBroadcastMode() == BroadcastModeVal.ATOMIC) simulation.setConnectedToSimulation(false);
			else  simulation.setConnectedToSimulation(true);
			Config.getBroadcastMode().notify();
		}
		System.out.println("Finish notify broadcast, can take user input now.");
	}

	public boolean changeBroadcastScheme(BroadcastModeVal newMode) throws RemoteException, InterruptedException {
		if (!Config.getBroadcastMode().isChanging()) {
			Config.getBroadcastMode().setBroadcastMode(newMode);
			if (Config.isConsensusForBroadcast()) {
				// Disable user input until notified.
				simulation.setConnectedToSimulation(false);
			} else {
				if (newMode == BroadcastModeVal.ATOMIC) simulation.setConnectedToSimulation(false);
			}
		}
		
		return true;
	}
	
	public boolean changeIPCMode(IPCVal newMode) throws RemoteException, InterruptedException {
		Config.getIPC().setIPCMode(newMode);
		return true;
	}
	
	public void notifyChangeIPCMode() throws RemoteException, InterruptedException {
		synchronized (Config.getIPC()) {
			Config.getIPC().setChanging(false);
			Config.getIPC().notify();
		}
	}

	@Override
	public void changeConsensusModeForBroadcast(boolean newMode) throws RemoteException {
		Config.setConsensusForBroadcast(newMode);
	}

	@Override
	public void changeConsensusModeForIPC(boolean newMode) throws RemoteException {
		Config.setConsensusForIPC(newMode);
	}

}
