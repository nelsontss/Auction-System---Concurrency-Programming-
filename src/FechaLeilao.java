import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class FechaLeilao implements Runnable{
    private Leilao l;
    private ReentrantLock leiloesLock;
    private ReentrantLock usersLock;
    private AtomicInteger nReservas;
    private Map<String,Reserva> reservas;
    private Map<String,User> users;
    private Map<String,Leilao> leiloes;
    private Condition c1;

    public FechaLeilao(Leilao l, ReentrantLock lock, ReentrantLock usersLock, AtomicInteger nReservas, Map<String,Reserva> reservas, Map<String,User> users, Condition c, Map<String,Leilao> leiloes ) {
        this.l = l;
        this.leiloesLock = lock;
        this.usersLock = usersLock;
        this.nReservas = nReservas;
        this.reservas = reservas;
        this.users = users;
        this.leiloes = leiloes;
        this.c1 = c;
    }


    public int reserva(String user, double preco){
        usersLock.lock();
        String i = String.valueOf(nReservas);
        nReservas.incrementAndGet();
        Servidor s = l.getServer();
        s.lock();
        usersLock.unlock();
        s.setReservado(true);
        s.setPreco(preco);
        Reserva r = new Reserva(LocalDateTime.now(), i, s, users.get(user),1);
        reservas.put(i, r);
        users.get(user).addReserva(i,r);
        leiloes.remove(s.getId());
        s.unlock();
        return Integer.valueOf(i);
    }

    @Override
    public void run() {
        try {
            sleep(l.getDuration());
            String[] r = l.fecharLeilao();
            if(r[0] != "") {
                System.out.println(reserva(r[0], Double.parseDouble(r[1])) + " - " + r[0]);
            }
            else {
                leiloesLock.lock();
                c1.signal();
                leiloesLock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
