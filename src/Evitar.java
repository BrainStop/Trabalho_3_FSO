/**
 * Codigo fabuloso feito pelo shityWizard
 * @author  Goncalo Olivera
 * @since   1.0
 */
public class Evitar {
	
	public static final String TERMINAR_EVITAR = "TerminarEvitar"; 
	
	/**
	 *  Distância que o robot se deslocará
	 */
	private static final int DIST = -15;
	
	/**
	 * Velocidade estimada que o robot se desloca em milisegundos.
	 */
	private static final double VEL = 0.01;
	
	/**
	 * Raio que o robot utiliza ao curvar
	 */
	private static final int RAIO = 1;
	
	/**
	 * Angulo que o robot utiliza ao curvar
	 */
	private static final int ANGULO = 90;
	
	/**
	 * Identificador do robot que a class irá controlar
	 */
	private static final String NOME_ROBO  = "FSO1";
	
	/**
	 * Objeto utilizado para comunicar e controlar o robot
	 */
	private final RobotLego robot;
//	TODO alterar sito
//	private final MyRobotLego robot;


	private CanalComunicacao cE;
	
	private boolean gestor;

	public Evitar() {
		robot = new RobotLego();
//		TODO alterar isto
//		robot = new MyRobotLego();

		cE = new CanalComunicacao(ConstantesComuns.PATH_EVITAR_FILE);
		gestor = false;
	}

	public void run() {
		lerMensagem();
	}
	
	private void lerMensagem() {
		System.out.println("Ler Mensagem");
		System.out.println(cE.receberMensagem());
		switch(cE.receberMensagem()) {
			case ConstantesComuns.ATIVAR_EVITAR:
				if (robot.OpenNXT(NOME_ROBO)) {
					robot.SetSensorTouch(Robot.S_2);
					gestor = true;
					evitar();
				}else System.exit(1);
				break;
			case ConstantesComuns.SEM_GESTOR_ATIVAR:
				if (robot.OpenNXT(NOME_ROBO)) {
					robot.SetSensorTouch(Robot.S_2);
					gestor = false;
					evitar();
				}else System.exit(1);
				break;
			default:
				esperarCanal();
		}
	}
	
	private void esperarCanal() {
		try {
			Thread.sleep(Gestor.SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lerMensagem();
	}

	private void lerSensorEmbate() {
		int sensor = robot.Sensor(Robot.S_2);
		System.out.println("LerSensor sensor embate: "+ sensor);
		if (sensor == 0) {
			desconectar();
		} else {
			evitar();
		}
	}
	
	private void evitar() {
		robot.Reta(DIST);
		robot.CurvarEsquerda(RAIO, ANGULO);
		robot.Parar(false);
		
		double dist = (ANGULO*Math.PI)/180 * RAIO;
		
		double td = (dist + Math.abs(DIST))/VEL;
		
		System.out.println("dist =" + dist);
		System.out.println(td);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lerSensorEmbate();
	}

	private void desconectar() {
		System.out.println("Desconectar");
		robot.Parar(true);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(gestor) {
			cE.getAndSet(ConstantesComuns.ATIVAR_EVITAR, TERMINAR_EVITAR);
			robot.CloseNXT();
			lerMensagem();
		} else {
			robot.CloseNXT();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		Evitar e = new Evitar();
		e.run();
	}
	
}
