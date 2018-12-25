import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Reserva {
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private boolean terminada;
    private String id;
    private Servidor s;
    private double valorPagar;
    private User user;
    private int tipo; // 0 - reserva normal; 1 - reserva leilao;


    public Reserva(LocalDateTime inicio, String id, Servidor s, User u, int t) {
        this.inicio = inicio;
        this.id = id;
        this.terminada = false;
        this.s = s;
        this.user = u;
        this.tipo = t;
    }

    public double getValorPagar(){
        if(terminada = false)
            return Duration.between(LocalDateTime.now(), inicio).toHours()*s.getPrecoNominal();
        else
            return valorPagar;
    }

    public Servidor terminarReserva(ReentrantLock lock, Condition c1, AtomicInteger servidoresDisponiveis){
        fim = LocalDateTime.now();
        valorPagar = Duration.between(fim, inicio).toHours()*s.getPrecoNominal();
        terminada = true;
        s.setReservado(false);
        if(tipo == 1){
            lock.lock();
            servidoresDisponiveis.incrementAndGet();
            c1.signal();
            lock.unlock();
        }
        return s;
    }


    public Servidor getServer() {
        return s;
    }

    public User getUser() {
        return user;
    }
}
