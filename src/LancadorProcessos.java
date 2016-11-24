import java.io.IOException;

public class LancadorProcessos {


	public static void lancarBloqueante(String srcPro) {
		
		Process p = null;
		
		try{ 
			p = Runtime.getRuntime().exec("java -jar " + srcPro);
			System.out.println("Processo "+srcPro+ " Lançado!");
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		if ( p != null) {
			try{
				p.waitFor();
				System.out.println("Processo Terminado");
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public static void lancar(String srcPro) {
		try{ 
			Runtime.getRuntime().exec( new String[] {"cmd","/c","start","cmd","/k","java -jar \"" + srcPro + "\""});
			System.out.println("Processo "+srcPro+ " Lançado!");
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	
}
