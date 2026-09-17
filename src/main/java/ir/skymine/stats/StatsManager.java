package ir.skymine.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.concurrent.*;

public final class StatsManager {
    private final Database db;
    private final Map<UUID,PlayerStats> players=new ConcurrentHashMap<>();
    private final Map<UUID,Long> sessions=new ConcurrentHashMap<>();
    private final Executor executor;
    public StatsManager(Database db,Executor executor)throws Exception{this.db=db;this.executor=executor;players.putAll(db.loadPlayers());}
    public PlayerStats get(Player p){return players.computeIfAbsent(p.getUniqueId(),id->new PlayerStats(id,p.getName()));}
    public PlayerStats get(UUID id){return players.get(id);}
    public Collection<PlayerStats> all(){return List.copyOf(players.values());}
    public void join(Player p){get(p).join(p.getName());sessions.put(p.getUniqueId(),System.currentTimeMillis());}
    public void quit(Player p){checkpoint(p);get(p).quit();sessions.remove(p.getUniqueId());}
    public void checkpoint(Player p){Long start=sessions.get(p.getUniqueId());if(start!=null){long sec=(System.currentTimeMillis()-start)/1000; if(sec>0)get(p).addPlaytime(sec);sessions.put(p.getUniqueId(),System.currentTimeMillis());}}
    public void checkpointOnline(){for(Player p:Bukkit.getOnlinePlayers())checkpoint(p);}
    public void saveAsync(){checkpointOnline();List<PlayerStats> copy=List.copyOf(players.values());executor.execute(()->{try{db.save(copy);}catch(Exception e){e.printStackTrace();}});}
    public void saveBlocking(){checkpointOnline();try{db.save(players.values());}catch(Exception e){e.printStackTrace();}}
    public void block(UUID id,String material){executor.execute(()->{try{db.incrementBlock(id,material,1);}catch(Exception e){e.printStackTrace();}});}
    public Map<String,Long> blocks(UUID id)throws Exception{return db.blocks(id);}
    public int online(){return Bukkit.getOnlinePlayers().size();}
    public int maxPlayers(){return Bukkit.getMaxPlayers();}
}
