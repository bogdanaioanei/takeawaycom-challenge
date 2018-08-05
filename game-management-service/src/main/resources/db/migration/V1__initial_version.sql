CREATE SCHEMA data;

CREATE TABLE data.player (
  id SERIAL PRIMARY KEY,
  name TEXT,
  uuid UUID UNIQUE
);

CREATE TABLE data.game (
  id SERIAL PRIMARY KEY,
  uuid UUID UNIQUE,
  player_uuids UUID[],
  start_number BIGINT,
  started BOOLEAN DEFAULT FALSE,
  finished BOOLEAN DEFAULT FALSE
);

