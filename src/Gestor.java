public class Gestor {
	
	public static final int SLEEP = 1000;

	private CanalComunicacao cV;

	private CanalComunicacao cE;
	
	private CanalComunicacao cG;
	
	public Gestor() {
		cV = new CanalComunicacao(ConstantesComuns.PATH_VAGUEAR_FILE);
		cE = new CanalComunicacao(ConstantesComuns.PATH_EVITAR_FILE);
		cG = new CanalComunicacao(ConstantesComuns.PATH_GESTOR_FILE);
	}
	
	private void inciarProcessos() {
		cV.enviarMensagem(ConstantesComuns.FAZER_NADA);
		cE.enviarMensagem(ConstantesComuns.FAZER_NADA);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LancadorProcessos.lancar(ConstantesComuns.SRC_VAGUEAR);
		LancadorProcessos.lancar(ConstantesComuns.SRC_EVITAR);
		lerMPGestor();
	}
	
	public void lerMPGestor() {
		if(cG.receberMensagem().equals(ConstantesComuns.ATIVAR_GESTOR)) {
			ativarVaguear();
		} else if(cG.receberMensagem().equals(ConstantesComuns.FAZER_NADA)) {
			fazerNada();
		}
	}
	
	public void fazerNada() {
		cV.enviarMensagem(ConstantesComuns.FAZER_NADA);
		cE.enviarMensagem(ConstantesComuns.FAZER_NADA);
		esperarGestor();
	}
	
	private void esperarGestor(){
		System.out.println("esperarGestor");
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lerMPGestor();
	}
	
	private void ativarVaguear() {
		if(!cG.receberMensagem().equals(ConstantesComuns.FAZER_NADA)) {
			System.out.println("ativarVaguear");
			cV.enviarMensagem(ConstantesComuns.ATIVAR_VAGUEAR);
			lerMPvaguear();
		}
		else lerMPGestor();
	}
	
	private void lerMPvaguear() {
		System.out.println("lerMPvaguear");
		if(cV.receberMensagem().equals(ConstantesComuns.ATIVAR_VAGUEAR)) {
			esperarVaguear();
		}
		else if(cV.receberMensagem().equals(Vaguear.TERMINAR_VAGUEAR)) {
			cV.enviarMensagem(ConstantesComuns.FAZER_NADA);
			ativarEvitar();
		}
	}
	
	private void esperarVaguear(){
		System.out.println("esperarVaguear");
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lerMPvaguear();
	}
	
	private void ativarEvitar() {
		if(!cG.receberMensagem().equals(ConstantesComuns.FAZER_NADA)) {
			System.out.println("ativarEvitar");
			cE.enviarMensagem(ConstantesComuns.ATIVAR_EVITAR);
			lerMPevitar();
		} else lerMPGestor();
	}

	private void lerMPevitar() {
		System.out.println("lerMPEvitar");
		if(cE.receberMensagem().equals(ConstantesComuns.ATIVAR_EVITAR)){
			esperarEvitar();
		}
		else if(cE.receberMensagem().equals(Evitar.TERMINAR_EVITAR)) {
			cE.enviarMensagem(ConstantesComuns.FAZER_NADA);
			ativarVaguear();
		}
	}

	private void esperarEvitar() {
		System.out.println("esperarEvitar");
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lerMPevitar();
	}
	
	public void desligar(){
		System.out.println("desligar");
		System.exit(1);
	}
	
	public void run() {
		inciarProcessos();
	}
	
	public static void main(String[] args) {
		Gestor g = new Gestor();
		g.run();
	}

}
