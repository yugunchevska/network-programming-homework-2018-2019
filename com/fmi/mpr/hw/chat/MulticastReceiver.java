package com.fmi.mpr.hw.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static com.fmi.mpr.hw.chat.Constants.*;

public class MulticastReceiver implements Runnable {

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
			    
			    byte[] bytes = new byte[BUFFER_SIZE];
			    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			    socket.receive(packet);
			    String message = new String(packet.getData()).trim();
			    
			    if(message.startsWith(MESSAGE_TYPE_TEXT)) {
			    	String msg = message.substring(MESSAGE_TYPE_TEXT.length(), message.length());
			    	System.out.println(msg);
			    	
				    if(msg.equals(this.name + ": " + "end")) {
				    	running = false;
				    }
			    } else if(message.startsWith(MESSAGE_TYPE_IMAGE)) {
			    	String msg = message.substring(MESSAGE_TYPE_IMAGE.length(), message.length());
			    	System.out.println(msg);
			    	handleFiles(msg, socket);
			    } else if(message.startsWith(MESSAGE_TYPE_VIDEO)) {
			    	String msg = message.substring(MESSAGE_TYPE_VIDEO.length(), message.length());
			    	System.out.println(msg);
			    	handleFiles(msg, socket);
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
	
	private void handleFiles(String message, MulticastSocket socket) {
		String filename = new File(message.substring(name.length() + 2, message.length())).getName();
		File file = new File(System.getProperty("user.home") + File.separator + filename);
		
		byte[] buffer = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
    	try(OutputStream os = new FileOutputStream(file)) {
    		while(true) {
    			socket.receive(packet);
    			if(new String(packet.getData(), packet.getOffset(), packet.getLength()).equals("END")) {
    				System.out.println("The file is saved to: " + file.getAbsolutePath());
    				break;
    			}
    			os.write(packet.getData(), packet.getOffset(), packet.getLength());
    		}
    	} catch (IOException e) {
    		System.err.println("Couldn't recieve file. Reason: " + e.getMessage());
    	}
	}
}
