import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        Cliente c = new Cliente();
        c.client();
    }


    static String HOST = "localhost";  // ip a la cual se conectará
    static int PUERTO = 8080;  //puerto del servidor
    Socket socket;
    DataOutputStream salida;
    DataInputStream entrada;
    String mensajeRecibido;
    String mensajeRecibidoValor;


    public  void  client(){

        try {
            socket = new Socket(HOST, PUERTO);  //LLamar al servidor
            salida = new DataOutputStream(socket.getOutputStream());
            entrada = new DataInputStream(socket.getInputStream());
            String msn = "";


            while (!msn.equals("x")) {

                mensajeRecibido = entrada.readUTF();// Leemos respuesta
                String  entradaPrincipal = JOptionPane.showInputDialog(null,mensajeRecibido);


                salida.writeUTF(entradaPrincipal);// enviamos mensaje

                int opcionEnviado  = Integer.parseInt(entradaPrincipal);

                if (opcionEnviado == 1){ // cliente  Hacer retiro

                    mensajeRecibido = entrada.readUTF();//

                    String  ingresarMonto = JOptionPane.showInputDialog(null,mensajeRecibido); //

                    int valorEnviado  = Integer.parseInt(ingresarMonto);

                    if (valorEnviado > 10000) {
                        salida.writeUTF(ingresarMonto);// enviamos el monto al servidor

                        mensajeRecibidoValor = entrada.readUTF();// Leemos respuesta que nos confirma  el estado del retiro y el valor
                        String  postRetiro = JOptionPane.showInputDialog(null,mensajeRecibidoValor+
                                "\n 🤔  ¿ Que deseas hacer ?  \n" +
                                "1) Hacer  un nuevo retiro \n" +
                                "2) Menú pincipal \n" +
                                "3) Salir");
                        // muestra nuevamente el menú para seleccionar la opción  que deseamos hacer
                        salida.writeUTF(postRetiro);// enviamos mensaje de que vamos hacer después del retiro

                        int opcionFinal  = Integer.parseInt(postRetiro);

                        if (opcionFinal == 3){  // el usuario desea  salir del sistema
                            JOptionPane.showMessageDialog(null,"Vuelve pronto ... 😢");

                            salida.writeUTF("El Usuario conectado  con el HOST: "+HOST+" Y puerto : "+ PUERTO+ " Finalizó sesión" );
                            socket.close();
                        }
                        if (opcionFinal != 3);  // el usuario desea regresar al menú principal

                    }else{ // nos rechaza la transacción debido a que ingresó un monto menor que 10.000$
                        JOptionPane.showMessageDialog(null,"Solo puedes hacer retiros mayor a 10.000 $ 😥");
                    }
                }
                else if (opcionEnviado == 2){ // ver saldo actual

                    mensajeRecibidoValor = entrada.readUTF();// Leemos respuesta de el valor actual del saldo
                    JOptionPane.showMessageDialog(null,"Tu saldo actual es : "+mensajeRecibidoValor);
                }

                else  if (opcionEnviado == 3){ // Hacer consignación a otra cuenta

                    mensajeRecibidoValor = entrada.readUTF();// Leemos la información que nos indica  el servidor
                    JOptionPane.showMessageDialog(null,mensajeRecibidoValor); // se muestra en pantalla dicho mensaje

                    String  numeroCuenta = JOptionPane.showInputDialog(null,"Cuenta a consignar");
                    String  ValorCuenta = JOptionPane.showInputDialog(null,"Valor a consignar");
                    // en estas dos variables  ingresamos los valores que nos indica  el servidor

                    salida.writeUTF(numeroCuenta);// enviamos número cuenta
                    salida.writeUTF(ValorCuenta);// enviamos  valor

                    mensajeRecibido = entrada.readUTF();// Leemos  la respuesta del servidor
                    JOptionPane.showMessageDialog(null,mensajeRecibido); // muestra en pantalla dicha respuesta
                                                                        // sobre la confirmación de la transacción

                }

            }

            socket.close();


        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}
