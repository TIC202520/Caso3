package colas;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrada {

    private Queue<String> cola = new LinkedList<>();
    private int capacidadMaxima;
    private boolean finGlobal = false;

    public BuzonEntrada(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }
    
    public synchronized void guardarCorreo(String correo) throws InterruptedException {
        while(cola.size() == capacidadMaxima){
            wait();}
        cola.add(correo);
        notifyAll();
    }

    public synchronized String entregarCorreo(){
        while (cola.isEmpty()&& !finGlobal) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        if (cola.isEmpty() && finGlobal) {
            return null;
        }
        String correo = cola.poll();
        notifyAll();
        return correo;
    }
    
    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }
    public synchronized void marcarFinGlobal() {
        finGlobal = true;
        notifyAll();
    }
}

