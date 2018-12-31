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

    public String getNome() {
        return nome;
    }

    public double getPrecoNominal() {
        return precoNominal;
    }

    public String getTipo(){
        return tipo;
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

    public void setPreco(double precoNominal) {
        this.precoNominal = precoNominal;
    }

    public String toString(){
        StringBuilder sb= new StringBuilder();
        sb.append("Id: " + this.getId() + " "); sb.append("Nome: " + this.getNome()+ " "); sb.append("Tipo: " + this.getTipo() + " ");
        return sb.toString();
    }
}
