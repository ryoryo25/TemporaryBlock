package ryoryo.temporaryblock.item;

import net.minecraft.item.Item;
import ryoryo.temporaryblock.TemporaryBlock;

public class ModItems {
	public static final Item ITEM_TEMPORARY_BLOCK_PLACER = new ItemTemporaryBlockPlacer();

	public static void init() {
		TemporaryBlock.REGISTER.registerItem(ITEM_TEMPORARY_BLOCK_PLACER, "temporary_block_placer");
	}
}