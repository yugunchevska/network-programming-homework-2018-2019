package com.fmi.mpr.hw.chat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver implements Runnable {
	
	private static final int MULTICAST_PORT = 4321;
	private static final String MULTICAST_IP = "230.0.0.0";
	private boolean running;
	private boolean isImage;
	private String name;
	private String imageName;
	
	public MulticastReceiver(String name) {
		this.name = name;
		this.imageName = new String();
		this.running = true;
		this.isImage = false;
	}
	
	public void recieveMessages() throws IOException {
		
		MulticastSocket socket = null;
		InetAddress group = null;
		try {
		    socket = new MulticastSocket(MULTICAST_PORT);
		    group = InetAddress.getByName(MULTICAST_IP);
		    socket.joinGroup(group);
		    
		    while(running) {
		    	
			    byte[] bytes = new byte[400_000];
			    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			    socket.receive(packet);
			    String message = new String(packet.getData()).trim();
			    
			    if(isImage) {
			    	File image = new File(System.getProperty("user.home") + File.separator + imageName);
			    	try (OutputStream os = new FileOutputStream(image)) {
			    	    os.write(packet.getData());
			    	} catch (IOException e) {
			    		System.out.println("Couldn't save image to the computer. Problem: " + e.getMessage());
			    	}
			    	System.out.println("The image is saved to: " + image.getAbsolutePath());
			    	
			    	isImage = false;
			    	break;
			    }
			    
			    if(message.endsWith("png")) {
			    	isImage = true;
			    	imageName = new File(message.substring(name.length() + 2, message.length())).getName();
			    }
			    
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
