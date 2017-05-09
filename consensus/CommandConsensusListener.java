package consensus;

import StringProcessors.HalloweenCommandProcessor;

public class CommandConsensusListener implements ConsensusListener<String>{

	HalloweenCommandProcessor simulation;
	ConsensusClient client;
	
	public CommandConsensusListener(HalloweenCommandProcessor simulation, ConsensusClient client) {
		this.simulation = simulation;
		this.client = client;
	}
	
	@Override
	public void newConsensusState(String aState) {
		simulation.processCommand(aState);
	}

	@Override
	public void newLocalProposalState(float aProposalNumber, String aProposal, ProposalState aProposalState) {		
	}

	@Override
	public void newRemoteProposalState(float aProposalNumber, String aProposal, ProposalState aProposalState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newProposalState(float aProposalNumber, String aProposal, ProposalState aProposalState) {
		// TODO Auto-generated method stub
		
	}
}
