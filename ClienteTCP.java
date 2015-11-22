package TrabalhosPraticos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClienteTCP {
	
	static long tempo = System.currentTimeMillis();
	//static String id = new String("" + tempo);
	static String id = "1234568";
	

    public static void main(String[] args) {

        String host = "localhost";  // M�quina onde reside a aplica��o servidora
        int    port = 80;         // Porto da aplica��o servidora

        Socket socket     = null;
        BufferedReader is = null;
        PrintWriter    os = null;

        try {
            socket = new Socket(host, port);

            // Mostrar os parametros da liga��o
            System.out.println("Liga��o: " + socket);
            System.out.println("Endere�o do Servidor: "
                    + socket.getInetAddress() + " Porto: " + socket.getPort());
            System.out.println("Endere�o Local: " + socket.getLocalAddress()
                    + " Porto: " + socket.getLocalPort());

            // Stream para escrita no socket
            os = new PrintWriter(socket.getOutputStream(), true); 

            // Stream para leitura do socket
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Escreve no socket
            os.println(id+";connect;");

            // Mostrar o que se recebe do socket
            System.out.println("Recebi -> " + is.readLine()); 
            
        } 
        // ---------------------
        //   Socket Exceptions
        // ---------------------
        // A maioria dos m�todos da class Socket reportam anomalias atrav�s das 
        // excep��es IOException ou a sub classe java.net.SocketException. 
        // No entanto, existem outras excep��es, que extendem destas classes, 
        // que permitem determinar com maior precis�o a causa dos problemas.
        // O conjunto de classes de excep��es originadas pelos m�todos socks s�o:
        //
        //      public class SocketException extends IOException
        //
        //      public class BindException extends SocketException
        //
        //      public class ConnectException extends SocketException
        //
        //      public class NoRouteToHostException extends SocketException
        //
        //      public class UnknownHostException extends IOException
        //
        //      public class ProtocolException extends IOException
        //
        catch (UnknownHostException e) { 
            System.err.println("M�quina " + host + " desconhecida: " + e.getMessage());
        } 
        catch (IOException e) {
            System.err.println("Erro na liga��o: " + e.getMessage());
        }
        finally {
            // No fim de tudo, fechar os streams e o socket
            try {
              if (os != null) os.close();
              if (is != null) is.close();
              if (socket != null ) socket.close();
            }
            catch (IOException e) { 
                // if an I/O error occurs when closing this socket
            }
        } // end finally
    } // end main
}