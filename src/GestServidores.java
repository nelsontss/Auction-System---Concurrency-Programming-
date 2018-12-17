import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class GestServidores {
    private ReentrantLock usersLock;
    private int nReservas;
    private Map<String,User> users;
    private Map<String,Servidor> servers;
    private Map<String,Servidor> serversLeiao;
    private Map<String,Reserva> reservas;

    public GestServidores(){
        users = new HashMap<String,User>();
        usersLock = new ReentrantLock();
        servers = new HashMap<>();
        serversLeiao = new HashMap<>();
        servers.put("0",new Servidor("0","s0.micro", 0.99,"mirco"));
        servers.put("1",new Servidor("1","s1.micro", 0.99,"mirco"));
        servers.put("2",new Servidor("2","s2.medium", 0.99,"medium"));
        servers.put("3",new Servidor("3","s3.medium", 1.50,"medium"));
        servers.put("4",new Servidor("4","s4.large", 2.30,"large"));
        servers.put("5",new Servidor("5","s5.large", 2.30,"large"));
        serversLeiao.put("0",new Servidor("0","s0L.micro", 0,"mirco"));
        serversLeiao.put("1",new Servidor("1","s1L.micro", 0,"mirco"));
        serversLeiao.put("2",new Servidor("2","s2L.medium", 0,"medium"));
        serversLeiao.put("3",new Servidor("3","s3L.medium", 0,"medium"));
        serversLeiao.put("4",new Servidor("4","s4L.large", 0,"large"));
        serversLeiao.put("5",new Servidor("5","s5L.large", 0,"large"));
        reservas = new HashMap<>();
        nReservas = 0;
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
            nReservas++;
            Servidor s = servers.get(id);
            s.lock();
            usersLock.unlock();
            if (s.isReservado())
                return -1;
            else {
                s.setReservado(true);
                reservas.put(i, new Reserva(LocalDateTime.now(), i, s, users.get(user)));
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
            usersLock.unlock();
            r.terminarReserva();
            return 0;
        }
    }


}
