package colas;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrada {

    private Queue<String> cola = new LinkedList<>();
    int capacidadMaxima;
    private boolean finRecibido = false;

    public BuzonEntrada(int capacidadMaxima, int numServidores) {
        this.capacidadMaxima = capacidadMaxima;
    }
    
    public synchronized void recibirCorreo(String correo){
        while(cola.size()== capacidadMaxima){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cola.add(correo);
        notify();
    }

    public synchronized String entregarCorreo(){
        while (cola.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
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

