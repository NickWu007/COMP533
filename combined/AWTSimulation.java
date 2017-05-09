package combined;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import StringProcessors.HalloweenCommandProcessor;
import combined.Config.BroadcastMode.BroadcastModeVal;
import consensus.ConsensusClient;
import gipc.AGIPCEchoClient;
import main.BeauAndersonFinalProject;
import niotut.NioClient;
import rmi.AEchoClient;

public class AWTSimulation implements Runnable, PropertyChangeListener{

	AEchoClient rmiClient;
	AGIPCEchoClient gipcClient;
	NioClient nioClient;
//	ConsensusClient consensusClient;
	HalloweenCommandProcessor simulation;
	
	public AWTSimulation() {
		this.simulation = BeauAndersonFinalProject.createSimulation("Simulation", 0, 0, 400, 750, 0, 0);
		simulation.setConnectedToSimulation(false);
		simulation.addPropertyChangeListener(this);
	}
	
	public void bindNIOClient(NioClient nioClient) {
		this.nioClient = nioClient;
		System.out.println("NIO Channel binded");
	}
	
	public void bindRMIClient(AEchoClient rmiClient) {
		this.rmiClient = rmiClient;
		System.out.println("RMI Channel binded");
	}
	
	public void bindGIPCClient(AGIPCEchoClient gipcClient) {
		this.gipcClient = gipcClient;
		System.out.println("GIPC Channel binded");
	}
	
//	public void bindConsensusClient(ConsensusClient consensusClient) {
//		this.consensusClient = consensusClient;
//		System.out.println("ConsensusClient binded");
//	}
	
	@Override
	public void run() {
		System.out.println("Simulation Running");
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent){
		
		if (!anEvent.getPropertyName().equals("InputString")) return;
		if (Config.getBroadcastMode().isChanging()) {
			System.out.println("Broadcast mode changing, ignore command");
			return;
		}
		if (Config.getIPC().isChanging()) return;
		long startTime = System.currentTimeMillis();
//		System.out.println("Start time: " + startTime);
//		consensusClient.startTime = startTime;
		String newCommand = (String) anEvent.getNewValue();
		String[] commands = newCommand.split(",");
		try {
//			System.out.println("Local handling length: " + commands.length);
			if (commands.length > 1 && simulation.isConnectedToSimulation()) {
				for (String command: commands) {
					simulation.processCommand(command);
				}
			}
			if (Config.getBroadcastMode().getBroadcastMode() == BroadcastModeVal.LOCAL) {
				System.out.println("Returning because of local mode");
				System.out.println("Time used: " + (System.currentTimeMillis() - startTime) / 1000.0);
				return;
			}
			switch (Config.getIPC().getIPCMode()) {
//			case NonAtomicAsync:
//			case NonAtomicSync:
//			case CentralizedAsync:
//			case CentralizedSync:
//			case SequentialPaxos:
//				consensusClient.proposeCommand(newCommand);
//				break;
			case NIO:
				this.nioClient.send(newCommand.getBytes());
				break;
			case RMI:
				this.rmiClient.handleUserInput(newCommand);
				break;
			case GIPC:
				this.gipcClient.handleUserInput(newCommand);
				break;
			default: break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
//		System.out.println("Time used: " + (System.currentTimeMillis() - startTime) / 1000.0);
	}

}
