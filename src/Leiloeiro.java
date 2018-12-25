import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Leiloeiro implements Runnable {
    private Map<String,Servidor> serversLeiao;
    private Map<String,Leilao> leiloes;
    private Map<String,Reserva> reservas;
    private AtomicInteger serversDisponiveis;
    private ReentrantLock leiloesLock;
    private AtomicInteger nReservas;
    private ReentrantLock usersLock;
    private Map<String,User> users;
    private Condition c1;

    public Leiloeiro(Map<String, Servidor> serversLeiao, Map<String, Leilao> leiloes, ReentrantLock lock, Condition c1, ReentrantLock usersLock, AtomicInteger nReservas, Map<String,Reserva> reservas, Map<String,User> users, AtomicInteger serversDisponiveis) {
        this.serversLeiao = serversLeiao;
        this.leiloes = leiloes;
        this.leiloesLock = lock;
        this.reservas = reservas;
        this.nReservas = nReservas;
        this.usersLock = usersLock;
        this.users = users;
        this.c1 = c1;
        this.serversDisponiveis = serversDisponiveis;
    }

    @Override
    public void run() {
        while (true){
            leiloesLock.lock();
            while (serversDisponiveis.intValue() ==  0){
                try {
                    c1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(Servidor s : serversLeiao.values()) {
                if (!s.isReservado()) {
                    Leilao l = new Leilao(s, LocalDateTime.now().plusSeconds(30));
                    leiloes.put(s.getId(), l);
                    new Thread(new FechaLeilao(l,leiloesLock,usersLock,nReservas,reservas,users,c1,leiloes)).start();
                    serversDisponiveis.decrementAndGet();
                }
            }
            leiloesLock.unlock();
        }
    }
}
