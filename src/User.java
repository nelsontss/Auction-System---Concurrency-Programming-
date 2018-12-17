import java.util.Map;

public class User {
    private String user;
    private String pass;
    private Map<String,Reserva> reservas;

    public User(String user, String pass){
        this.user=user;
        this.pass=pass;
    }

    public double getvDivida() {
        double r = 0.0;
        for(Reserva x : reservas.values())
            r += x.getValorPagar();
        return r;
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }
}
