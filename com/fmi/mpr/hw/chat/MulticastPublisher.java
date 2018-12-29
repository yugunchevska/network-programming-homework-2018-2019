package com.fmi.mpr.hw.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MulticastPublisher {
	
	private static final int MULTICAST_PORT = 4321;
	private static final String MULTICAST_IP = "230.0.0.0";
	
	public void sendMessages() throws IOException {
		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			InetAddress group = InetAddress.getByName(MULTICAST_IP);
						
			while(true) {
				byte[] receivedBytes = new byte[1024];
			    DatagramPacket packet = new DatagramPacket(receivedBytes, receivedBytes.length);
			    socket.receive(packet);
			    String message = new String(packet.getData()).trim();
				
				byte[] bytes = message.getBytes();
				packet = new DatagramPacket(bytes, bytes.length, group, MULTICAST_PORT);
				socket.send(packet);
			}
		} finally {
			if(socket != null) {
				socket.close();
			}
		}
	}
	
	public static void main(String[] argv) throws IOException {
		MulticastPublisher publisher = new MulticastPublisher();
		publisher.sendMessages();
	}

}
