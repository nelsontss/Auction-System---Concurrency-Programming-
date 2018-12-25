import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Leilao {
    private Servidor s;
    private int nLicitacoes;
    private double vLicitacao;
    private ReentrantLock leilaoLock;
    private String licitador;
    private LocalDateTime inicio;
    private LocalDateTime fim;


    public Leilao(Servidor s, LocalDateTime fim) {
        this.s = s;
        this.licitador = "";
        this.nLicitacoes = 0;
        this.vLicitacao = 0.0;
        this.leilaoLock = new ReentrantLock();
        inicio = LocalDateTime.now();
        this.fim = fim;
    }

    public void licitar(String user, double v) throws LicitacaoInsuficienteException {
        System.out.println(v + " <= " + vLicitacao);
        if(v<=vLicitacao)
            throw new LicitacaoInsuficienteException();
        else {
            licitador = user;
            nLicitacoes++;
            vLicitacao = v;
        }
    }

    public String[] fecharLeilao(){
        leilaoLock.lock();
        String[] r = {licitador,String.valueOf(vLicitacao)};
        leilaoLock.unlock();
        return r;
    }

    public void lock(){
        leilaoLock.lock();
    }

    public void unlock(){
        leilaoLock.unlock();
    }

    public long getDuration(){
        return inicio.until( fim, ChronoUnit.MILLIS);
    }

    public Servidor getServer() {
        return s;
    }
}
