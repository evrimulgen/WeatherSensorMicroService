package io.pivotal.sensor;

import io.pivotal.sensor.messaging.GasSmokeReceiver;
import io.pivotal.sensor.messaging.TempHumidityReceiver;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.netflix.discovery.DiscoveryClient;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
public class WeatherSensorMicroSensorApplication {

	
	private String queueNameWeather;
	private String queueNameGas;
	private String exchange;
	private String routingKeyWeather;
	private String routingKeyGas;
	
//	final static String queueName = "arduino-weather-queue";
//	final static String queueNameGas = "arduino-gas-smoke-queue";
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Bean
	Queue queue() {
		return new Queue(queueNameWeather, true);
	}
	
//	@Bean
//	Queue queueGasSmoke() {
//		return new Queue(queueNameGas, true);
//	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(exchange, true, false);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingKeyWeather);
	}

//	@Bean
//	Binding bindingGas(Queue queueGasSmoke, TopicExchange exchange) {
//		return BindingBuilder.bind(queueGasSmoke).to(exchange).with(routingKeyGas);
//	}
	
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueNameWeather);
		container.setMessageListener(listenerAdapter);
		return container;
	}
	
//	@Bean
//	SimpleMessageListenerContainer containerGas(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapterGas) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		container.setQueueNames(queueNameGas);
//		container.setMessageListener(listenerAdapterGas);
//		return container;
//	}
//	
	@Bean
	TempHumidityReceiver receiver() {
        return new TempHumidityReceiver();
    }
//	
//	@Bean
//	GasSmokeReceiver receiverGas() {
//        return new GasSmokeReceiver();
//    }

	@Bean
	MessageListenerAdapter listenerAdapter(TempHumidityReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
//	@Bean
//	MessageListenerAdapter listenerAdapterGas(GasSmokeReceiver gasSmokeReceiver) {
//		return new MessageListenerAdapter(gasSmokeReceiver, "receiveGasMessage");
//	}
	
    public static void main(String[] args) {
        SpringApplication.run(WeatherSensorMicroSensorApplication.class, args);
    }
    
    @Autowired
    void setEnvironment(Environment e) { //used to test reading of values
    
	 	queueNameWeather = e.getProperty("weather.queueNameWeather");
		queueNameGas = e.getProperty("weather.queueNameGas");
		exchange = e.getProperty("exchangeName");
		routingKeyWeather = e.getProperty("weather.routingKeyWeather");
		routingKeyGas = e.getProperty("weather.routingKeyGas");
	 
    	System.out.println(e.getProperty("weather.queueNameWeather"));
    	System.out.println(e.getProperty("weather.queueNameGas"));
    	System.out.println(e.getProperty("exchangeName"));
    	System.out.println(e.getProperty("weather.routingKeyWeather"));
    	System.out.println(e.getProperty("weather.routingKeyGas"));	
    }

}

@RestController
@RefreshScope
class queueNameRestController {
	
	@Value ("${weather.queueNameWeather}")
	private String weatherQueue;
	
	String weatherQueue() {
		return this.weatherQueue;
	}
	
	@Value ("${weather.queueNameGas}")
	private String gasQueue;
	
	String gasQueue() {
		return this.gasQueue;
	}
	
	@Value ("${exchangeName}")
	private String exchange;
	
	String exchange() {
		return this.exchange;
	}
	
	@Value ("${weather.routingKeyWeather}")
	private String routingKeyWeather;
	
	String routingKeyWeather() {
		return this.routingKeyWeather;
	}
	
	@Value ("${weather.routingKeyGas}")
	private String routingKeyGas;
	
	String routingKeyGas () {
		return this.routingKeyGas;
	}
	
}



