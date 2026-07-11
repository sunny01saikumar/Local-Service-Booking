CREATE TABLE chat_messages (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  booking_id UUID NOT NULL REFERENCES bookings(id),
  sender_id UUID NOT NULL REFERENCES users(id),
  message_type VARCHAR(30) NOT NULL,
  content TEXT,
  media_url TEXT,
  latitude NUMERIC(10,7),
  longitude NUMERIC(10,7),
  read_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by UUID REFERENCES users(id),
  updated_by UUID REFERENCES users(id),
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  version BIGINT NOT NULL DEFAULT 0,
  CONSTRAINT chk_chat_msg_type CHECK (message_type IN ('TEXT', 'IMAGE', 'DOCUMENT', 'LOCATION', 'VOICE'))
);

CREATE INDEX idx_chat_booking_time ON chat_messages(booking_id, created_at DESC);
