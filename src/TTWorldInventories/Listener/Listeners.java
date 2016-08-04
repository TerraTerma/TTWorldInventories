package TTWorldInventories.Listener;

import TTCore.Entity.Living.Human.Player.TTPlayer;
import TTWorldInventories.Mech.WorldInventoriesData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class Listeners implements Listener {
	@EventHandler
	public void playerChangeWorld(PlayerChangedWorldEvent event) {
		TTPlayer player = TTPlayer.getPlayer(event.getPlayer());
		WorldInventoriesData data = (WorldInventoriesData) player.getOrCreateSingleData(WorldInventoriesData.class);
		data.swapFor(event.getFrom(), player.getEntity(), data.isSaveOnLeaveEnabled());
	}
}