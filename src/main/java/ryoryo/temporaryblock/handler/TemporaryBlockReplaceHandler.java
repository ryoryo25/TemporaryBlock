package ryoryo.temporaryblock.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryoryo.polishedlib.util.Utils;
import ryoryo.temporaryblock.block.ModBlocks;
import ryoryo.temporaryblock.item.ItemTemporaryBlockPlacer;

public class TemporaryBlockReplaceHandler {

	@SubscribeEvent
	public static void rightClickBlock(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos targetPos = event.getPos();
		ItemStack held = event.getItemStack();
		EnumFacing facing = event.getFace();
		Vec3d hitVec = event.getHitVec();

		if (!player.isSneaking() && world.getBlockState(targetPos).getBlock() == ModBlocks.BLOCK_TEMPORARY) {
			if (!held.isEmpty() && !(held.getItem() instanceof ItemTemporaryBlockPlacer)) {
				if (held.getItem() instanceof ItemBlock && ((ItemBlock) held.getItem()).getBlock() != ModBlocks.BLOCK_TEMPORARY) {
					event.setCanceled(true); // cancel placing block normally
					useItem(held, (ItemBlock) held.getItem(), player, world, targetPos, event.getHand(), facing, hitVec.x, hitVec.y, hitVec.z);
				} else if (held.getItem() instanceof ItemBlockSpecial) {
					event.setCanceled(true);
					useItem(held, (ItemBlockSpecial) held.getItem(), player, world, targetPos, event.getHand(), facing, hitVec.x, hitVec.y, hitVec.z);
				} else if (held.getItem() instanceof ItemBed) {
					event.setCanceled(true);
					useItem(held, (ItemBed) held.getItem(), player, world, targetPos, event.getHand(), facing, hitVec.x, hitVec.y, hitVec.z);
				} else if (held.getItem() instanceof ItemDoor) {
					event.setCanceled(true);
					useItem(held, (ItemDoor) held.getItem(), player, world, targetPos, event.getHand(), facing, hitVec.x, hitVec.y, hitVec.z);
				}
			}
		}
	}

	private static void useItem(ItemStack held, Item heldItem, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, double hitX, double hitY, double hitZ) {
		world.setBlockState(pos, Blocks.AIR.getDefaultState()); // temporarily replace it with Air Block

		if (heldItem.onItemUse(player, world, pos, hand, facing, (float) hitX, (float) hitY, (float) hitZ).equals(EnumActionResult.SUCCESS)) {
			player.swingArm(hand);

			if (world.isRemote) {
				// spawn block destroy particle
				// world#destroyBlock uses world#playEvent to show destroy particle and broadcast destroy sound
				// world.playEvent(2001, pos, Block.getStateId(ModBlocks.BLOCK_TEMPORARY.getDefaultState()));
				// IWorldEventListener is implemented in RenderGlobal.class
				Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos, ModBlocks.BLOCK_TEMPORARY.getDefaultState());
				// spawnParticle with EnumParticleTypes.BLOCK_CRACK can be used as follows
				// world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x, y, z, xSpeed, ySpeed, zSpeed, Block.getStateId(state));
			}

			if (Utils.isCreative(player))
				held.grow(1);
		} else {
			world.setBlockState(pos, ModBlocks.BLOCK_TEMPORARY.getDefaultState());
			Utils.sendPopUpMessage(player, "This block can't be placed here!");
		}
	}
}