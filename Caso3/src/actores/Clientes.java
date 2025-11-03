package actores;

import colas.BuzonEntrada;

public class Clientes extends Thread{

    private int idCliente;
    private BuzonEntrada entrada;
    private int kMensajes;

    public Clientes(int idCliente, int kMensajes, BuzonEntrada entrada){
        super("Cliente-" + idCliente);
        this.idCliente = idCliente;
        this.entrada = entrada;
        this.kMensajes = kMensajes;
    }

    @Override
    public void run(){
        try{
            entrada.guardarCorreo("INICIO "+ idCliente);

            for (int i = 1; i <= kMensajes; i++) {
                entrada.guardarCorreo("MSG " + i);
                System.out.println(getName() + " envía: " + "MSG " + i);
            }
            entrada.guardarCorreo("FIN");
            
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }


}