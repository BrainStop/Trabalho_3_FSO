package aulateorioa;

import java.util.concurrent.Semaphore;

public class Ola extends Thread {

	Semaphore olaLock;
	Semaphore mundoRelease;
	
	Ola(Semaphore olaLock,Semaphore mundoRelease) {
		this.olaLock = olaLock;
		this.mundoRelease = mundoRelease;
	}
	
	public void run() {
		while(true) {
			try {
				olaLock.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print("ola ");
			mundoRelease.release();
		}
	}
	
}
