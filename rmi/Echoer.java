package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;

public interface Echoer extends Remote {
	void registerForListen(Echoee remote) throws RemoteException;
	void changeBroadcastScheme(BroadcastModeVal newState, Echoee caller) throws RemoteException, InterruptedException;
	void changeIPCMode(IPCVal newMode, Echoee caller) throws RemoteException, InterruptedException;
	void changeConsensusModeForBroadcast(boolean newMode) throws RemoteException;
	void changeConsensusModeForIPC(boolean newMode) throws RemoteException;
	void nonAtomicBroadcast(String message, Echoee caller) throws RemoteException, InterruptedException;
	void atomicBroadcast(String message) throws RemoteException, InterruptedException;
}