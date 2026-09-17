package ir.skymine.stats;

import java.io.File;
import java.sql.*;
import java.util.*;

public final class Database implements AutoCloseable {
    private final Connection connection;
    public Database(File file) throws SQLException {
        file.getParentFile().mkdirs();
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        try (Statement s = connection.createStatement()) {
            s.execute("PRAGMA journal_mode=WAL");
            s.execute("PRAGMA synchronous=NORMAL");
            s.execute("CREATE TABLE IF NOT EXISTS players(uuid TEXT PRIMARY KEY,name TEXT NOT NULL,first_seen INTEGER NOT NULL,last_seen INTEGER NOT NULL,playtime INTEGER NOT NULL,joins INTEGER NOT NULL,quits INTEGER NOT NULL,kills INTEGER NOT NULL,deaths INTEGER NOT NULL,blocks_broken INTEGER NOT NULL,blocks_placed INTEGER NOT NULL,items_picked INTEGER NOT NULL,items_dropped INTEGER NOT NULL,items_crafted INTEGER NOT NULL,items_consumed INTEGER NOT NULL,damage_dealt REAL NOT NULL,damage_taken REAL NOT NULL,distance REAL NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS block_stats(uuid TEXT NOT NULL,material TEXT NOT NULL,count INTEGER NOT NULL,PRIMARY KEY(uuid,material))");
            s.execute("CREATE TABLE IF NOT EXISTS daily(day TEXT NOT NULL,uuid TEXT NOT NULL,playtime INTEGER NOT NULL,joins INTEGER NOT NULL,quits INTEGER NOT NULL,kills INTEGER NOT NULL,deaths INTEGER NOT NULL,blocks_broken INTEGER NOT NULL,blocks_placed INTEGER NOT NULL,PRIMARY KEY(day,uuid))");
        }
    }
    public synchronized Map<UUID,PlayerStats> loadPlayers() throws SQLException {
        Map<UUID,PlayerStats> out=new HashMap<>();
        try(Statement s=connection.createStatement();ResultSet r=s.executeQuery("SELECT * FROM players")) {
            while(r.next()) { UUID id=UUID.fromString(r.getString("uuid")); PlayerStats p=new PlayerStats(id,r.getString("name")); p.load(r.getString("name"),r.getLong("first_seen"),r.getLong("last_seen"),r.getLong("playtime"),r.getLong("joins"),r.getLong("quits"),r.getLong("kills"),r.getLong("deaths"),r.getLong("blocks_broken"),r.getLong("blocks_placed"),r.getLong("items_picked"),r.getLong("items_dropped"),r.getLong("items_crafted"),r.getLong("items_consumed"),r.getDouble("damage_dealt"),r.getDouble("damage_taken"),r.getDouble("distance")); out.put(id,p); }
        } return out;
    }
    public synchronized void save(Collection<PlayerStats> players) throws SQLException {
        connection.setAutoCommit(false);
        try(PreparedStatement p=connection.prepareStatement("INSERT INTO players VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT(uuid) DO UPDATE SET name=excluded.name,last_seen=excluded.last_seen,playtime=excluded.playtime,joins=excluded.joins,quits=excluded.quits,kills=excluded.kills,deaths=excluded.deaths,blocks_broken=excluded.blocks_broken,blocks_placed=excluded.blocks_placed,items_picked=excluded.items_picked,items_dropped=excluded.items_dropped,items_crafted=excluded.items_crafted,items_consumed=excluded.items_consumed,damage_dealt=excluded.damage_dealt,damage_taken=excluded.damage_taken,distance=excluded.distance")) {
            for(PlayerStats x:players){int i=1;p.setString(i++,x.uuid().toString());p.setString(i++,x.name());p.setLong(i++,x.firstSeen());p.setLong(i++,x.lastSeen());p.setLong(i++,x.playtimeSeconds());p.setLong(i++,x.joins());p.setLong(i++,x.quits());p.setLong(i++,x.kills());p.setLong(i++,x.deaths());p.setLong(i++,x.blocksBroken());p.setLong(i++,x.blocksPlaced());p.setLong(i++,x.itemsPicked());p.setLong(i++,x.itemsDropped());p.setLong(i++,x.itemsCrafted());p.setLong(i++,x.itemsConsumed());p.setDouble(i++,x.damageDealt());p.setDouble(i++,x.damageTaken());p.setDouble(i++,x.distance());p.addBatch();} p.executeBatch(); connection.commit();
        } catch(Exception e){connection.rollback();throw e;} finally {connection.setAutoCommit(true);}
    }
    public synchronized void incrementBlock(UUID uuid,String material,long amount) throws SQLException { try(PreparedStatement p=connection.prepareStatement("INSERT INTO block_stats VALUES(?,?,?) ON CONFLICT(uuid,material) DO UPDATE SET count=count+excluded.count")){p.setString(1,uuid.toString());p.setString(2,material);p.setLong(3,amount);p.executeUpdate();} }
    public synchronized Map<String,Long> blocks(UUID uuid) throws SQLException {Map<String,Long> m=new HashMap<>();try(PreparedStatement p=connection.prepareStatement("SELECT material,count FROM block_stats WHERE uuid=? ORDER BY count DESC")){p.setString(1,uuid.toString());try(ResultSet r=p.executeQuery()){while(r.next())m.put(r.getString(1),r.getLong(2));}}return m;}
    public synchronized void close(){try{connection.close();}catch(Exception ignored){}}
}
