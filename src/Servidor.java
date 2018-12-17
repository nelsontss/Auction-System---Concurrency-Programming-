import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class Servidor {
    private String id;
    private String nome;
    private double precoNominal;
    private String tipo; //micro //medium //large
    private boolean reservado;
    private ReentrantLock lock;

    public Servidor(String id, String nome, double precoNominal, String tipo) {
        this.id = id;
        this.nome = nome;
        this.precoNominal = precoNominal;
        this.tipo = tipo;
        this.reservado = false;
        this.lock=new ReentrantLock();
    }

    public String getId() {
        return id;
    }

    public double getPrecoNominal() {
        return precoNominal;
    }

    public void lock(){
        lock.lock();
    }

    public void unlock(){
        lock.unlock();
    }

    public boolean isReservado() {
        return reservado;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }
}
