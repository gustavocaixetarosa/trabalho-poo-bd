package dev.gustavorosa.models;

import java.time.LocalDate;

public class Pagamento {
    private Long id;
    private Long idContrato;
    private Double valor;
    private int numParcela;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private SituacaoPagamento situacao;

    public Pagamento() {
    }

    public Pagamento(Long idContrato, Double valor, int numParcela, LocalDate dataPagamento,
                     LocalDate dataVencimento, SituacaoPagamento situacao) {
       this.idContrato = idContrato;
       this.valor = valor;
       this.numParcela = numParcela;
       this.dataPagamento = dataPagamento;
       this.dataVencimento = dataVencimento;
       this.situacao = situacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getNumParcela() {
        return numParcela;
    }

    public void setNumParcela(int numParcela) {
        this.numParcela = numParcela;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public SituacaoPagamento getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoPagamento situacao) {
        this.situacao = situacao;
    }
}
