package gipc.customization;

import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import inputport.rpc.duplex.DuplexReceivedCallInvokerSelector;
import inputport.rpc.duplex.DuplexSentCallCompleterSelector;
import inputport.rpc.duplex.SynchronousDuplexReceivedCallInvokerSelector;
import port.trace.buffer.BufferTraceUtility;
import port.trace.objects.ReceivedMessageDequeued;
import port.trace.objects.ReceivedMessageQueueCreated;
import port.trace.objects.ReceivedMessageQueued;
import port.trace.rpc.CallReceived;
import port.trace.rpc.RPCTraceUtility;
import serialization.LogicalBinarySerializerFactory;
import serialization.LogicalTextualSerializerFactory;
import serialization.SerializerSelector;
import util.trace.ImplicitKeywordKind;
import util.trace.TraceableInfo;
import util.trace.Tracer;
import examples.gipc.counter.layers.AMultiLayerCounterClient;

public class ACustomCounterClient extends AMultiLayerCounterClient{
	
	public static void setFactories() {
//		DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(
//				new ACustomDuplexReceivedCallInvokerFactory());
//		DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(
//				new AnAsynchronousCustomDuplexReceivedCallInvokerFactory());
//		DuplexSentCallCompleterSelector.setDuplexSentCallCompleterFactory(
//				new ACustomSentCallCompleterFactory());
//		DuplexObjectInputPortSelector.setDuplexInputPortFactory(
//				new ACustomDuplexObjectInputPortFactory());
		SerializerSelector.setSerializerFactory(new LogicalTextualSerializerFactory());	
	}
	public static void main (String[] args) {
//		BufferTraceUtility.setTracing();
		RPCTraceUtility.setTracing();
		
		Tracer.showInfo(true);
		Tracer.setDisplayThreadName(true); 
		TraceableInfo.setPrintTraceable(true);
		TraceableInfo.setPrintSource(true);
		
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_CLASS_NAME);
		Tracer.setKeywordPrintStatus(ReceivedMessageQueueCreated.class, true);
		Tracer.setKeywordPrintStatus(ReceivedMessageQueued.class, true);
		Tracer.setKeywordPrintStatus(ReceivedMessageDequeued.class, true);
		
		
		
		setFactories();
		init("Client " + System.currentTimeMillis());
		setPort();
//		sendByteBuffers();
//		sendObjects();
		doOperations();	
		while (true) {
			ReceiveReturnMessage aReceivedMessage = gipcRegistry.getRPCClientPort().receive();
			if (aReceivedMessage == null) {
				break;
			}
			System.out.println("Received message:" + aReceivedMessage );
		}
	}
	

}
