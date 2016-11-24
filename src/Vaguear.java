
import java.util.Random;

/**
 * Um processo JAVA que quando ativo, abre o canal de comunicação Bluetooth com
 * o robot, configura o sensor de toque no robot e depois faz com que o robot
 * vagueie pelo espaço até encontrar um obstáculo. Quando o robot encontra
 * um obstáculo, o sensor de toque do robot que normalmente está desativo fica
 * ativo e neste caso, o processo deve parar o robot, fechar o canal 
 * de comunicação e ficar desativo.
 * A definição de vaguear no espaço consiste no robot andar em frente,
 * curvar à direita, curvar à esquerda e parar de forma aleatória.
 *
 * @author Gonçalo Oliveira
 * @author Miguel Marçal
 */
public class Vaguear {
	
	public static final String TERMINAR_VAGUEAR = "TerminarVaguear";
	
	/**
	 * Tempo de amostragem do sensor do robot.
	 */
	private static final int  T_A = 250;
	
	/**
	 * Tempo que o processo adormece quando o robot fica parado com a
	 * instrução "Parar".
	 */
	private static final int T_P = 1000;
	
	/**
	 * Velocidade estimada que o robot se desloca em milisegundos.
	 */
	private static final double VEL = 0.02;
	
	/**
	 * Identificador do robot que a class irá controlar
	 */
	private static final String NOME_ROBO  = "FSO1"; 
	
	/**
	 * Ojecto utilizado para gerar as escolhas do robot aleatoriamente
	 */
	private final Random random; 
	
	/**
	 * Objeto utilizado para comunicar e controlar o robot
	 */
	//TODO Alterar isto
	private final RobotLego robot;
	
	/**
	 * TimeStamp usado para controlar o fluxo de intruções enviadas para
	 * o robot.
	 */
	private double timestamp;
	
	/**
	 * Tempo estimado que o robo demora a realizar certa instrução
	 */
	private double td;

	/**
	 * Canal de Comunicação utilizado para comuinicar com o processo Vaguear
	 */
	private CanalComunicacao cV; 
	
	private boolean gestor;
	
	/**
	 * 
	 */
	private enum Acao {
		frente, parar, esquerda, direita;
		
		private static final Acao[] VALUES = values(); 
		private static final int SIZE = VALUES.length;
		private static final Random RANDOM = new Random();
		
		public static Acao getAcaoAleatoria()  {
			return VALUES[RANDOM.nextInt(SIZE)];
		}
	}

	/**
	 * Construtor inicia as variaveis necessárias para o funcionamento da 
	 * aplicação
	 */
	public Vaguear() {
		robot = new RobotLego();
		random = new Random();			
		cV = new CanalComunicacao(ConstantesComuns.PATH_VAGUEAR_FILE);
		gestor = false;
	}

	/**
	 * Metodo run que é chamado quando o processo é inicializado e chama o 
	 * metodo lerMensagem
	 */
	public void run() {
		lerMensagem();
	}

	/**
	 * Metodo LerMensagem é responsável por ler os comandos recebidos do 
	 * processo Gestor atravez do Canal de Comunicação.
	 * Se nenhuma mensagem for enviada ele passa para o metodo esperar caso 
	 * contrario chama o metodo escolha
	 */
	private void lerMensagem() {
		System.out.println("Ler Mensagem");
		System.out.println(cV.receberMensagem());
		switch(cV.receberMensagem()) {
			case ConstantesComuns.ATIVAR_VAGUEAR:
				if (robot.OpenNXT(NOME_ROBO)) {
					robot.SetSensorTouch(Robot.S_2);
					gestor = true;
					escolha();
				}else System.exit(1);
				break;
			case ConstantesComuns.SEM_GESTOR_ATIVAR:
				if (robot.OpenNXT(NOME_ROBO)) {
					robot.SetSensorTouch(Robot.S_2);
					gestor = false;
					escolha();
				}else System.exit(1);
				break;
			default:
				esperarCanal();
		}
	}
	
	/**
	 * 
	 */
	private void esperarCanal() {
		try {
			Thread.sleep(Gestor.SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lerMensagem();
	}

	private void escolha() {
		System.out.println("Escolha");
		timestamp = System.currentTimeMillis();

		switch (Acao.getAcaoAleatoria()) {
		case frente:
			reta();
			break;
		case direita:
			curvarDir();
			break;
		case esquerda:
			curvarEsq();
			break;
		case parar:
			pararSync();
			break;
		default:
			System.out.println("ERRO Escolha entrou aqui!");
			break;
		}
		lerSensor();
	}

	private void reta() {
		double dist = random.nextInt(66) + 10;
		timestamp = System.currentTimeMillis();
		td = dist/VEL;
		System.out.println("Reta dist:"+dist+" td:"+td);
		
		robot.Reta((int) dist);
	}
	
	private void curvarDir() {
		double raio = random.nextInt(41) + 10;
		double angulo = random.nextInt(91);
		double dist = (angulo*Math.PI)/180 * raio;
		timestamp = System.currentTimeMillis();
		td = dist/VEL;
		System.out.println("CurvaD dist:"+dist+" raio:"+raio+" angulo:"+angulo+" td:"+td);
		
		robot.CurvarDireita((int)raio, (int)angulo);
	}

	private void curvarEsq() {
		double raio = random.nextInt(41) + 10;
		double angulo = random.nextInt(91);
		double dist = (angulo*Math.PI)/180 * raio;
		timestamp = System.currentTimeMillis();
		td = dist/VEL;
		System.out.println("CurvaE dist:"+dist+" raio:"+raio+" angulo:"+angulo+" td:"+td);
		
		robot.CurvarEsquerda((int)raio, (int)angulo);
	}

	private void pararSync() {
		timestamp = System.currentTimeMillis();
		td = T_P;
		System.out.println("Parar td:"+td);
		
		robot.Parar(false);
	}
	
	private void lerSensor() {
		int sensor = robot.Sensor(Robot.S_2);
		System.out.println("LerSensor sensor:"+ sensor);
		
		if (sensor == 0) {
			esperar();
		} else {
			desconectar();
		}
	}

	private void esperar() {
		try {
			System.out.println("Thread sleep");
			Thread.sleep(T_A);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double t = System.currentTimeMillis() - timestamp;
		System.out.println("Testar t:"+t+" td:"+td);
		if (t > td) 
			escolha();
		else
			lerSensor();
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
			cV.getAndSet(ConstantesComuns.ATIVAR_VAGUEAR, TERMINAR_VAGUEAR);
			robot.CloseNXT();
			lerMensagem();
		} else {
			robot.CloseNXT();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		Vaguear v = new Vaguear();
		v.run();
	}

}
