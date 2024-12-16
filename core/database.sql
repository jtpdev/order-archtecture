-- Criação da tabela order_status
CREATE TABLE order_status
(
  id         INT      NOT NULL,
  title      VARCHAR  NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

-- Inserção dos dados na tabela order_status
INSERT INTO order_status (id, title, created_at) VALUES
  (1, 'PENDING', CURRENT_TIMESTAMP),
  (2, 'DUPLICATED', CURRENT_TIMESTAMP),
  (3, 'NO_STOCK', CURRENT_TIMESTAMP),
  (4, 'RECEIVED', CURRENT_TIMESTAMP);

-- Criação da tabela order
CREATE TABLE orders
(
  id          UUID          NOT NULL,
  status_id   INT           NOT NULL,
  date        TIMESTAMP     NOT NULL,
  customer_id UUID          NOT NULL,
  seller_id   UUID          NOT NULL,
  total       DECIMAL(18,2) NOT NULL,
  partial     BOOL          NOT NULL,
  created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT FK_order_status_TO_order FOREIGN KEY (status_id)
    REFERENCES order_status (id) ON DELETE CASCADE
);

-- Criação da tabela order_item
CREATE TABLE order_item
(
  order_id   UUID          NOT NULL,
  item_id    UUID          NOT NULL,
  amount     INT           NOT NULL,
  value      DECIMAL(18,2) NOT NULL,
  total      DECIMAL(18,2) NOT NULL,
  created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP     NULL,
  PRIMARY KEY (order_id, item_id),
  CONSTRAINT FK_order_TO_order_item FOREIGN KEY (order_id)
    REFERENCES "order" (id) ON DELETE CASCADE
);

CREATE INDEX idx_order_status_id ON "order" (status_id);

CREATE INDEX idx_order_customer_id ON "order" (customer_id);

CREATE INDEX idx_order_seller_id ON "order" (seller_id);

CREATE INDEX idx_order_item_order_id ON order_item (order_id);

CREATE INDEX idx_order_item_item_id ON order_item (item_id);

CREATE INDEX idx_order_item_updated_at ON order_item (updated_at);

CREATE INDEX idx_order_date_status ON "order" (date, status_id);
