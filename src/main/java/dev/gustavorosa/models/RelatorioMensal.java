package dev.gustavorosa.models;

public class RelatorioMensal {
    private Long id;
    private Long idCliente;
    private int mes;
    private int ano;
    private Double totalPago;
    private Double totalPendente;
    private Double totalPagoComAtraso;

    public RelatorioMensal() {
    }

    public RelatorioMensal(Long idCliente, int mes, int ano, Double totalPago, 
                          Double totalPendente, Double totalPagoComAtraso) {
        this.idCliente = idCliente;
        this.mes = mes;
        this.ano = ano;
        this.totalPago = totalPago;
        this.totalPendente = totalPendente;
        this.totalPagoComAtraso = totalPagoComAtraso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
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

    public Double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(Double totalPago) {
        this.totalPago = totalPago;
    }

    public Double getTotalPendente() {
        return totalPendente;
    }

    public void setTotalPendente(Double totalPendente) {
        this.totalPendente = totalPendente;
    }

    public Double getTotalPagoComAtraso() {
        return totalPagoComAtraso;
    }

    public void setTotalPagoComAtraso(Double totalPagoComAtraso) {
        this.totalPagoComAtraso = totalPagoComAtraso;
    }
}

