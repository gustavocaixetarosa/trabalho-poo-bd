package dev.gustavorosa.models;

public class MeusGanhos {
    private Long id;
    private int mes;
    private int ano;
    private Double totalRecebido;

    public MeusGanhos() {
    }

    public MeusGanhos(int mes, int ano, Double totalRecebido) {
        this.mes = mes;
        this.ano = ano;
        this.totalRecebido = totalRecebido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Double getTotalRecebido() {
        return totalRecebido;
    }

    public void setTotalRecebido(Double totalRecebido) {
        this.totalRecebido = totalRecebido;
    }
}

