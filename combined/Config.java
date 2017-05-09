package combined;

public class Config {
	
	static boolean useSameIPC = true;
	
	public static boolean getUseSameIPC() {
		return Config.useSameIPC;
	}
	
	public static void setUseSameIPC(boolean newVal) {
		Config.useSameIPC = newVal;
	}
	
	static boolean consensusForBroadcast = false;
	
	public static boolean isConsensusForBroadcast() {
		return Config.consensusForBroadcast;
	}
	
	public static void setConsensusForBroadcast(boolean newVal) {
		Config.consensusForBroadcast = newVal;
	}
	
	static boolean consensusForIPC = false;
	
	public static boolean isConsensusForIPC() {
		return Config.consensusForIPC;
	}
	
	public static void setConsensusForIPC(boolean newVal) {
		Config.consensusForIPC = newVal;
	}
	
	static boolean synchronous = false;
	
	public static boolean isSynchronous() {
		return Config.synchronous;
	}
		
	public static void setSynchronous(boolean newVal) {
		Config.synchronous = newVal;
	}
	
	public static class SynchronousObject {
		
		boolean changing = false;
		static SynchronousObject singleton = new SynchronousObject();
		
		private SynchronousObject() {}
		
		public void startSynchro() throws InterruptedException {
			if (Config.isSynchronous()) {	
				synchronized (singleton) {
					while (singleton.changing) {
						System.out.println("start waiting for synchronous");
						singleton.wait();
					}
					System.out.println("finish waiting for synchronous");
					singleton.changing = true;
				}
			}
		}
		
		public void endSynchro() throws InterruptedException {
			if (Config.isSynchronous()) {	
				synchronized (singleton) {
					if (singleton.changing) {
						singleton.changing = false;
						System.out.println("notify for synchronous");
						singleton.notify();
					}
				}
			}
		}
		
		public boolean isChanging() {
			return singleton.changing;
		}
		
		public void setChanging(boolean newVal) {
			singleton.changing = newVal;
		}
	}
	
	public static SynchronousObject getSynchronousObject() {
		return SynchronousObject.singleton;
	}
	
	public static class BroadcastMode {
		
		public enum BroadcastModeVal {
			LOCAL, NONATOMIC, ATOMIC;
		}
		
		BroadcastModeVal mode = BroadcastModeVal.ATOMIC;
		boolean changing = false;
		static BroadcastMode singleton = new BroadcastMode();
		
		private BroadcastMode() {}
		
		public BroadcastModeVal getBroadcastMode() throws InterruptedException {
			if (Config.isConsensusForBroadcast()) {
				synchronized (Config.getBroadcastMode()) {
					while (Config.getBroadcastMode().isChanging()) {
						Config.getBroadcastMode().wait();
					}
					return singleton.mode;
				}	
			} else {
				return singleton.mode;
			}
		}
		
		public void setBroadcastMode(BroadcastModeVal newMode) throws InterruptedException {
			if (singleton.changing) {
				synchronized (Config.getBroadcastMode()) {
					while (singleton.changing) {
						System.out.println("Waiting for consensus");
						Config.getBroadcastMode().wait();
					}
				}	
			}
			singleton.mode = newMode;
			if (Config.isConsensusForBroadcast()) {
				singleton.changing = true;
			}
		}
		
		public boolean isChanging() {
			return singleton.changing;
		}
		
		public void setChanging(boolean newVal) {
			singleton.changing = newVal;
		}
	}
	
	public static BroadcastMode getBroadcastMode() {
		return BroadcastMode.singleton;
	}
	
	public static class IPC {
		
		public enum IPCVal {
			NIO, RMI, GIPC, NonAtomicAsync, NonAtomicSync, CentralizedAsync, CentralizedSync, SequentialPaxos;
		}
		
		IPCVal mode = IPCVal.GIPC;
		boolean changing = false;
		static IPC singleton = new IPC();
		
		private IPC() {}
		
		public IPCVal getIPCMode() throws InterruptedException {
			if (Config.isConsensusForIPC()) {
				synchronized (Config.getIPC()) {
					while (Config.getIPC().isChanging()) {
						Config.getIPC().wait();
					}
					Config.getIPC().setChanging(false);
					return singleton.mode;
				}	
			} else {
				return singleton.mode;
			}
		}
		
		public void setIPCMode(IPCVal newMode) throws InterruptedException {
			if (singleton.changing) {
				synchronized (Config.getIPC()) {
					while (singleton.changing) {
						Config.getIPC().wait();
					}
				}	
			}
			singleton.mode = newMode;
			if (Config.isConsensusForIPC()) singleton.changing = true;
		}
		
		public boolean isChanging() {
			return singleton.changing;
		}
		
		public void setChanging(boolean newVal) {
			singleton.changing = newVal;
		}
	}
	
	public static IPC getIPC() {
		return IPC.singleton;
	}
	
	public static void printConfig() throws InterruptedException {
		System.out.println("=================Config=================");
		System.out.println("Broadcast Mode: " + Config.getBroadcastMode().getBroadcastMode());
		System.out.println("Broadcast Mode Consensus: " + Config.consensusForBroadcast);
		System.out.println();
		System.out.println("IPC Mode: " + Config.getIPC().getIPCMode());
		System.out.println("IPC Mode Consensus: " + Config.consensusForIPC);
		System.out.println("========================================");
	}
	
}
