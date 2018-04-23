package com.joaovenancio;

public class HashSemAlocacaoDinamica <E extends IHash>{
    //Atributos:
    private PacoteDados[] dados;
    private int valorHash;
    private int qtdElementos;
    private int primeiroLivre;

    //Construtor:
    public HashSemAlocacaoDinamica(int qtdGrupos, int espacoDeTratamentoDeColisao) {
        this.dados = new PacoteDados[qtdGrupos+espacoDeTratamentoDeColisao];
        this.valorHash = qtdGrupos;
        this.qtdElementos = 0;
        for (int i = qtdGrupos; i <= qtdGrupos+espacoDeTratamentoDeColisao-1; i++) {
            int encademantoLivres = qtdGrupos++;
            this.dados[i] = new PacoteDados(null,encademantoLivres);
            encademantoLivres++;
        }
        this.dados[this.dados.length-1] .setProximoPacote(-1);
        this.primeiroLivre = qtdGrupos;
    }

    //Metodos:
    public void adicionar (E novoDado) {
        int grupo = this.funcaoHash(novoDado.getID());
        if (this.dados[grupo] == null) {
            this.dados[grupo] = new PacoteDados(novoDado, -1);
            this.qtdElementos++;
        } else {
            this.dados[this.primeiroLivre].setDado(novoDado);
            this.dados[this.primeiroLivre].setProximoPacote(this.dados[grupo].getProximoPacote());
            this.dados[grupo].setProximoPacote(this.primeiroLivre);
            this.primeiroLivre = this.dados[this.primeiroLivre].getProximoPacote();
            this.qtdElementos++;
        }
    }

    public void excluir (int ID) {
        int grupo = this.funcaoHash(ID);
        if (this.dados[grupo].getDado().getID() == ID) {

        }
    }

    private int funcaoHash(int ID) {
        return ID % this.valorHash;
    }
}
