package com.joaovenancio;

public class HashSemAlocacaoDinamica <E extends IHash>{
    //Atributos:
    private PacoteDados[] dados;
    private int valorHash;
    private int qtdElementos;
    private int primeiroLivre;
    private int ultimoPacoteLivre;

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
        this.ultimoPacoteLivre = this.dados.length-1;
        this.dados[this.dados.length-1].setProximoPacote(-1);
        this.primeiroLivre = qtdGrupos;
    }

    //Metodos:
    public void adicionar (E novoDado) throws RuntimeException {
        if (this.qtdElementos == this.dados.length) {
            throw new RuntimeException("Não tem mais espaço na Hash.");
        }

        int grupo = this.funcaoHash(novoDado.getID());
        if (this.dados[grupo] == null) { //Ver se existe um primeiro valor para o grupo:
            this.dados[grupo] = new PacoteDados(novoDado, -1);
            this.qtdElementos++;
        } else {
            if (this.primeiroLivre != -1) { //Se tiver espaco, adicionar um novo elemento:
                this.dados[this.primeiroLivre].setDado(novoDado);
                int proximoLivre = this.primeiroLivre;
                this.dados[this.primeiroLivre].setProximoPacote(this.dados[grupo].getProximoPacote());
                this.dados[grupo].setProximoPacote(this.primeiroLivre);
                if (this.primeiroLivre == this.ultimoPacoteLivre) { //Se por acaso o primeiro livre for o ultimo livre tambem, deve-se manter as duas variaveis atualizadas:
                    this.ultimoPacoteLivre = -1;
                    this.primeiroLivre = -1;
                } else {
                    this.primeiroLivre = this.dados[proximoLivre].getProximoPacote();
                }
                this.qtdElementos++;
            } else { //Se nao tiver:
                throw new RuntimeException("Não tem mais espaço na Hash.");
            }

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
                this.dados[grupo] = this.dados[this.dados[grupo].getProximoPacote()]; //Dar overwrite
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
                    if (this.ultimoPacoteLivre == -1) { //Se por acaso nao houver mais dados livres:
                        this.ultimoPacoteLivre = this.dados[iteradorIndice].getProximoPacote(); //Colocar o dado vago como ultimo das lista dos dados livres
                        this.primeiroLivre = this.ultimoPacoteLivre;
                    } else { //Caso ainda tiver:
                        this.dados[this.ultimoPacoteLivre].setProximoPacote(this.dados[iteradorIndice].getProximoPacote()); //Fazer o ultimo dadoLivre colcoar com o proximo esse dado que vagou
                        this.ultimoPacoteLivre = this.dados[iteradorIndice].getProximoPacote(); //Colocar o dado vago como ultimo das lista dos dados livres
                    }
                    this.qtdElementos--;
                }
            }
            //Eh nescessario fazer uma ultima verificacao:
            if (this.dados[this.dados[iteradorIndice].getProximoPacote()].getDado().getID() == ID) { //Checar se é o dado que quero excluir
                this.dados[this.dados[iteradorIndice].getProximoPacote()].setDado(null); //Tirar o dado que o pacote carrega
                this.dados[iteradorIndice].setProximoPacote(this.dados[this.dados[iteradorIndice].getProximoPacote()].getProximoPacote()); //Setar o proximo pacote do dado anterior para o pacote que o dado que eu quero exlcuir apontava
                this.dados[this.dados[iteradorIndice].getProximoPacote()].setProximoPacote(-1); //Setar o dado que estou excluindo como -1, para adicionar ao final da lista com os dados livres
                if (this.ultimoPacoteLivre == -1) { //Se por acaso nao houver mais dados livres:
                    this.ultimoPacoteLivre = this.dados[iteradorIndice].getProximoPacote(); //Colocar o dado vago como ultimo das lista dos dados livres
                    this.primeiroLivre = this.ultimoPacoteLivre;
                } else { //Caso ainda tiver:
                    this.dados[this.ultimoPacoteLivre].setProximoPacote(this.dados[iteradorIndice].getProximoPacote()); //Fazer o ultimo dadoLivre colcoar com o proximo esse dado que vagou
                    this.ultimoPacoteLivre = this.dados[iteradorIndice].getProximoPacote(); //Colocar o dado vago como ultimo das lista dos dados livres
                }
                this.qtdElementos--;
            }
        }
    }

    public boolean buscar (int ID) {
        int iteradorBusca = this.funcaoHash(ID);

        if (this.dados[iteradorBusca] == null) {
            return false;
        } else {
            while (this.dados[iteradorBusca].getDado().getID() != ID) { //Passar por cada elemento de cada grupo vendo se existe o elemento com a mesma ID:
                if (this.dados[iteradorBusca].getProximoPacote() == -1) {
                    break;
                }
                iteradorBusca = this.dados[iteradorBusca].getProximoPacote();
            }
            if (this.dados[iteradorBusca].getDado().getID() == ID) { //Se saiu do loop, eh porque os IDs sao iguais ou ele chegou ao final do grupo:
                return true;
            } else {
                return false;
            }
        }
    }

    private int funcaoHash(int ID) {
        return ID % this.valorHash;
    }
}
