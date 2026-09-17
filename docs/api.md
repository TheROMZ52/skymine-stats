# SkyMine Stats API

Base URL: `http://HOST:8787/api/v1`

- `GET /status` — online count, max players, tracked players
- `GET /players` — persistent player summaries
- `GET /leaderboard?metric=playtime` — leaderboard

Leaderboard metrics: `playtime`, `kills`, `deaths`, `blocks_broken`, `blocks_placed`.

If `http.require-token` is enabled, send `Authorization: Bearer YOUR_TOKEN`.
