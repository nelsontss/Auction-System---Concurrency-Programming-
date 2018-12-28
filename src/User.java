import java.util.HashMap;
import java.util.Map;

public class User {
    private String user;
    private String pass;
    private Map<String,Reserva> reservas;

    public User(String user, String pass){
        this.user=user;
        this.pass=pass;
        this.reservas = new HashMap<>();
    }

    public double getvDivida() {
        double r = 0.0;
        for(Reserva x : reservas.values()) {
            r += x.getValorPagar();
            System.out.println("Valor a pagar: " + x.getValorPagar());
        }
        return r;
    }

    public void addReserva(String key,Reserva r){
        System.out.println("Reserva : " + key);
        reservas.put(key,r);
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }
}
