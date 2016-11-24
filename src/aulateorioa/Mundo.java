package aulateorioa;

import java.util.concurrent.Semaphore;

public class Mundo extends Thread {

	Semaphore olaLock;
	Semaphore mundoRelease;
	
	Mundo(Semaphore olaLock,Semaphore mundoRelease) {
		this.olaLock = olaLock;
		this.mundoRelease = mundoRelease;
	}
	
	public void run() {
		while(true) {
			try {
				mundoRelease.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Mundo");
			olaLock.release();
		}
	}
	
}

