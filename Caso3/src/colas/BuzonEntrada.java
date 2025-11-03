package colas;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrada {

    private Queue<String> cola = new LinkedList<>();
    private int capacidadMaxima;

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
        while (cola.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        String correo = cola.poll();
        notify();
        return correo;
    }
    
    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }
}

