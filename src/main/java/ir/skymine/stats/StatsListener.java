package ir.skymine.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;

public final class StatsListener implements Listener {
    private final StatsManager stats;
    public StatsListener(StatsManager stats){this.stats=stats;}
    @EventHandler public void join(PlayerJoinEvent e){stats.join(e.getPlayer());}
    @EventHandler public void quit(PlayerQuitEvent e){stats.quit(e.getPlayer());}
    @EventHandler public void breakBlock(BlockBreakEvent e){if(!e.isCancelled()){var p=stats.get(e.getPlayer());p.breakBlock();stats.block(p.uuid(),e.getBlock().getType().name());}}
    @EventHandler public void placeBlock(BlockPlaceEvent e){if(!e.isCancelled())stats.get(e.getPlayer()).placeBlock();}
    @EventHandler public void death(PlayerDeathEvent e){stats.get(e.getEntity()).death();var k=e.getEntity().getKiller();if(k!=null)stats.get(k).kill();}
    @EventHandler public void damage(EntityDamageEvent e){if(e.isCancelled())return; if(e.getEntity() instanceof Player p)stats.get(p).damageTaken(e.getFinalDamage()); if(e instanceof EntityDamageByEntityEvent d && d.getDamager() instanceof Player p)stats.get(p).damageDealt(e.getFinalDamage());}
    @EventHandler public void pickup(EntityPickupItemEvent e){if(!e.isCancelled()&&e.getEntity() instanceof Player p)stats.get(p).pickup(e.getItem().getItemStack().getAmount());}
    @EventHandler public void drop(PlayerDropItemEvent e){if(!e.isCancelled())stats.get(e.getPlayer()).drop(e.getItemDrop().getItemStack().getAmount());}
    @EventHandler public void craft(CraftItemEvent e){if(!e.isCancelled()&&e.getWhoClicked() instanceof Player p)stats.get(p).craft(e.getRecipe().getResult().getAmount());}
    @EventHandler public void consume(PlayerItemConsumeEvent e){if(!e.isCancelled())stats.get(e.getPlayer()).consume(1);}
    @EventHandler public void move(PlayerMoveEvent e){if(e.getTo()!=null&&e.getFrom().getWorld()==e.getTo().getWorld())stats.get(e.getPlayer()).distance(e.getFrom().distance(e.getTo()));}
}
