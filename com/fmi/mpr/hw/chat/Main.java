package com.fmi.mpr.hw.chat;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		MulticastPublisher publisher = new MulticastPublisher();
		publisher.sendMessages();
	}
}