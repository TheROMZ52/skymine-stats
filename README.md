# SkyMine Stats

Advanced statistics and live analytics plugin built specifically for **SkyMine**.

### Stack
- Paper 1.21.8
- Java 21
- SQLite
- Async persistence
- Built-in JSON HTTP API

### Tracked statistics
Playtime, joins, quits, kills, deaths, blocks broken/placed, per-material block mining, item pickup/drop/craft/consume, damage dealt/taken and travelled distance.

### Commands
`/skystats` — personal stats

`/skystats top <playtime|kills|deaths|blocks>` — leaderboard

`/skystats save` — queue a save

### API
See [`docs/api.md`](docs/api.md).

### Build
```bash
mvn clean package
```

The shaded plugin is produced in `target/`.