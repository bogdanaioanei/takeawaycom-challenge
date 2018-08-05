CREATE INDEX CONCURRENTLY IF NOT EXISTS player_name_idx ON data.player (name);
CREATE INDEX CONCURRENTLY IF NOT EXISTS player_uuid_idx ON data.player (uuid);
CREATE INDEX CONCURRENTLY IF NOT EXISTS game_uuid_idx ON data.game (uuid);