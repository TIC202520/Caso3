public class BuzonEntrada {
    int capacidad;
    int capacidadMaxima;
    public BuzonEntrada(int capacidadMaxima){
        this.capacidad=0;
        this.capacidadMaxima = capacidadMaxima;
    }
    
    public synchronized void recibirCorreo(){
        while(capacidad== capacidadMaxima){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        capacidad++;
    }

    public synchronized void entregarCorreo(){
        capacidad--;
        notify();
    }
}
