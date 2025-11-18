package dev.gustavorosa.models;

import java.time.LocalDate;

public class Contrato {
  private Long id;
  private Long idCliente;
  private String nome;
  private String registro;
  private LocalDate dataInicio;
  private int duracaoEmMeses;
  private Double valorContrato;

  public Contrato() {
  }

  public Contrato(Long idCliente, String nome, String registro, LocalDate dataInicio,
      int duracao, Double valorContrato) {
    this.idCliente = idCliente;
    this.nome = nome;
    this.registro = registro;
    this.dataInicio = dataInicio;
    this.duracaoEmMeses = duracao;
    this.valorContrato = valorContrato;
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

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  public LocalDate getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }

  public int getDuracaoEmMeses() {
    return duracaoEmMeses;
  }

  public void setDuracaoEmMeses(int duracaoEmMeses) {
    this.duracaoEmMeses = duracaoEmMeses;
  }

  public Double getValorContrato() {
    return valorContrato;
  }

  public void setValorContrato(Double valorContrato) {
    this.valorContrato = valorContrato;
  }
}
