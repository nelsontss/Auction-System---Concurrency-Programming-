import java.util.concurrent.locks.ReentrantLock;

public class Leilao {
    private Servidor s;
    private int nLicitacoes;
    private double vLicitacao;
    private ReentrantLock leilaoLock;
    private String licitador;

    public Leilao(Servidor s) {
        this.s = s;
        this.licitador = "";
        this.nLicitacoes = 0;
        this.vLicitacao = 0.0;
        this.leilaoLock = new ReentrantLock();
    }

    public void licitar(String user, double v) throws LicitacaoInsuficienteException {
        leilaoLock.lock();
        if(v<=vLicitacao)
            throw new LicitacaoInsuficienteException();
        else {
            licitador = user;
            nLicitacoes++;
            vLicitacao = v;
        }
        leilaoLock.unlock();
    }

    public String[] fecharLeilao(){
        leilaoLock.lock();
        String[] r = {licitador,s.getId()};
        leilaoLock.unlock();
        return r;
    }
}
