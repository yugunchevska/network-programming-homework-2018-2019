package com.fmi.mpr.hw.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver implements Runnable {
	
	private static final int MULTICAST_PORT = 4321;
	private static final String MULTICAST_IP = "230.0.0.0";
	private boolean running;
	private String name;
	
	public MulticastReceiver(String name) {
		this.name = name;
		this.running = true;
	}
	
	public void recieveMessages() throws IOException {
		
		MulticastSocket socket = null;
		InetAddress group = null;
		try {
		    socket = new MulticastSocket(MULTICAST_PORT);
		    group = InetAddress.getByName(MULTICAST_IP);
		    socket.joinGroup(group);
		    
		    while(running) {
		    	
			    byte[] bytes = new byte[1024];
			    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			    socket.receive(packet);
			    String message = new String(packet.getData()).trim();
			    System.out.println(message);
			    
			    if(message.equals(this.name + ": " + "end")) {
			    	running = false;
			    }
		    }
		    
		} finally {			
			if(socket != null && group != null) {			
				socket.leaveGroup(group);
				socket.close();
			} else if (socket != null && group == null) {
				socket.close();
			}
			
		}
	}
	
	@Override
	public void run() {
		try {
			recieveMessages();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
