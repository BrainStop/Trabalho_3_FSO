package aulateorioa;

import java.util.concurrent.Semaphore;

public class Main {

	public static void main(String[] args) {
		
		Semaphore olaLock = new Semaphore(1);
		Semaphore mundoRelease = new Semaphore(0);
		
		Ola o = new Ola(olaLock, mundoRelease);
		Mundo m = new Mundo(olaLock, mundoRelease);
		
		m.start();
		o.start();
	}
	
}
