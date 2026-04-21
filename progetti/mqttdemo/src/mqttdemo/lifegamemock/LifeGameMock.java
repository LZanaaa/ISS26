package lifegamemock;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.mqtt.MqttSupport;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class LifeGameMock {
	
	private final String MqttBroker = "tcp://localhost:1883";//"tcp://broker.hivemq.com"; //
	private MqttSupport mqttSupport = new MqttSupport();
 	private String inputTopic = "lifegameIn";
	
	public static void main(String args[]) {
		new LifeGameMock().doJob();
		
	}
	
	public void doJob() {
		CommUtils.outyellow("LifeGameMock dojob");
		mqttSupport.connectToBroker("lifegamemock",MqttBroker );
		CommUtils.outyellow("LGM connected");
		mqttSupport.subscribe ( inputTopic, (topic, mqttmsg) -> {
			//Lambda is of type org.eclipse.paho.client.mqttv3.IMqttMessageListener
			String msg            = new String( mqttmsg.getPayload() );
			IApplMessage applMesg = new ApplMessage(msg);
			CommUtils.outmagenta("lifegamemock" + " | Riceve via listener: " + msg );
			if( applMesg.isRequest() ) {
				CommUtils.outred("lifegamemock" + " | WARNING: unable to handle requests " + applMesg);
				System.exit(0);
			}
		});
	}
	
}
