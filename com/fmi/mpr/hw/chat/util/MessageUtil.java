package com.fmi.mpr.hw.chat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.fmi.mpr.hw.chat.Constants.MESSAGE_TYPE_IMAGE;
import static com.fmi.mpr.hw.chat.Constants.BUFFER_SIZE;

public final class MessageUtil {
	
	public static void sendTextMessage(String textMessage, InetAddress group, int port, DatagramSocket socket) {
		byte[] bytes = textMessage.getBytes();
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, group, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Couldn't send the message. Problem: " + e.getMessage());
		}
	}
	
	public static void sendFile(String name, String message, InetAddress group, int port, DatagramSocket socket) {
		sendTextMessage(message, group, port, socket);
		
		String filepath = message.substring(name.length() + MESSAGE_TYPE_IMAGE.length() + 2, message.length());
		File file = new File(filepath);
		
		byte[] bytes = new byte[BUFFER_SIZE];
		
		try(InputStream is = new FileInputStream(file)) {
		    while(is.read(bytes, 0, BUFFER_SIZE) > 0) {
		    	DatagramPacket packet = new DatagramPacket(bytes, bytes.length, group, port);
		    	socket.send(packet);
		    }
		    
		    socket.send(new DatagramPacket("END".getBytes(), "END".length(), group, port));
		} catch (IOException e) {
			System.err.println("Couldn't send file. Problem: " + e.getMessage());
		}
	}

}
