package com.fmi.mpr.hw.chat.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.imageio.ImageIO;

public final class MessageUtil {
	
	public static void sendTextMessage(String textMessage, InetAddress group, int port, DatagramSocket socket) {
		byte[] bytes = textMessage.getBytes();
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, group, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("Couldn't send the message. Problem: " + e.getMessage());
		}
	}
	
	public static void sendImage(String name, String message, InetAddress group, int port, DatagramSocket socket) {		
		sendTextMessage(message, group, port, socket);
		
		String imagePath = message.substring(name.length() + 2, message.length());
		File image = new File(imagePath);
		
		try (InputStream is = new FileInputStream(image)) {
			byte[] byteBuffer = new byte[is.available()];
			is.read(byteBuffer);
		    DatagramPacket packet = new DatagramPacket(byteBuffer, byteBuffer.length, group, port);
			socket.send(packet);
		} catch(IOException e) {
			System.out.println("Couldn't send the image. Problem: " + e.getMessage());
		}
		
		/*sendTextMessage(message, group, port, socket);

		
		
		String imagePath = message.substring(name.length() + 2, message.length());
		File image = new File(imagePath);	
		int readBytes = 0;
		
		try(InputStream is = new FileInputStream(image)) {
			byte[] lengthBytes = new byte[2048];
			int length = 0;
			while((readBytes = is.read(lengthBytes, 0, 2048)) > 0) {
				length += readBytes;
			}
			byte[] lengthInBytes = new String(length + "").getBytes();
			DatagramPacket packet = new DatagramPacket(lengthInBytes, lengthInBytes.length, group, port);
			socket.send(packet);
		} catch(IOException e) {
			
		}
		
		try (InputStream is = new FileInputStream(image)) {
			byte[] byteBuffer = new byte[2048];
			while((readBytes = is.read(byteBuffer, 0, 2048)) > 0) {
				DatagramPacket packet = new DatagramPacket(byteBuffer, readBytes, group, port);
				packet.setData(byteBuffer, 0, readBytes);
				socket.send(packet);
			}
		} catch(IOException e) {
			System.out.println("Couldn't send the image. Problem: " + e.getMessage());
		}*/
		
	}
	
	public static void sendVideo(String name, String message, InetAddress group, int port, DatagramSocket socket) {
		sendTextMessage(message, group, port, socket);
		
		String imagePath = message.substring(name.length() + 2, message.length());
		File image = new File(imagePath);
		int readBytes = 0;
		
		try (InputStream is = new FileInputStream(image)) {
			byte[] byteBuffer = new byte[2048];
			while((readBytes = is.read(byteBuffer, 0, 2048)) > 0) {
				DatagramPacket packet = new DatagramPacket(byteBuffer, readBytes, group, port);
				packet.setData(byteBuffer, 0, readBytes);
				socket.send(packet);
			}
		} catch(IOException e) {
			System.out.println("Couldn't send the video. Problem: " + e.getMessage());
		}
	}

}
