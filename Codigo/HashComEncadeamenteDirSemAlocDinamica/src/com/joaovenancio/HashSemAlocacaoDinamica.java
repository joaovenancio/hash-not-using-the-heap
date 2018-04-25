package com.joaovenancio;

public class HashSemAlocacaoDinamica <E extends IHash>{
    //Atributos:
    private PacoteDados[] dados;
    private int valorHash;
    private int qtdElementos;
    private int primeiroLivre;
    private int ultimoDadoLivre;

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
        this.ultimoDadoLivre = this.dados.length-1;
        this.dados[this.dados.length-1].setProximoPacote(-1);
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
            int proximoLivre = this.primeiroLivre;
            this.dados[this.primeiroLivre].setProximoPacote(this.dados[grupo].getProximoPacote());
            this.dados[grupo].setProximoPacote(this.primeiroLivre);
            this.primeiroLivre = proximoLivre;
            this.qtdElementos++;
        }
    }

    public void excluir (int ID) throws RuntimeException {
        int grupo = this.funcaoHash(ID);
        if (this.dados[grupo] == null) {
            throw new RuntimeException("Não existem dados nesse grupo.");
        } else if (this.dados[grupo].getDado().getID() == ID) {
            if (this.dados[grupo].getProximoPacote() == -1 ) {
                this.dados[grupo] = null;
                this.qtdElementos--;
                return;
            } else {
                this.dados[grupo] = this.dados[this.dados[grupo].getProximoPacote()];
                this.qtdElementos--;
                return;
            }
        } else {
            int iteradorIndice = grupo;
            while (this.dados[this.dados[iteradorIndice].getProximoPacote()].getProximoPacote() != -1) {  //Tenho que sempre checar se eh o proximo que devo excluir
                if (this.dados[this.dados[iteradorIndice].getProximoPacote()].getDado().getID() == ID) { //Checar se é o dado que quero excluir
                    this.dados[this.dados[iteradorIndice].getProximoPacote()].setDado(null); //Tirar o dado que o pacote carrega
                    this.dados[iteradorIndice].setProximoPacote(this.dados[this.dados[iteradorIndice].getProximoPacote()].getProximoPacote()); //Setar o proximo pacote do dado anterior para o pacote que o dado que eu quero exlcuir apontava
                    this.dados[this.dados[iteradorIndice].getProximoPacote()].setProximoPacote(-1); //Setar o dado que estou excluindo como -1, para adicionar ao final da lista com os dados livres
                    if (this.ultimoDadoLivre == -1) { //Se por acaso nao houver mais dados livres:
                        this.ultimoDadoLivre = this.dados[iteradorIndice].getProximoPacote(); //Colocar o dado vago como ultimo das lista dos dados livres
                        this.primeiroLivre = this.ultimoDadoLivre;
                    } else { //Caso ainda tiver:
                        this.dados[this.ultimoDadoLivre].setProximoPacote(this.dados[iteradorIndice].getProximoPacote()); //Fazer o ultimo dadoLivre colcoar com o proximo esse dado que vagou
                        this.ultimoDadoLivre = this.dados[iteradorIndice].getProximoPacote(); //Colocar o dado vago como ultimo das lista dos dados livres
                    }
                    this.qtdElementos--;
                }
            }
        }
    }

    private int funcaoHash(int ID) {
        return ID % this.valorHash;
    }
}
