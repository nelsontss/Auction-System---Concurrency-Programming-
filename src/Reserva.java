import java.time.Duration;
import java.time.LocalDateTime;

public class Reserva {
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private boolean terminada;
    private String id;
    private Servidor s;
    private double valorPagar;
    private User user;


    public Reserva(LocalDateTime inicio, String id, Servidor s, User u) {
        this.inicio = inicio;
        this.id = id;
        this.terminada = false;
        this.s = s;
        this.user = u;
    }

    public double getValorPagar(){
        if(terminada = false)
            return Duration.between(LocalDateTime.now(), inicio).toHours()*s.getPrecoNominal();
        else
            return valorPagar;
    }

    public Servidor terminarReserva(){
        fim = LocalDateTime.now();
        valorPagar = Duration.between(fim, inicio).toHours()*s.getPrecoNominal();
        terminada = true;
        s.lock();
        s.setReservado(false);
        s.unlock();
        return s;
    }

    public User getUser() {
        return user;
    }
}
