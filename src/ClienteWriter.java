import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class ClienteWriter implements Runnable {
    private PrintWriter out;

    public ClienteWriter(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void run() {
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));

        try {
            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
            String x;
            while((x=sc.readLine())!=null && !x.equals("exit")){
                String[] cmd = x.split("\\s+");

                switch (cmd[0]){
                    case "login": out.println(x);out.flush();break;
                    case "register": out.println(x);out.flush();break;
                    case "reservarServer": out.println(x);out.flush();break;
                    case "libertarServer": out.println(x);out.flush();break;
                    case "licitarServer": out.println(x); out.flush();break;
                    default:System.out.println("Erro: Comando Invalido!");
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}