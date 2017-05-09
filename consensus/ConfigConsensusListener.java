package consensus;

import combined.Config;
import combined.Config.IPC.IPCVal;

public class ConfigConsensusListener implements ConsensusListener<Integer>{

	ConsensusClient client;
	
	public ConfigConsensusListener(ConsensusClient client) {
		this.client = client;
	}
	
	@Override
	public void newLocalProposalState(float aProposalNumber, Integer aProposal,
			ProposalState aProposalState) {
		
		
	}

	@Override
	public void newRemoteProposalState(float aProposalNumber, Integer aProposal,
			ProposalState aProposalState) {
		
	}

	@Override
	public void newConsensusState(Integer aState) {
		System.out.println("Changing to config number: " + aState);
		try {
			if (aState == 1) {
				client.simulateNonAtomicAsynchronous();
				Config.getIPC().setIPCMode(IPCVal.NonAtomicAsync);
			} else if (aState == 2) {
				client.simulateNonAtomicSynchronous();
				Config.getIPC().setIPCMode(IPCVal.NonAtomicSync);
			} else if (aState == 3) {
				client.simulateCentralizedAsynchronous();
				Config.getIPC().setIPCMode(IPCVal.CentralizedAsync);
			} else if (aState == 4) {
				client.simulateCentralizedSynchronous();
				Config.getIPC().setIPCMode(IPCVal.CentralizedSync);
			} else if (aState == 5) {
				client.simulateSequentialPaxos();
				Config.getIPC().setIPCMode(IPCVal.SequentialPaxos);
			} else {
				System.out.println("Unsupported config number.");
			}
			System.out.println("Config set to " + Config.getIPC().getIPCMode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void newProposalState(float aProposalNumber, Integer aProposal,
			ProposalState aProposalState) {
	
	}

}