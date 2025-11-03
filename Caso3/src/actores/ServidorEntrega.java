package actores;

import colas.BuzonEntrega;
import java.util.Random;

public class ServidorEntrega extends Thread {
    
    private int idServidor;
    private BuzonEntrega buzonEntrega;
    private Random random = new Random();

    public ServidorEntrega(int idServidor, BuzonEntrega buzonEntrega) {
        super("Servidor-" + idServidor);
        this.idServidor = idServidor;
        this.buzonEntrega = buzonEntrega;
    }

    @Override
    public void run() {
        boolean activo = true;

        while (activo) {
            // Espera ACTIVA
            String mensaje = buzonEntrega.entregarCorreo();

            if (mensaje != null) {
                // Si recibe mensaje FIN, el servidor termina
                if (mensaje.equals("FIN")) {
                    System.out.println(getName() + " recibe FIN y termina su ejecución.");
                    activo = false;
                } 
            
                else if (mensaje.startsWith("correo") || mensaje.startsWith("INICIO")) {
                    System.out.println(getName() + " procesando: " + mensaje);
                    try {
                        int tiempo = 100 + random.nextInt(400);
                        Thread.sleep(tiempo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Espera activa
        }

        System.out.println(getName() + " finalizó correctamente.");
    }
}
