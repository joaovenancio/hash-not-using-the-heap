package com.joaovenancio;

public class PacoteDados <E extends IHash>{
    //Atributos:
    private E dado;
    private int proximoPacote;

    //Construtor:
    public PacoteDados (E novoDado, int proximoPacote) {
        this.dado = novoDado;
        this.proximoPacote = proximoPacote;
    }

    //Metodos:
    public E getDado() {
        return dado;
    }

    public int getProximoPacote() {
        return proximoPacote;
    }

    public void setProximoPacote(int proximoPacote) {
        this.proximoPacote = proximoPacote;
    }

    public void setDado(E dado) {
        this.dado = dado;
    }
}
