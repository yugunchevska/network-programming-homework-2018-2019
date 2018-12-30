package com.fmi.mpr.hw.chat;

public class Main {

	public static void main(String[] args) {
		MulticastPublisher publisher = new MulticastPublisher();
		publisher.sendMessages();
	}
}