package ryoryo.temporaryblock.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryoryo.temporaryblock.item.ItemTemporaryBlockPlacer;
import ryoryo.temporaryblock.item.ModItems;

public class BridgeOldVersionHandler {

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		World world = event.getWorld();

		if (!world.isRemote) {
			if (entity != null && entity instanceof EntityItem) {
				EntityItem entityItem = (EntityItem) entity;
				ItemStack stack = entityItem.getItem();

				if (stack.getItem() == ModItems.ITEM_TEMPORARY_BLOCK_PLACER) {
					if (!ItemTemporaryBlockPlacer.hasModTagCompound(stack)) {
						ItemTemporaryBlockPlacer.addDurabilityTag(stack, 512 - stack.getItemDamage());
					}
				}
			}
		}
	}
}