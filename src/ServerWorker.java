import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ServerWorker implements Runnable {
    private Socket s;
    private GestServidores e;
    private String user;


    public ServerWorker(Socket s, GestServidores e) {
        this.s = s;
        this.e = e;
    }

    public int login(String user, String pass, PrintWriter out){
        if(e.login(user,pass)){
            out.println("Login efetuado");
            out.flush();
            this.user = user;
            return 1;
        }else{
            out.println("Username ou password invalidos");
            out.flush();
            return 0;
        }

    }

    public int register(String user, String pass, PrintWriter out){
        try {
            e.criarConta(user,pass);
            out.println("Registo efetuado");
            out.flush();
            this.user = user;
            return 1;
        } catch (ContaJaExisteException e1) {
            out.println("Conta ja existe");
            out.flush();
            return 0;
        }

    }

    public void reservarServer(String id, PrintWriter out){
         int r = e.reserva(id,user);
         if(r<0) {
             out.println("O server que indicou nao esta disponivel.");
             out.flush();
         }else{
             out.println("Reservou o servidor, o numero da reserva é: "+r);
             out.flush();
         }
    }

    public void libertaServer(String id, PrintWriter out){
        int r = e.liberta(id,this.user);
        if(r<0) {
            out.println("O server que indicou nao esta reservado por si.");
            out.flush();
        }else{
            out.println("Acabou de libertar o servidor!");
            out.flush();
        }
    }

    public void licitarServer(String id, String bid, PrintWriter out){
        try {
            int r = e.licitar(id,this.user,Double.parseDouble(bid));
            if(r == 0){
                out.println("Licitação efetuada.");
                out.flush();
            }
            if(r == -1){
                out.println("O servidor nao estao disponivel.");
                out.flush();
            }
            if(r == 1){
                out.println("Licitação insuficiente.");
                out.flush();
            }
        }catch (NumberFormatException e) {
            out.println("O preço que indicou não é valido.");
            out.flush();
        }

    }

    public void consultarDivida(PrintWriter out){
        double div = e.consultarDivida(this.user);
        out.println("A sua divida é: " + div + " €");
        out.flush();
    }
    
       public void mostrarCatalogo (PrintWriter out) {
        try {
            for (Map.Entry<String,Double> e : e.getServers().entrySet()) {
                    out.println(e.getKey() + " - "  + e.getValue() + "." );
            }
            out.flush();
        }
        catch (NumberFormatException e) {
        out.println("Não há servidores disponíveis no catálogo");
        out.flush();
    }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());

            int flag = 0;
            String x;
            while(flag == 0 && (x=in.readLine())!=null){
                String[] cmd = x.split("\\s+");
                System.out.println(x);
                switch (cmd[0]){
                    case "login": flag=login(cmd[1],cmd[2],out);break;
                    case "register": flag=register(cmd[1],cmd[2],out);break;
                }
            }

            System.out.println("logado");

            while((x=in.readLine())!=null) {
                String[] cmd = x.split("\\s+");

                switch (cmd[0]){
                    case "reservarServer":reservarServer(cmd[1],out);break;
                    case "libertarServer":libertaServer(cmd[1],out);break;
                    case "licitarServer":licitarServer(cmd[1],cmd[2],out);break;
                    case "consultarDivida": consultarDivida(out);break;
                    case "mostrarCatalogo":mostrarCatalogo(out);break;
                    default:out.println("Erro: Comando Invalido!");out.flush();break;
                }
            }

            s.shutdownInput();
            s.shutdownOutput();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
