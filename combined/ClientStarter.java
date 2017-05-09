package combined;

import java.util.Scanner;

import combined.Config.BroadcastMode.BroadcastModeVal;
import combined.Config.IPC.IPCVal;
import consensus.CommandConsensusListener;
import consensus.ConfigConsensusListener;
import consensus.ConsensusClient;
import inputport.datacomm.simplex.buffer.nio.AScatterGatherSelectionManager;
import niotut.NioClient;
import niotut.RspHandler;
import port.trace.consensus.ConsensusTraceUtility;
import port.trace.nio.NIOTraceUtility;
import util.trace.TraceableInfo;
import util.trace.Tracer;

public class ClientStarter {
	
	private static final int LOOP_NUMER = 500;
	public static void main(String[] args) {
		try {
			Tracer.showWarnings(false); // do not show oeall22 and other warnings
			AScatterGatherSelectionManager.setMaxOutstandingWrites(100);
//			ConsensusTraceUtility.setTracing();
			Scanner sc = new Scanner(System.in);
//			String inputId = sc.nextLine();
//			String portStr = sc.nextLine();
//			ConsensusClient consensusClient = new ConsensusClient(inputId, Integer.parseInt(portStr));
			
//			NIOTraceUtility.setTracing();
			AWTSimulation simulation = new AWTSimulation();
//			simulation.bindConsensusClient(consensusClient);
		
//			RMIClientStarter rmiClientStarter = new RMIClientStarter(simulation);
//			CommandConsensusListener commandListener = new CommandConsensusListener(simulation.simulation, consensusClient);
//			ConfigConsensusListener configListener = new ConfigConsensusListener(consensusClient);
//			consensusClient.addCommandListenersAndVetoersToGreetingMechanism(commandListener);
//			consensusClient.addConfigListenersToMeaningMechanism(configListener);
			GIPCClientStarter gipcClientStarter = new GIPCClientStarter(simulation);
//			NIOClientStarter nioClientStarter = new NIOClientStarter(simulation);
			
//			Thread nioClient = new Thread(nioClientStarter);
//			nioClient.start();
//			
//			Thread rmiClient = new Thread(rmiClientStarter);
//			rmiClient.start();
			
			Thread gipcClient = new Thread(gipcClientStarter);
			gipcClient.start();
			
			Thread awtThread = new Thread(simulation);
			awtThread.start();
			
			
			while (true) {
				String input = sc.nextLine();
				if (input.equals("use atomic")) {
					gipcClientStarter.changeBroadcastMode(BroadcastModeVal.ATOMIC);
				}
				if (input.equals("use local")) {
					gipcClientStarter.changeBroadcastMode(BroadcastModeVal.LOCAL);
				}
				if (input.equals("use non-atomic")) {
					gipcClientStarter.changeBroadcastMode(BroadcastModeVal.NONATOMIC);
				}
				if (input.equals("use same ipc")) {
					Config.setUseSameIPC(true);
				}
				if (input.equals("not use same ipc")) {
					Config.setUseSameIPC(false);
				}
				if (input.equals("use consensus for broadcast")) {
					if (!Config.isConsensusForBroadcast()) gipcClientStarter.changeConsensusModeForBroadcast(true);
				}
				if (input.equals("not use consensus for broadcast")) {
					if (Config.isConsensusForBroadcast()) gipcClientStarter.changeConsensusModeForBroadcast(false);
				}
				if (input.equals("use consensus for ipc")) {
					if (!Config.isConsensusForIPC()) gipcClientStarter.changeConsensusModeForIPC(true);
				}
				if (input.equals("not use consensus for ipc")) {
					if (Config.isConsensusForIPC()) gipcClientStarter.changeConsensusModeForIPC(false);
				}
//				if (input.equals("use NonAtomicAsync")) {
//					consensusClient.proposeConfig(1);
//				}
//				if (input.equals("use NonAtomicSync")) {
//					consensusClient.proposeConfig(2);
//				}
//				if (input.equals("use CentralizedAsync")) {
//					consensusClient.proposeConfig(3);
//				}
//				if (input.equals("use CentralizedSync")) {
//					consensusClient.proposeConfig(4);
//				}
//				if (input.equals("use SequentialPaxos")) {
//					consensusClient.proposeConfig(5);
//				}
				if (input.equals("use NIO")) {
					if (Config.getIPC().getIPCMode() != IPCVal.NIO) gipcClientStarter.changeIPCMode(IPCVal.NIO);
				}
				if (input.equals("use RMI")) {
					if (Config.getIPC().getIPCMode() != IPCVal.RMI) gipcClientStarter.changeIPCMode(IPCVal.RMI);
				}
				if (input.equals("use GIPC")) {
					if (Config.getIPC().getIPCMode() != IPCVal.GIPC) gipcClientStarter.changeIPCMode(IPCVal.GIPC);
				}
				if (input.equals("time")) {
					long startTime = System.currentTimeMillis();
					for (int i = 1; i <= LOOP_NUMER; i++) {
						simulation.simulation.setInputString("move 1 0");
					}
					System.out.println("Time used: " + (System.currentTimeMillis() - startTime) / 1000.0);
				}
				if (input.equals("time2")) {
					long startTime = System.currentTimeMillis();
					for (int i = 1; i <= LOOP_NUMER; i++) {
						simulation.simulation.setInputString("move -1 0");
					}
					System.out.println("Time used: " + (System.currentTimeMillis() - startTime) / 1000.0);
				}
				if (input.equals("show config")) {
					Config.printConfig();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
