package co.edu.udea.iot.backend.broker;

import ch.qos.logback.core.net.server.Client;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PetBroker {

    private final IMqttClient client;
    private final String DEFAULT_PUB_TOPIC = "pet_inbound";
    private final String BROKER_URL = "tcp://localhost:1883";

    public PetBroker() throws MqttException {
        String clientId = UUID.randomUUID().toString();
        client = new MqttClient(BROKER_URL, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        //client.subscribe(DEFAULT_PUB_TOPIC , this::processMessage);
    }

    private void processMessage(String topic, MqttMessage message) {
        System.out.printf("%s -> %s", topic, message);
    }

    public void publish(String message) throws MqttException {
        this.publish(this.DEFAULT_PUB_TOPIC, message);
    }

    public void publish(String topic, String message) throws MqttException {
        if (!client.isConnected()) {
            //todo log message client not connected
            return;
        }
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(topic, msg);
    }

    public void listen(String topic, IMqttMessageListener listener) throws MqttException {
        this.client.subscribe(topic, listener);
        System.out.println("Mensaje cualq");
    }

}
