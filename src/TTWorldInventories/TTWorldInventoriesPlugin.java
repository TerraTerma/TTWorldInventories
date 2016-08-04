package TTWorldInventories;

import TTWorldInventories.Listener.Listeners;
import TTWorldInventories.Mech.WorldInventoriesData;

public class TTWorldInventoriesPlugin extends org.bukkit.plugin.java.JavaPlugin {
	static TTWorldInventoriesPlugin PLUGIN;

	public void onEnable() {
		PLUGIN = this;
		TTCore.Mech.DataHandler.MECHS.add(WorldInventoriesData.class);
		getServer().getPluginManager().registerEvents(new Listeners(), this);
	}

	public static TTWorldInventoriesPlugin getPlugin() {
		return PLUGIN;
	}
}