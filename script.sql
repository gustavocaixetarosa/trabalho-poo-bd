CREATE USER IF NOT EXISTS 'cobranca_user'@'localhost' IDENTIFIED BY '139150';
GRANT ALL PRIVILEGES ON cobranca.* TO 'cobranca_user'@'localhost';

CREATE USER IF NOT EXISTS 'cobranca_user'@'%' IDENTIFIED BY '139150';
GRANT ALL PRIVILEGES ON cobranca.* TO 'cobranca_user'@'%';

FLUSH PRIVILEGES;

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
  FOREIGN KEY (idCliente) REFERENCES cliente(id) ON DELETE CASCADE
);

CREATE TABLE pagamento (
  id BIGINT auto_increment primary key,
  valor DOUBLE not NULL,
  numParcela INT not NULL,
  dataVencimento DATE not NULL,
  dataPagamento DATE,
  situacao ENUM('PENDENTE', 'PAGO', 'ATRASADO') not NULL,
  idContrato BIGINT,
  FOREIGN KEY (idContrato) REFERENCES contrato(id) ON DELETE CASCADE
);

CREATE TABLE relatorioMensal (
  id BIGINT auto_increment primary key,
  idCliente BIGINT,
  mes INT not NULL,
  ano INT not NULL,
  totalPago DOUBLE NOT NULL,
  totalPendente DOUBLE NOT NULL,
  totalPagoComAtraso DOUBLE NOT NULL,
  FOREIGN KEY (idCliente) REFERENCES cliente(id) ON DELETE CASCADE
);


CREATE TABLE meusGanhos (
  id BIGINT auto_increment primary key,
  mes INT not NULL,
  ano INT not NULL,
  totalRecebido DOUBLE NOT NULL,
  UNIQUE KEY unique_mes_ano (mes, ano)
);

CREATE OR REPLACE VIEW vw_resumo_clientes AS
SELECT 
    c.id AS cliente_id,
    c.nome AS cliente_nome,
    c.registro AS cliente_registro,
    COUNT(DISTINCT ct.id) AS total_contratos,
    COALESCE((SELECT SUM(valorContrato) FROM contrato WHERE idCliente = c.id), 0) AS valor_total_contratos,
    COUNT(DISTINCT p.id) AS total_pagamentos,
    COUNT(DISTINCT CASE WHEN p.situacao = 'PAGO' THEN p.id END) AS pagamentos_pagos,
    COUNT(DISTINCT CASE WHEN p.situacao = 'PENDENTE' THEN p.id END) AS pagamentos_pendentes,
    COUNT(DISTINCT CASE WHEN p.situacao = 'ATRASADO' THEN p.id END) AS pagamentos_atrasados,
    COALESCE(SUM(CASE WHEN p.situacao = 'PAGO' OR p.situacao = 'ATRASADO' THEN p.valor ELSE 0 END), 0) AS total_recebido,
    COALESCE(SUM(CASE WHEN p.situacao = 'PENDENTE' THEN p.valor ELSE 0 END), 0) AS total_pendente
FROM 
    cliente c
    LEFT JOIN contrato ct ON c.id = ct.idCliente
    LEFT JOIN pagamento p ON ct.id = p.idContrato
GROUP BY 
    c.id, c.nome, c.registro;

DELIMITER $$

DROP TRIGGER IF EXISTS tr_atualizar_ganhos_apos_pagamento$$

CREATE TRIGGER tr_atualizar_ganhos_apos_pagamento
AFTER UPDATE ON pagamento
FOR EACH ROW
BEGIN
    DECLARE v_mes INT;
    DECLARE v_ano INT;
    DECLARE v_total DOUBLE DEFAULT 0;
    
    IF NEW.dataPagamento IS NOT NULL 
       AND (NEW.situacao = 'PAGO' OR NEW.situacao = 'ATRASADO')
       AND (OLD.situacao != NEW.situacao OR OLD.dataPagamento IS NULL) THEN
        
        SET v_mes = MONTH(NEW.dataPagamento);
        SET v_ano = YEAR(NEW.dataPagamento);
        
        SELECT IFNULL(SUM(valor), 0) INTO v_total
        FROM pagamento
        WHERE (situacao = 'PAGO' OR situacao = 'ATRASADO')
          AND dataPagamento IS NOT NULL
          AND MONTH(dataPagamento) = v_mes
          AND YEAR(dataPagamento) = v_ano;
        
        INSERT INTO meusGanhos (mes, ano, totalRecebido)
        VALUES (v_mes, v_ano, v_total)
        ON DUPLICATE KEY UPDATE totalRecebido = v_total;
        
    END IF;
END$$

DELIMITER ;
