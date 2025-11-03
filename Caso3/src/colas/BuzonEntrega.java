package colas;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrega {

    private Queue<String> cola = new LinkedList<>();
    private int capacidadMaxima;
    private int numServidores;
    private boolean finRecibido = false;

    public BuzonEntrega(int capacidadMaxima, int numServidores) {
        this.capacidadMaxima = capacidadMaxima;
        this.numServidores = numServidores;
    }
    
     // Productores: espera PASIVA si lleno
    public synchronized void guardarCorreo(String correo) throws InterruptedException {
        while (cola.size() == capacidadMaxima) {
            wait();
        }
        cola.add(correo);
        notifyAll();
    }

    // Servidores consumen en espera ACTIVA
    public String entregarCorreo() {
        String correo;
        for (;;)  {
            synchronized (this) {
                if (!cola.isEmpty()) {
                    correo = cola.poll();
                    notifyAll();
                    return correo;
                }
            }
            Thread.yield();
        }
    }

    public void enviarFin() {
        synchronized (this) {
            if (finRecibido) return;
            finRecibido = true;
            while (!cola.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            for (int i = 0; i < numServidores; i++) {
                cola.add("FIN");
            }
            notifyAll(); // despertar a servidores para que tomen sus FIN
        }
    }


    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }
}
