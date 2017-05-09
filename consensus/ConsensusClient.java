package consensus;

import consensus.paxos.sequential.ASequentialPaxosConsensusMechanismFactory;
import examples.gipc.consensus.paxos.APaxosMemberLauncher;

public class ConsensusClient extends APaxosMemberLauncher{
	
	public long startTime;
	
	@Override
	protected ConsensusMechanismFactory<String> greetingConsensusMechanismFactory() {
		return new ASequentialPaxosConsensusMechanismFactory<String>();
	}

	public ConsensusClient(String aLocalName, int aPortNumber) {
		super(aLocalName, aPortNumber);
		meaningOfLifeMechanism.setAcceptSynchrony(ReplicationSynchrony.ASYNCHRONOUS);
		meaningOfLifeMechanism.setConcurrencyKind(ConcurrencyKind.NON_ATOMIC);
		meaningOfLifeMechanism.setSequentialAccess(false);
	}
	
	public void proposeCommand(String command) {
		float proposalNum = greetingMechanism.propose(command);
		greetingMechanism.waitForConsensus(proposalNum, reProposeTime());
	}
	
	public void proposeConfig(Integer config) {
		float proposalNum = meaningOfLifeMechanism.propose(config);
	}
	
	public void addCommandListenersAndVetoersToGreetingMechanism(CommandConsensusListener commandListener) {
		greetingMechanism.addConsensusListener(commandListener);
	}
	
	public void addConfigListenersToMeaningMechanism(ConfigConsensusListener configListener) {
		meaningOfLifeMechanism.addConsensusListener(configListener);
	}
	
	@Override 
	protected void addListenersAndVetoersToConsensusMechanisms() {
		
	}

	@Override
	protected ConsensusMechanismFactory<Integer> meaningConsensusMechanismFactory() {
		return super.meaningConsensusMechanismFactory();
	}

	@Override
	protected void customizeConsensusMechanisms() {
	}
	
	protected void simulateNonAtomicAsynchronous() {
		greetingMechanism.setAcceptSynchrony(ReplicationSynchrony.ASYNCHRONOUS);
		greetingMechanism.setConcurrencyKind(ConcurrencyKind.NON_ATOMIC);
		greetingMechanism.setSequentialAccess(false);
	}
	protected void simulateNonAtomicSynchronous() {
		greetingMechanism.setAcceptSynchrony(ReplicationSynchrony.ALL_SYNCHRONOUS);
		greetingMechanism.setConcurrencyKind(ConcurrencyKind.NON_ATOMIC);
		greetingMechanism.setSequentialAccess(false);
	}
	protected void simulateCentralized() {
		greetingMechanism.setCentralized(true);
	}
	protected void simulateCentralizedSynchronous() {
		simulateNonAtomicSynchronous();
		simulateCentralized();
	}
	protected void simulateCentralizedAsynchronous() {
		simulateNonAtomicAsynchronous();
		simulateCentralized();
	}
	protected void simulateBasicPaxos() {
		overrideRetry = true;
		greetingMechanism.setCentralized(false);
		greetingMechanism.setConcurrencyKind(ConcurrencyKind.SERIALIZABLE);
		greetingMechanism
				.setPrepareSynchrony(ReplicationSynchrony.MAJORITY_SYNCHRONOUS);
		greetingMechanism
				.setAcceptSynchrony(ReplicationSynchrony.MAJORITY_SYNCHRONOUS);
	}
	protected void simulateSequentialPaxos() {
		simulateBasicPaxos();
		greetingMechanism.setSequentialAccess(true);
		overrideRetry = false;
	}

}
