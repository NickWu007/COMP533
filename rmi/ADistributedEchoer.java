package rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import combined.Config;
import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;

public class ADistributedEchoer implements Echoer{
	
	List<Echoee> clients = new ArrayList<Echoee>();
	
	public void registerForListen(Echoee remote) throws RemoteException {
		clients.add(remote);
		System.out.println("A new client registered for listen");
	}
	
	public void nonAtomicBroadcast(String message, Echoee caller) throws RemoteException, InterruptedException {
		Config.getSynchronousObject().startSynchro();
		List<Echoee> deadClients = new ArrayList<Echoee>();
		for (Echoee remote : clients) {
			try {
				if (!caller.equals(remote)) {
					remote.processCmd(message);
				}
			} catch (IOException e) {
				deadClients.add(remote);
			}
		}
		if (deadClients.size() > 0) {
			for (Remote remote : deadClients) {
				clients.remove(remote);
			}
		}
		Config.getSynchronousObject().endSynchro();
	}

	public void changeBroadcastScheme(BroadcastModeVal newState, Echoee caller) throws RemoteException, InterruptedException {
		Config.getBroadcastMode().setBroadcastMode(newState);
		
		List<Echoee> deadClients = new ArrayList<Echoee>();
		for (Echoee remote : clients) {
			try {
				if (!caller.equals(remote)) {
					boolean success = remote.changeBroadcastScheme(newState);
				}
			} catch (Exception e) {
				deadClients.add(remote); 
			}
		}
		
		if (deadClients.size() > 0) {
			for (Remote remote : deadClients) {
				clients.remove(remote);
			}
		}
		Config.getBroadcastMode().setChanging(false);
		
		if (Config.isConsensusForBroadcast()) {
			for (Echoee remote : clients) {
				try {
					remote.notifyChangeBroadcastMode();
				} catch (Exception e) {
					deadClients.add(remote);
				}
			}
			
			if (deadClients.size() > 0) {
				for (Remote remote : deadClients) {
					clients.remove(remote);
				}
			}
		}
	}

	public void atomicBroadcast(String message) throws RemoteException, InterruptedException {
		Config.getSynchronousObject().startSynchro();
		List<Echoee> deadClients = new ArrayList<Echoee>();
		
		for (Echoee remote : clients) {
			try {
				remote.processCmd(message);
				
			} catch (IOException e) {
				deadClients.add(remote);
			}
		}
		
		if (deadClients.size() > 0) {
			for (Remote remote : deadClients) {
				clients.remove(remote);
			}
		}
		Config.getSynchronousObject().endSynchro();
	}

	@Override
	public void changeConsensusModeForBroadcast(boolean newMode) throws RemoteException {
		Config.setConsensusForBroadcast(newMode);
		
		List<Echoee> deadClients = new ArrayList<Echoee>();
		for (Echoee remote : clients) {
			try {
				remote.changeConsensusModeForBroadcast(newMode);
			} catch (Exception e) {
				deadClients.add(remote);
			}
		}
		
		if (deadClients.size() > 0) {
			for (Remote remote : deadClients) {
				clients.remove(remote);
			}
		}
		
	}
	
	@Override
	public void changeConsensusModeForIPC(boolean newMode) throws RemoteException {
		Config.setConsensusForIPC(newMode);;
		
		List<Echoee> deadClients = new ArrayList<Echoee>();
		for (Echoee remote : clients) {
			try {
				remote.changeConsensusModeForIPC(newMode);
			} catch (Exception e) {
				deadClients.add(remote);
			}
		}
		
		if (deadClients.size() > 0) {
			for (Remote remote : deadClients) {
				clients.remove(remote);
			}
		}
	}

	@Override
	public void changeIPCMode(IPCVal newMode, Echoee caller) throws RemoteException, InterruptedException {
		Config.getIPC().setIPCMode(newMode);
		
		List<Echoee> deadClients = new ArrayList<Echoee>();
		for (Echoee remote : clients) {
			try {
				if (!caller.equals(remote)) {
					boolean success = remote.changeIPCMode(newMode);
				}
			} catch (Exception e) {
				deadClients.add(remote);
			}
		}
		
		if (deadClients.size() > 0) {
			for (Remote remote : deadClients) {
				clients.remove(remote);
			}
		}
		Config.getIPC().setChanging(false);
		
		if (Config.isConsensusForIPC()) {
			for (Echoee remote : clients) {
				try {
					remote.notifyChangeIPCMode();
				} catch (Exception e) {
					deadClients.add(remote);
				}
			}
			
			if (deadClients.size() > 0) {
				for (Remote remote : deadClients) {
					clients.remove(remote);
				}
			}
		}
	}
}
