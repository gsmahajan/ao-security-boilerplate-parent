package com.example.websocketdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

import com.example.websocketdemo.handler.SecurityWebSocketHandler;

import in.apporchid.flinkcep.monitor.LenelAccessMonitor;

@EnableHystrix
@EnableWebSocket
@EnableAutoConfiguration
@SpringBootApplication
public class WebsocketDemoApplication implements WebSocketConfigurer {

	public static void main(String[] args) throws Exception {
		//		ForkJoinPool commonPool = new ForkJoinPool(2);
		//		commonPool.invoke(new Runnable() {
		//			
		//			@Override
		//			public void run() {
		//				LenelAccessMonitor.begin();
		//			}
		//		});


		SpringApplication.run(WebsocketDemoApplication.class, args);
		
		LenelAccessMonitor.begin();
		
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(securityWebSocketHandler(), "/security", "/foo").setAllowedOrigins("*").withSockJS();
	}

	@Bean
	public WebSocketHandler securityWebSocketHandler() {
		return new PerConnectionWebSocketHandler(SecurityWebSocketHandler.class);
	}
}