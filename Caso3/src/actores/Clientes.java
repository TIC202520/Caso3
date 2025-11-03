package actores;

import colas.BuzonEntrada;

public class Clientes extends Thread{
    private final BuzonEntrada entrada;
    private final int kMensajes;

    public Clientes(BuzonEntrada entrada, int kMensajes){
        this.entrada = entrada;
        this.kMensajes = kMensajes;
    }

    @Override
    public void run(){
        try{
            entrada.entregarCorreo("INICIO");

            for (int i = 1; i <= kMensajes; i++) {
                entrada.guardarCorreo("MSG " + i);
            }
            entrada.guardarCorreo("FIN");
            
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }


}
    

