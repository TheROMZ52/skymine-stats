package ir.skymine.stats;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.concurrent.*;

public final class SkyMineStatsPlugin extends JavaPlugin {
    private Database database; private StatsManager stats; private ExecutorService executor; private WebApi web;
    public void onEnable(){saveDefaultConfig();try{database=new Database(new File(getDataFolder(),"skymine.db"));executor=Executors.newSingleThreadExecutor(r->{Thread t=new Thread(r,"SkyMineStats-DB");t.setDaemon(true);return t;});stats=new StatsManager(database,executor);getServer().getPluginManager().registerEvents(new StatsListener(stats),this);var cmd=getCommand("skystats");if(cmd!=null){var c=new SkyMineCommand(stats);cmd.setExecutor(c);cmd.setTabCompleter(c);}int autosave=Math.max(30,getConfig().getInt("autosave-seconds",60));getServer().getScheduler().runTaskTimer(this,stats::saveAsync,autosave*20L,autosave*20L);if(getConfig().getBoolean("http.enabled",true)){web=new WebApi(stats,getConfig().getInt("http.port",8787),getConfig().getBoolean("http.require-token",false),getConfig().getString("http.token",""));web.start();}getLogger().info("SkyMine Stats enabled.");}catch(Exception e){getLogger().severe("Failed to start SkyMine Stats: "+e.getMessage());getServer().getPluginManager().disablePlugin(this);}}
    public void onDisable(){if(stats!=null)stats.saveBlocking();if(web!=null)web.stop();if(executor!=null)executor.shutdownNow();if(database!=null)database.close();}
}
