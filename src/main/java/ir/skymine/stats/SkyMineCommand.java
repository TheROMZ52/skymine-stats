package ir.skymine.stats;

import org.bukkit.command.*;
import java.util.*;

public final class SkyMineCommand implements CommandExecutor,TabCompleter {
    private final StatsManager stats;
    public SkyMineCommand(StatsManager stats){this.stats=stats;}
    public boolean onCommand(CommandSender s,Command c,String l,String[] a){if(a.length==0){if(!(s instanceof org.bukkit.entity.Player p)){s.sendMessage("SkyMine Stats: console cannot show personal stats.");return true;}PlayerStats x=stats.get(p);s.sendMessage("§b§lSkyMine Stats §7| §f"+x.name());s.sendMessage("§7Playtime: §f"+x.playtimeSeconds()+"s §8| §7Kills: §f"+x.kills()+" §8| §7Deaths: §f"+x.deaths());s.sendMessage("§7Blocks broken: §f"+x.blocksBroken()+" §8| §7Placed: §f"+x.blocksPlaced());return true;}if(a[0].equalsIgnoreCase("save")){if(!s.hasPermission("skymine.stats.save")){s.sendMessage("§cNo permission.");return true;}stats.saveAsync();s.sendMessage("§aSkyMine Stats save queued.");return true;}if(a[0].equalsIgnoreCase("top")){String m=a.length>1?a[1]:"playtime";List<PlayerStats> x=new ArrayList<>(stats.all());Comparator<PlayerStats> c2=switch(m){case "kills"->Comparator.comparingLong(PlayerStats::kills);case "deaths"->Comparator.comparingLong(PlayerStats::deaths);case "blocks"->Comparator.comparingLong(PlayerStats::blocksBroken);default->Comparator.comparingLong(PlayerStats::playtimeSeconds);};x.sort(c2.reversed());s.sendMessage("§b§lSkyMine Top §7| §f"+m);for(int i=0;i<Math.min(10,x.size());i++)s.sendMessage("§7"+(i+1)+". §f"+x.get(i).name()+" §8- §b"+c2.reversed().compare(x.get(i),x.get(i)));return true;}s.sendMessage("§b/skystats §7| §b/skystats top <playtime|kills|deaths|blocks> §7| §b/skystats save");return true;}
    public List<String> onTabComplete(CommandSender s,Command c,String l,String[] a){if(a.length==1)return List.of("top","save");if(a.length==2&&a[0].equalsIgnoreCase("top"))return List.of("playtime","kills","deaths","blocks");return List.of();}
}
