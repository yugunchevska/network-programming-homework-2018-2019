package com.fmi.mpr.hw.chat;

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
	private boolean isVideo;
	private String name;
	private String imageName;
	private String videoName;
	private int imageLength;
	private int videoLength;
	
	public MulticastReceiver(String name) {
		this.name = name;
		this.imageName = new String();
		this.videoName = new String();
		this.running = true;
		this.isImage = false;
		this.isVideo = false;
		this.imageLength = 0;
		this.videoLength = 0;
	}
	
	public void recieveMessages() throws IOException {
		
		MulticastSocket socket = null;
		InetAddress group = null;
		try {
		    socket = new MulticastSocket(MULTICAST_PORT);
		    group = InetAddress.getByName(MULTICAST_IP);
		    socket.joinGroup(group);
		    
		    while(running) {		    
			    
			    if(isImage) {
			    	
			    	byte[] bytes = new byte[400_000];			    	
					DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
					socket.receive(packet);	

			    	File image = new File(System.getProperty("user.home") + File.separator + imageName);			
					try (OutputStream os = new FileOutputStream(image)) {
				        os.write(packet.getData());
					} catch (IOException e) {
			    		System.out.println("Couldn't save image to the computer. Problem: " + e.getMessage());
			    	}
			    	System.out.println("The image is saved to: " + image.getAbsolutePath());
			    	
			    	isImage = false;
			    	continue;
			    	
			    	/*byte[] bytes = new byte[2048];
			    	File video = new File(System.getProperty("user.home") + File.separator + imageName);
					DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
					int receivedBytes = 0;
					try (OutputStream os = new FileOutputStream(video)) {
						do {
						    socket.receive(packet);
					    	os.write(packet.getData());
					    	receivedBytes = packet.getData().length;
					    	System.out.println(receivedBytes);
					    	imageLength -= receivedBytes;
					    	System.out.println(imageLength);
				    	} while(packet != null && receivedBytes > 0 && receivedBytes <= 2048 && imageLength > 2048);
					} catch (IOException e) {
			    		System.out.println("Couldn't save the video to the computer. Problem: " + e.getMessage());
			    	}
			    	System.out.println("The video is saved to: " + video.getAbsolutePath());*/
			    	
			    	//handleFiles(imageName, imageLength, socket);
			    
			    	//isImage = false;
			    	//continue;
			    	
			    } else if (isVideo) {
			    	/*byte[] bytes = new byte[2048];
			    	File video = new File(System.getProperty("user.home") + File.separator + videoName);
					DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
					int receivedBytes = 0;
					try (OutputStream os = new FileOutputStream(video)) {
						do {
						    socket.receive(packet);
					    	os.write(packet.getData());
					    	receivedBytes = packet.getData().length;
					    	System.out.println(receivedBytes);
				    	} while(packet != null && receivedBytes > 0 && receivedBytes <= 2048);
					} catch (IOException e) {
			    		System.out.println("Couldn't save the video to the computer. Problem: " + e.getMessage());
			    	}
			    	System.out.println("The video is saved to: " + video.getAbsolutePath());*/
			    	
			    	handleFiles(videoName, videoLength, socket);
			    
			    	isVideo = false;
			    	continue;
			    }	    
			    
			    byte[] bytes = new byte[2048];
			    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			    socket.receive(packet);
			    String message = new String(packet.getData()).trim();
			    
			    if(message.endsWith(".png") || message.endsWith("jpg")) {
			    	isImage = true;
			    	imageName = new File(message.substring(name.length() + 2, message.length())).getName();
			    	
			    	byte[] length = new byte[516];
			    	packet = new DatagramPacket(length, length.length);
			    	socket.receive(packet);
			    	String msg = new String(packet.getData()).trim();
			    	imageLength = Integer.parseInt(msg);
			    } else if(message.endsWith(".mov")) {
			    	isVideo = true;
			    	videoName = new File(message.substring(name.length() + 2, message.length())).getName();
			    	
			    	byte[] length = new byte[516];
			    	packet = new DatagramPacket(length, length.length);
			    	socket.receive(packet);
			    	String msg = new String(packet.getData()).trim();
			    	videoLength = Integer.parseInt(msg);
			    }
		
			    if(message.startsWith(this.name)) {
			        System.out.println(message);
			    }
			    
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
	
	private void handleFiles(String name, int length, MulticastSocket socket) {
		
    	byte[] bytes = new byte[2048];
    	File file = new File(System.getProperty("user.home") + File.separator + name);
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
		int receivedBytes = 0;
		try (OutputStream os = new FileOutputStream(file)) {
			do {
			    socket.receive(packet);
		    	os.write(packet.getData());
		    	receivedBytes = packet.getData().length;
		    	length -= receivedBytes;
	    	} while(packet != null && receivedBytes > 0 && receivedBytes <= 2048 && length > 2048);
		} catch (IOException e) {
    		System.out.println("Couldn't save the file to the computer. Problem: " + e.getMessage());
    	}
    	System.out.println("The file is saved to: " + file.getAbsolutePath());
		
	}
}
