package com.fmi.mpr.hw.chat;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import com.fmi.mpr.hw.chat.util.MessageUtil;

import static com.fmi.mpr.hw.chat.Constants.*;

public class MulticastPublisher {
	
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
				
				if(!type.equals(MESSAGE_TYPE_TEXT) && !type.equals(MESSAGE_TYPE_IMAGE) && !type.equals(MESSAGE_TYPE_VIDEO)) {
					System.out.println("The type of the message is not clear. Try again.");
					continue;
				}
				
				System.out.println("Send your message:");
				String message = sc.nextLine();
				
				if(type.equals(MESSAGE_TYPE_TEXT)) {
					String msg = "TEXT" + name + ": " + message;
					MessageUtil.sendTextMessage(msg, group, MULTICAST_PORT, socket);
				} else if(type.equals(MESSAGE_TYPE_IMAGE)) {
					String msg = "IMAGE" + name + ": " + message;
					MessageUtil.sendFile(name, msg, group, MULTICAST_PORT, socket);
				} else if(type.equals(MESSAGE_TYPE_VIDEO)) {
					String msg = "VIDEO" + name + ": " + message;
					MessageUtil.sendFile(name, msg, group, MULTICAST_PORT, socket);
				}
				    
				if(message.equals("end")) {
					running = false;
				}
				
				Thread.sleep(400);
			}
		} catch(IOException | InterruptedException e) {
			System.err.println("Couldn't connect. Problem: " + e.getMessage());
		} finally {
			sc.close();
			
			if(socket != null) {
				socket.close();
			}
		}
	}

}
