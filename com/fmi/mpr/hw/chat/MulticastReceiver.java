package com.fmi.mpr.hw.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class MulticastReceiver implements Runnable {
	
	private static final int MULTICAST_PORT = 4321;
	private static final String MULTICAST_IP = "230.0.0.0";
	private boolean running = true;
	private String name = new String();
	
	public void recieveMessages() throws IOException {
		
		MulticastSocket socket = null;
		InetAddress group = null;
		try {
		    socket = new MulticastSocket(MULTICAST_PORT);
		    group = InetAddress.getByName(MULTICAST_IP);
		    socket.joinGroup(group);
		    
		    Scanner sc = new Scanner(System.in);
		    System.out.println("Please, enter your name:");
		    name = sc.nextLine();
		    
		    MulticastPublisher publisher = new MulticastPublisher();
		    new Thread(() -> {
				try {
					publisher.sendMessages();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		    
		    while(running) {
		    	System.out.println("Send a message:");
		    	String sendMessage = sc.nextLine();
		    	String msg = name + ": " + sendMessage;
		    	byte[] sendBytes = msg.getBytes();
				DatagramPacket packet = new DatagramPacket(sendBytes, sendBytes.length, group, MULTICAST_PORT);
				socket.send(packet);
		    	
			    byte[] bytes = new byte[1024];
			    packet = new DatagramPacket(bytes, bytes.length);
			    socket.receive(packet);
			    String message = new String(packet.getData()).trim();
			    System.out.println(message);
			    
			    if(message.equals("end")) {
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
	
	public static void main(String[] argv) throws IOException {
		Thread thread = new Thread(new MulticastReceiver());
		thread.start();
	}

}
