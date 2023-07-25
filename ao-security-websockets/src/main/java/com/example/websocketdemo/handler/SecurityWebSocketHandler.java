package com.example.websocketdemo.handler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.websocketdemo.model.SurveillanceGossipMessage;
import com.example.websocketdemo.model.SurveillanceGossipMessage.SeverityType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.apporchid.flinkcep.monitor.LenelAccessMonitor;

@Component
public class SecurityWebSocketHandler extends TextWebSocketHandler{

	static Set<WebSocketSession> sessions = new HashSet<WebSocketSession>();

	public SecurityWebSocketHandler() {
		super();
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		// TODO Auto-generated method stub
		super.handleBinaryMessage(session, message);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		super.handleTextMessage(session, message);
		for(WebSocketSession aSession: sessions) {
			if(aSession.isOpen()) {
				StringBuffer buffer = new StringBuffer();
				for(SurveillanceGossipMessage s : LenelAccessMonitor.broker) {
					buffer.append(new ObjectMapper().writeValueAsString(s));
				}
				session.sendMessage(new TextMessage(buffer.toString()));
			}
		} 

	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		// TODO Auto-generated method stub
		super.handlePongMessage(session, message);
		
		for(WebSocketSession aSession: sessions) {
			if(aSession.isOpen()) {
				StringBuffer buffer = new StringBuffer();
				for(SurveillanceGossipMessage s : LenelAccessMonitor.broker) {
					buffer.append(new ObjectMapper().writeValueAsString(s));
				}
				session.sendMessage(new TextMessage(buffer.toString()));
			}
		} 

		
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		super.handleTransportError(session, exception);
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return super.supportsPartialMessages();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessions.add(session);
		/*StringBuffer buffer = new StringBuffer();
		for(SurveillanceGossipMessage s : LenelAccessMonitor.broker) {
			buffer.append(new ObjectMapper().writeValueAsString(s));
		}
		session.sendMessage(new TextMessage(buffer.toString()));*/
	}

	static {
		new Thread() {
			public void run() {
				while(true) {
					SurveillanceGossipMessage gossipMessage = new SurveillanceGossipMessage();
					gossipMessage.setContent("Event occured - "+RandomUtils.nextDouble() );
					gossipMessage.setSeverityType(SeverityType.MONITOR);
					gossipMessage.setSender(String.valueOf(RandomUtils.nextDouble()));

					LenelAccessMonitor.broker.push(gossipMessage);


					if(sessions!=null) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						for(WebSocketSession session : sessions) {
							StringBuffer buffer = new StringBuffer();
							for(SurveillanceGossipMessage s : LenelAccessMonitor.broker) {
								try {
									buffer.append(new ObjectMapper().writeValueAsString(s));
								} catch (JsonProcessingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							try {
								session.sendMessage(new TextMessage(buffer.toString()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			};
		}.start();
	}


	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
		for(WebSocketSession aSession: sessions) {
			if(aSession.isOpen()) {
				StringBuffer buffer = new StringBuffer();
				for(SurveillanceGossipMessage s : LenelAccessMonitor.broker) {
					buffer.append(new ObjectMapper().writeValueAsString(s));
				}
				session.sendMessage(new TextMessage(buffer.toString()));
			}
		} 
	}


	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		sessions.remove(session);
	}
}