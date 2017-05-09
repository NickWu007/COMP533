package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;

public interface Echoee extends Remote{
	boolean changeBroadcastScheme(BroadcastModeVal newState) throws RemoteException, InterruptedException;
	boolean changeIPCMode(IPCVal newMode) throws RemoteException, InterruptedException;
	void changeConsensusModeForBroadcast(boolean newMode) throws RemoteException;
	void changeConsensusModeForIPC(boolean newMode) throws RemoteException;
	void processCmd(String cmd) throws RemoteException;
	void notifyChangeBroadcastMode() throws RemoteException, InterruptedException;
	void notifyChangeIPCMode() throws RemoteException, InterruptedException;
}
