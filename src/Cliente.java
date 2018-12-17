import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",12345);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        System.out.println("Bem Vindo ao ServerSelling!!");
        System.out.println("Se ja tem conta faça: login <email> <pass>!");
        System.out.println("Se ainda nao tem entao: register <email> <pass>!");



        Thread listener = new Thread(new ClienteListener(in));
        Thread writer = new Thread(new ClienteWriter(out));
        writer.start();
        listener.start();

        try {
            writer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }


    }
}
