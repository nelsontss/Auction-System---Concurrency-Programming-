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
                    case "login": if(cmd.length==3){out.println(x);out.flush();break;}
                    case "register": if (cmd.length==3){out.println(x);out.flush();break;}
                    case "reservarServer": if (cmd.length==2){out.println(x);out.flush();break;}
                    case "libertarServer": if (cmd.length==2){out.println(x);out.flush();break;}
                    case "licitarServer": if (cmd.length==3){out.println(x); out.flush();break;}
                    case "consultarDivida": if (cmd.length==1){out.println(x);out.flush();break;}
                    case "mostrarCatalogo": if (cmd.length==1){out.println(x); out.flush();break;}
                    case "mostrarCatalogoLeiloes": if (cmd.length==1){out.println(x); out.flush();break;}
                    default:System.out.println("Erro: Comando Invalido!");
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
