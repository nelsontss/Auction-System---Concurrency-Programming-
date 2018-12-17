import java.io.BufferedReader;
import java.io.IOException;

class ClienteListener implements Runnable {
    private BufferedReader in;

    public ClienteListener(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String x = "";
            while ((x=in.readLine()) != null) {
                System.out.println(x);
            }
        }catch (IOException e){
            System.out.println("Ligaçao Interrompida");
        }
    }
}