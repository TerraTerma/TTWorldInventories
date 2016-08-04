package TTWorldInventories.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import TTCore.Stores.TwoStore;

public class InventoryStore {

	List<TwoStore<Integer, ItemStack>> ITEMS = new ArrayList<>();

	public InventoryStore() {

	}

	public InventoryStore(Inventory inv) {
		for (int A = 0; A < 64; A++) {
			ItemStack item = inv.getItem(A);
			if (item != null) {
				ITEMS.add(new TwoStore<>(A, item));
			}
		}
	}

	public void add(ItemStack item) {
		ITEMS.add(new TwoStore<>(-1, item));
	}

	public void set(int A, ItemStack item) {
		remove(A);
		ITEMS.add(new TwoStore<>(A, item));
	}

	public Optional<ItemStack> get(int A) {
		Optional<TwoStore<Integer, ItemStack>> opItem = ITEMS.stream().filter(e -> (e.getOne() == A)).findFirst();
		if (opItem.isPresent()) {
			return Optional.of(opItem.get().getTwo());
		} else {
			return Optional.empty();
		}
	}

	public void remove(int A) {
		ITEMS.stream().forEach(s -> {
			if (s.getOne() == A) {
				ITEMS.remove(s);
			}
		});
	}

	public void remove(ItemStack item) {
		ITEMS.stream().forEach(s -> {
			if (s.getTwo().equals(item)) {
				ITEMS.remove(s);
			}
		});
	}

	public List<TwoStore<Integer, ItemStack>> get() {
		return ITEMS;
	}

	public void apply(Inventory inv) {
		inv.clear();
		ITEMS.stream().forEach(e -> {
			if (e.getOne() == -1) {
				inv.addItem(e.getTwo());
			} else {
				inv.setItem(e.getOne(), e.getTwo());
			}
		});
	}
}