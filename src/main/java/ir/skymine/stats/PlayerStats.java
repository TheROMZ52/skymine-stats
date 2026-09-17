package ir.skymine.stats;

import java.util.UUID;

public final class PlayerStats {
    private final UUID uuid;
    private String name;
    private long firstSeen;
    private long lastSeen;
    private long playtimeSeconds;
    private long joins;
    private long quits;
    private long kills;
    private long deaths;
    private long blocksBroken;
    private long blocksPlaced;
    private long itemsPicked;
    private long itemsDropped;
    private long itemsCrafted;
    private long itemsConsumed;
    private double damageDealt;
    private double damageTaken;
    private double distance;

    public PlayerStats(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.firstSeen = System.currentTimeMillis();
        this.lastSeen = this.firstSeen;
    }

    public synchronized void load(String name, long firstSeen, long lastSeen, long playtimeSeconds, long joins, long quits,
                                   long kills, long deaths, long blocksBroken, long blocksPlaced, long itemsPicked,
                                   long itemsDropped, long itemsCrafted, long itemsConsumed, double damageDealt,
                                   double damageTaken, double distance) {
        this.name = name; this.firstSeen = firstSeen; this.lastSeen = lastSeen; this.playtimeSeconds = playtimeSeconds;
        this.joins = joins; this.quits = quits; this.kills = kills; this.deaths = deaths; this.blocksBroken = blocksBroken;
        this.blocksPlaced = blocksPlaced; this.itemsPicked = itemsPicked; this.itemsDropped = itemsDropped;
        this.itemsCrafted = itemsCrafted; this.itemsConsumed = itemsConsumed; this.damageDealt = damageDealt;
        this.damageTaken = damageTaken; this.distance = distance;
    }

    public synchronized void join(String name) { this.name = name; this.joins++; this.lastSeen = System.currentTimeMillis(); }
    public synchronized void quit() { this.quits++; this.lastSeen = System.currentTimeMillis(); }
    public synchronized void addPlaytime(long s) { playtimeSeconds += Math.max(0, s); }
    public synchronized void kill() { kills++; }
    public synchronized void death() { deaths++; }
    public synchronized void breakBlock() { blocksBroken++; }
    public synchronized void placeBlock() { blocksPlaced++; }
    public synchronized void pickup(long n) { itemsPicked += Math.max(0, n); }
    public synchronized void drop(long n) { itemsDropped += Math.max(0, n); }
    public synchronized void craft(long n) { itemsCrafted += Math.max(0, n); }
    public synchronized void consume(long n) { itemsConsumed += Math.max(0, n); }
    public synchronized void damageDealt(double n) { damageDealt += Math.max(0, n); }
    public synchronized void damageTaken(double n) { damageTaken += Math.max(0, n); }
    public synchronized void distance(double n) { distance += Math.max(0, n); }
    public synchronized UUID uuid() { return uuid; }
    public synchronized String name() { return name; }
    public synchronized long firstSeen() { return firstSeen; }
    public synchronized long lastSeen() { return lastSeen; }
    public synchronized long playtimeSeconds() { return playtimeSeconds; }
    public synchronized long joins() { return joins; }
    public synchronized long quits() { return quits; }
    public synchronized long kills() { return kills; }
    public synchronized long deaths() { return deaths; }
    public synchronized long blocksBroken() { return blocksBroken; }
    public synchronized long blocksPlaced() { return blocksPlaced; }
    public synchronized long itemsPicked() { return itemsPicked; }
    public synchronized long itemsDropped() { return itemsDropped; }
    public synchronized long itemsCrafted() { return itemsCrafted; }
    public synchronized long itemsConsumed() { return itemsConsumed; }
    public synchronized double damageDealt() { return damageDealt; }
    public synchronized double damageTaken() { return damageTaken; }
    public synchronized double distance() { return distance; }
}
