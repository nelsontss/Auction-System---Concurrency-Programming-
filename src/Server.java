import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private GestServidores e;
    private ServerSocket s;

    public Server(GestServidores e, ServerSocket s) {
        this.e = e;
        this.s = s;
    }

    public void start() throws IOException {
        while (true){
            Socket clSock = s.accept();
            Thread worker = new Thread(new ServerWorker(clSock,e));
            worker.start();
        }
    }

    public static void main(String[] args) throws IOException {
        Server s = new Server(new GestServidores(),new ServerSocket(12345));
        s.start();
    }
}
