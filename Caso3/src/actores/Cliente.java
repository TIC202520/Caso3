package actores;

import colas.BuzonEntrada;

public class Clientes extends Thread{
    private final BuzonEntrada entrada;

    public Clientes(BuzonEntrada entrada){
        this.entrada = entrada;
    }

    @Override
    public void run(){
        try{
            entrada.entregarCorreo();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }

    
}
