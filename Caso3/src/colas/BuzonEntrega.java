package colas;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrega {

    private  Queue<String> cola = new LinkedList<>();
    private  int capacidadMaxima;
    private  int numServidores;
    private boolean finRecibido = false;

    public BuzonEntrega(int capacidadMaxima, int numServidores) {
        this.capacidadMaxima = capacidadMaxima;
        this.numServidores = numServidores;
    }
    
    public synchronized void guardarCorreo(String correo){
        boolean agregado = false;
        while (!agregado) {
            synchronized (this) {
                if (cola.size() < capacidadMaxima) {
                    cola.add(correo);
                    agregado = true;
                    notify();
                }else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Servidores consumen en espera ACTIVA
    public String entregarCorreo() {
        String correo = null;
        while (correo == null) {
            synchronized (this) {
                if (!cola.isEmpty()) {
                    correo = cola.poll();
                    notify();
                }
            }
        }
        return correo;
    }

    public void enviarFin() {
        synchronized (this) {
            if (finRecibido) return;
            finRecibido = true;
            cola.add("FIN");
            notify();
    }

        esperarVacioYReplicarFin();
    }

    private void esperarVacioYReplicarFin() {
        boolean vacio = false;
        while (!vacio) {
            synchronized (this) {
                vacio = cola.isEmpty();
            }
            if (!vacio) {
                try {
                    Thread.sleep(50); // Espera semiactiva
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        synchronized (this) {
            cola.clear();
            for (int i = 0; i < numServidores; i++) {
                cola.add("FIN");
            }
            notify();
        }
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }
}
