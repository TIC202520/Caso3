package actores;

import colas.BuzonEntrada;
import colas.BuzonEntrega;
import colas.Cuarentena;
import java.util.Random;

public class FiltroSpam extends Thread {

    private final BuzonEntrada buzonEntrada;
    private final BuzonEntrega buzonEntrega;
    private final Cuarentena buzonCuarentena;

    private final int numClientes;
    private static int numFinesRecibidos = 0;
    private static volatile boolean finGlobalEmitido = false;

    private final Random random = new Random();
    private boolean activo = true;

    public FiltroSpam(BuzonEntrada buzonEntrada, BuzonEntrega buzonEntrega, Cuarentena buzonCuarentena, int numClientes, String nombre) {
        super(nombre);
        this.buzonEntrada = buzonEntrada;
        this.buzonEntrega = buzonEntrega;
        this.buzonCuarentena = buzonCuarentena;
        this.numClientes = numClientes;
    }

    @Override
    public void run() {
        while (activo) {
            if (finGlobalEmitido) {
                break;
            }

            // Espera pasiva del buzón de entrada
            String correo = buzonEntrada.entregarCorreo();

            if (correo == null) continue;

            // Mensaje de inicio
            if (correo.startsWith("INICIO ")) {
                System.out.println(getName() + " detecta inicio de cliente -> " + correo);
            }
            // Cuenta los clientes terminados
            else if (correo.equals("FIN")) {
                boolean deboEmitirFinGlobal = false;

                synchronized (FiltroSpam.class) {
                    numFinesRecibidos++;
                    System.out.println(getName() + " recibe FIN. Total recibidos: " + numFinesRecibidos);
                    if (!finGlobalEmitido && numFinesRecibidos == numClientes) {
                        finGlobalEmitido = true;
                        deboEmitirFinGlobal = true; // lo haremos fuera del synchronized
                    }
                }

        
                // CAMBIO emitir FIN global fuera del lock para no bloquear a otros
                if (deboEmitirFinGlobal) {
                    System.out.println(getName() + " envía mensaje FIN final a entrega y cuarentena.");
                    buzonEntrega.enviarFin();
                    buzonCuarentena.enviarFin();
                    activo = false;
                
                }
            }else {
                boolean esSpam = random.nextBoolean(); // Simula detección de spam aleatoria

                if (esSpam) {
                    // Cuarentena en espera semiactiva
                    System.out.println(getName() + " detecta SPAM -> " + correo);
                    buzonCuarentena.guardarCorreo(correo);
                } else {
                    // Enviar a entrega en espera semiactiva
                    System.out.println(getName() + " entrega correo válido -> " + correo);
                    try {
                        buzonEntrega.guardarCorreo(correo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println(getName() + " finaliza ejecución.");
    }
}
