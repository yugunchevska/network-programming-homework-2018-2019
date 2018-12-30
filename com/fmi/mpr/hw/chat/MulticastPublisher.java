package com.fmi.mpr.hw.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import com.fmi.mpr.hw.chat.util.MessageUtil;

public class MulticastPublisher {
	
	private static final int MULTICAST_PORT = 4321;
	private static final String MULTICAST_IP = "230.0.0.0";
	
	private String name = new String();
	private boolean running = true;
	
	public void sendMessages() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please, enter your name:");
		this.name = sc.nextLine();
		
		MulticastReceiver receiver = new MulticastReceiver(name);
		Thread thread = new Thread(receiver);
		thread.start();
		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			InetAddress group = InetAddress.getByName(MULTICAST_IP);
						
			while(running) {
				System.out.println("Type of the message:");
				String type = sc.nextLine().toUpperCase();
				
				System.out.println("Send your message:");
				String message = sc.nextLine();
				String msg = name + ": " + message;
				
				if(type.equals("TEXT")) {
					MessageUtil.sendTextMessage(msg, group, MULTICAST_PORT, socket);
				} else if(type.equals("IMAGE")) {
					MessageUtil.sendImage(name, msg, group, MULTICAST_PORT, socket);
				}
				    
				if(message.equals("end")) {
					running = false;
				}
			}
		} catch(IOException e) {
			System.out.println("Couldn't connect. Problem: " + e.getMessage());
		} finally {
			sc.close();
			
			if(socket != null) {
				socket.close();
			}
		}
	}

}
