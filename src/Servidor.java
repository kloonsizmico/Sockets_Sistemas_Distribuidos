import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
         Servidor s =  new Servidor();
         s.Server();
    }

    static int puerto = 8080; //  definimos el puerto

    ServerSocket server;      // ponemos un nombre a nuestros sockets
    Socket socket;

    DataOutputStream salida; // bufer para armar las salidas o lo que debe responder
    DataInputStream entrada; // bufer para recibir datos

    String mensajeRecibido; // Variable donde guardo lo que llega
    String mensajeRecibidoValor; // Variable donde guardo lo que llega

    int DINEROcuenta = 500000;  // iniciaremos la cuenta del  usuario con un saldo de 500.000$
    public void Server() {

        try {
            server = new ServerSocket(puerto);
            socket = new  Socket();

            System.out.println("Conectando al cliente");
            socket = server.accept();

            entrada = new DataInputStream(socket.getInputStream()); // armar el constructor de recibo
            salida = new DataOutputStream(socket.getOutputStream());// armar constructor de  salida de datos

            String msn = "";

            while (!msn.equals("x")) {

                salida.writeUTF("Bienvenido a nuestro portal de pagos ¿ que transacción deseas hacer ?  \n" +
                        "1) Retiro \n" +
                        "2) Ver saldo actual  \n" +
                        "3) Consignar \n"+
                        "4) Salir"  );// enviamos mensaje de bienvenida y menú para interactuar con el cliente

                mensajeRecibido = entrada.readUTF();// Leemos la  opción escogida por el cliente

                int serverRespuesta = Integer.parseInt(mensajeRecibido); // convertimos la respuesta en un valor númerico

                if (serverRespuesta == 1){ // cliente seleccionó  retirar

                    salida.writeUTF("Ingrese el monto por favor a retirar ");
                    mensajeRecibidoValor = entrada.readUTF();// Leemos monto seleccionado por el cliente

                    int saldoIngresado = Integer.parseInt(mensajeRecibidoValor);

                    if (saldoIngresado > 10000){ // hacemos validación de que solo se pueda ingresar valor mayor a 10.000 $

                        BL bl = new BL(); //  El programa irá a la capa de logica de negocio y hará las operaciones establecidas
                            salida.writeUTF("Transacción realizada satisfactoriamente 👍 \n" +
                                    "Nuevo saldo: "+bl.Retiros(DINEROcuenta,saldoIngresado));
                            // envamos respuesta de la transacción y le diremos el saldo actual

                        mensajeRecibido = entrada.readUTF();// Leemos la opción escogida por si quiere hacer otro retiro


                    }else { // si el cliente  ingresó un valor de retiro menor  a 10.0000 $ , mostrará el error
                        salida.writeUTF("Solo puedes   hacer retiros mayor a 10.000 $");
                    }
                }

                else if (serverRespuesta == 2){  // cliente seleccionó ver saldo actual

                    String saldoIngresado = Integer.toString(DINEROcuenta);
                    salida.writeUTF(saldoIngresado);
                }

                else if (serverRespuesta == 3){ // consignar a otra cuenta
                    salida.writeUTF("A continuación digite   una cuenta valida");

                    mensajeRecibido = entrada.readUTF();// Leemos lo que llega numero
                    mensajeRecibidoValor = entrada.readUTF();// Leemos lo que llega valor  a  consingnar
                    int valorConsignar = Integer.parseInt(mensajeRecibidoValor);


                    BL bl = new BL();
                    salida.writeUTF("Consignación realizada satisfactoriamente 👍 \n" +
                            "Nuevo saldo: "+bl.Retiros(DINEROcuenta,valorConsignar));

                }
                else if (serverRespuesta == 4){
                    JOptionPane.showMessageDialog(null,"Nos vemos ...  👊");
                    socket.close(); // Cierre sesión el cliente
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
