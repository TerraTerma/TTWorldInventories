package TTWorldInventories.Mech;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import TTCore.Entity.TTEntity;
import TTCore.Mech.DataHandlers.SavableData;
import TTCore.Mech.Stores.PlayerData;
import TTCore.Savers.Saver;
import TTCore.Savers.SpecialSaver;
import TTWorldInventories.Tools.InventoryStore;

public class WorldInventoriesData implements PlayerData, SavableData {

	Map<World, InventoryStore> INVS = new HashMap<>();
	boolean SAVE_ON_LEAVE = true;

	public boolean isSaveOnLeaveEnabled() {
		return SAVE_ON_LEAVE;
	}

	public WorldInventoriesData setSaveOnLeave(boolean check) {
		SAVE_ON_LEAVE = check;
		return this;
	}

	public void set(World world, InventoryStore store) {
		InventoryStore store2 = INVS.get(world);
		if (store2 == null) {
			INVS.put(world, store);
		} else {
			INVS.replace(world, store);
		}
	}

	public InventoryStore get(World world) {
		InventoryStore store = INVS.get(world);
		if (store == null) {
			return new InventoryStore();
		} else {
			return store;
		}
	}

	public void swapFor(World from, Player player, boolean check) {
		if (check) {
			set(from, new InventoryStore(player.getInventory()));
		}
		InventoryStore store = get(player.getWorld());
		store.apply(player.getInventory());
	}

	@Override
	public void save(Saver saver) {
		Optional<TTEntity> opEntity = TTEntity.getRelatedMech(this);
		if(SAVE_ON_LEAVE){
			if(opEntity.isPresent()){
				TTEntity entity = opEntity.get();
				if(entity.getEntity() instanceof Player){
					Player player = (Player)entity.getEntity();
					World world = player.getWorld();
					Inventory inventory = player.getInventory();
					set(world, new InventoryStore(inventory));
				}
			}
		}
		INVS.entrySet().stream().forEach(e -> {
			String world = e.getKey().getName();
			InventoryStore store = e.getValue();
			for (int A = 0; A < 64; A++) {
				String path = world + ".Slot" + A;
				Optional<ItemStack> opItem = store.get(A);
				ItemStack item = null;
				if (opItem.isPresent()) {
					System.out.println("item is present");
					item = opItem.get();
				} else {
					Optional<ItemStack> opItem2 = store.get(-1);
					if (opItem2.isPresent()) {
						System.out.println("item -1 is present");
						item = opItem2.get();
					}else{
						saver.remove(path);
					}
				}

				if (item != null) {
					System.out.println("item saved");
					new SpecialSaver().setItemStack(saver, item, path);
				}
			}
		});
	}

	@Override
	public boolean load(Saver saver) {
		Map<String, Object> values = saver.values(true);
		values.entrySet().stream().forEach(e -> {
			String[] args = e.getKey().split("\\.");
			if (args.length == 3) {
				System.out.println(e.getKey());
				World world = Bukkit.getWorld(args[1]);
				Integer slot = Integer.parseInt(args[2].replace("Slot", ""));
				Optional<ItemStack> items = new SpecialSaver().getItemStack(saver, world.getName(), "Slot" + slot);
				if (items.isPresent()) {
					InventoryStore store = get(world);
					store.set(slot, items.get());
					set(world, store);
				}
			}
		});
		return true;
	}

}