package trabalhosComVitor;

import java.util.Arrays;
import java.util.Random;

public class MasterMind {

	public static void main(String[] args) {
		
		int[] master = {0, 3, 1, 5};
		int[] user = {4, 7, 1, 1};
		
		Random r = new Random();
		
		for(int i = 0; i < master.length; i++) {
			master[i] = r.nextInt(8);
			user[i] = r.nextInt(8);
		}
		
		int deslocados = 0;
		int certos = 0;
		
		for(int i = 0; i < user.length; i++){
			for(int j = 0; j < master.length; j++) {
				if(user[i] == master[j]) {					
					if(user[i] == master[i]) certos++;
					else if(user[j] != master[j]) 
						deslocados++;
					break;
				}
			}
		}
		System.out.println("Master: "+Arrays.toString(master));
		System.out.println("User:   "+Arrays.toString(user));
		System.out.println("deslocados " +deslocados + " | certos: " + certos);
	}
	
}
