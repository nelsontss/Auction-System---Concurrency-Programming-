import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class GestServidores {
    private ReentrantLock usersLock;
    private ReentrantLock leiloesLock;
    private Condition c1;
    private AtomicInteger nReservas;
    private Map<String,User> users;
    private AtomicInteger servidoresDisponiveis;
    private Map<String,Servidor> servers;
    private Map<String,Servidor> serversLeiao;
    private Map<String,Leilao> leiloes;
    private Leiloeiro leiloeiro;
    private Map<String,Reserva> reservas;

    public GestServidores(){
        users = new HashMap<>();
        usersLock = new ReentrantLock();
        leiloesLock = new ReentrantLock();
        c1 = leiloesLock.newCondition();
        servers = new HashMap<>();
        serversLeiao = new HashMap<>();
        leiloes = new HashMap<>();
        servers.put("0",new Servidor("0","s0.micro", 0.99,"mirco"));
        servers.put("1",new Servidor("1","s1.micro", 0.99,"mirco"));
        servers.put("2",new Servidor("2","s2.medium", 0.99,"medium"));
        servers.put("3",new Servidor("3","s3.medium", 1.50,"medium"));
        servers.put("4",new Servidor("4","s4.large", 2.30,"large"));
        servers.put("5",new Servidor("5","s5.large", 2.30,"large"));
        serversLeiao.put("0",new Servidor("0","s0L.micro", 0,"mirco"));
        serversLeiao.put("1",new Servidor("1","s1L.medium",0,"medium"));
        reservas = new HashMap<>();
        nReservas = new AtomicInteger(0);
        servidoresDisponiveis = new AtomicInteger(0);
        for(Servidor s : serversLeiao.values())
            servidoresDisponiveis.incrementAndGet();
        leiloeiro = new Leiloeiro(serversLeiao,leiloes,leiloesLock,c1,usersLock,nReservas,reservas,users,servidoresDisponiveis);
        new Thread(leiloeiro).start();
    }

    public boolean login(String user, String pass){
        usersLock.lock();
        boolean r;
        if(!users.containsKey(user))
            r=false;
        else if(users.get(user).getPass().equals(pass))
                 r=true;
             else
                 r=false;
        usersLock.unlock();
        return  r;
    }

    public void criarConta(String user, String pass) throws ContaJaExisteException {
        usersLock.lock();
        if(users.containsKey(user)) {
            usersLock.unlock();
            throw new ContaJaExisteException();
        }
        users.put(user,new User(user,pass));
        usersLock.unlock();
    }

    public int reserva(String id,String user){
        usersLock.lock();
        if(!servers.containsKey(id)) {
            usersLock.unlock();
            return -1;
        }
        else {
            String i = String.valueOf(nReservas);
            nReservas.incrementAndGet();
            Servidor s = servers.get(id);
            s.lock();
            usersLock.unlock();
            if (s.isReservado())
                return -1;
            else {
                s.setReservado(true);
                Reserva r = new Reserva(LocalDateTime.now(), i, s, users.get(user),0);
                reservas.put(i, r);
                users.get(user).addReserva(i,r);
            }
            s.unlock();
            return Integer.valueOf(i);
        }
    }

    public int liberta(String id, String user){
        usersLock.lock();
        if(!reservas.containsKey(id) || !reservas.get(id).getUser().getUser().equals(user)) {
            usersLock.unlock();
            return -1;
        }
        else {
            Reserva r = reservas.get(id);
            Servidor s = r.getServer();
            s.lock();
            usersLock.unlock();
            r.terminarReserva(leiloesLock,c1,servidoresDisponiveis);
            s.unlock();
            return 0;
        }
    }

    public int licitar(String id, String user, Double bid){
        usersLock.lock();
        if(!leiloes.containsKey(id)) {
            usersLock.unlock();
            return -1;
        }else{
            Leilao l = leiloes.get(id);
            l.lock();
            usersLock.unlock();
            try {
                l.licitar(user, bid);
                l.unlock();
                return 0;
            } catch (LicitacaoInsuficienteException e) {
                l.unlock();
                return 1;
            }
        }
    }

    public double consultarDivida(String user){
        return users.get(user).getvDivida();
    }

    public Map<String, Servidor> getServers()  {
        Map<String,Servidor> finalserver=new HashMap<>();
        usersLock.lock();
        for(Map.Entry<String,Servidor> c: servers.entrySet())
            finalserver.put(c.getKey(), c.getValue());
        usersLock.unlock();
        return finalserver;
    }

    public List<Leilao> getLeiloes(){
        usersLock.lock();
        List<Leilao> cs = new ArrayList<Leilao>();
        cs=this.leiloes.values().stream().collect(Collectors.toList());
        usersLock.unlock();
        return cs;
    }
}
