<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:050505,35:171717,70:303030,100:050505&height=190&section=header&text=SkyMine%20Stats&fontSize=54&fontColor=ffffff&fontAlignY=38&desc=DETAILED%20PLAYER%20ANALYTICS%20FOR%20PAPER&descAlignY=62&descSize=13" width="100%" />

<br/>

<img src="https://img.shields.io/badge/PAPER%201.21.8-111111?style=for-the-badge&logo=papermc&logoColor=white" /> <img src="https://img.shields.io/badge/JAVA%2021-111111?style=for-the-badge&logo=openjdk&logoColor=white" /> <img src="https://img.shields.io/badge/SQLITE-111111?style=for-the-badge&logo=sqlite&logoColor=white" /> <img src="https://img.shields.io/github/stars/TheROMZ52/skymine-stats?style=for-the-badge&color=111111&labelColor=050505&logo=github" /> <img src="https://img.shields.io/github/last-commit/TheROMZ52/skymine-stats?style=for-the-badge&color=111111&labelColor=050505" />

<br/>

[English](README.md) · [فارسی](README.fa.md)

### Because “I played a lot” is not exactly a useful leaderboard metric.

<img src="https://skillicons.dev/icons?i=java,maven,sqlite&theme=dark" />

</div>

---

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

---

<div align="center">

<a href="https://github.com/TheROMZ52?tab=repositories"><img src="https://img.shields.io/badge/MORE%20PROJECTS-111111?style=for-the-badge&logo=github&logoColor=white" /></a>

<br/><br/>

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:050505,50:202020,100:050505&height=110&section=footer" width="100%" />

<sub>© TheROMZ52 • Built with code, curiosity, and a suspicious number of tabs.</sub>

</div>
