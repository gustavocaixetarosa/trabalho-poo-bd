CREATE DATABASE cobranca;
USE cobranca;

CREATE TABLE cliente (
	  id BIGINT auto_increment primary key,
    nome VARCHAR(50) not NULL,
    registro VARCHAR(15) not NULL UNIQUE,
    endereco VARCHAR(100)
);

CREATE TABLE contrato (
	id BIGINT auto_increment primary key,
  idCliente BIGINT,
  nome VARCHAR(50) not NULL,
  registro VARCHAR(15) not NULL UNIQUE,
  dataInicio DATE,
  duracaoEmMeses INT not NULL,
  valorContrato DOUBLE not NULL,
  FOREIGN KEY (idCliente) REFERENCES cliente(id)
)

CREATE TABLE pagamento (
  id BIGINT auto_increment primary key,
  valor DOUBLE not NULL,
  numParcela INT not NULL,
  dataVencimento DATE not NULL,
  dataPagamento DATE,
  situacao ENUM('PENDENTE', 'PAGO', 'ATRASADO') not NULL,
  idContrato BIGINT,
  FOREIGN KEY (idContrato) REFERENCES contrato(id)
);

CREATE TABLE relatorioMensal (
  id BIGINT auto_increment primary key,
  idCliente BIGINT,
  mes INT not NULL,
  ano INT not NULL,
  totalPago DOUBLE NOT NULL,
  totalPendente DOUBLE NOT NULL,
  totalPagoComAtraso DOUBLE NOT NULL,
  FOREIGN KEY (idCliente) REFERENCES cliente(id)
);


CREATE TABLE meusGanhos (
  id BIGINT auto_increment primary key,
  mes INT not NULL,
  ano INT not NULL,
  totalRecebido DOUBLE NOT NULL
)
