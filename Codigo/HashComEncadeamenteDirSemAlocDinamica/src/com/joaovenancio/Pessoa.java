package com.joaovenancio;

public class Pessoa implements IHash {
    private static int controleID = 0;
    private int ID;
    private String nome;

    //Construtor:
    public Pessoa (String nome) {
        this.nome = nome;
        this.ID = Pessoa.controleID;
        Pessoa.controleID++;
    }


    //Metodos:
    public void dizerOi () {
        System.out.println(this.nome+" de ID="+this.ID+" disse oi!");
    }

    public void setiD (int ID) {
        this.ID = ID;
    }

    @Override
    public int getID() {
        return this.ID;
    }

}
