package ryoryo.temporaryblock.handler;

import java.util.ArrayDeque;
import java.util.Deque;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryoryo.polishedlib.util.Utils;
import ryoryo.temporaryblock.block.ModBlocks;

public class ChainDestructionHandler {

	@SubscribeEvent
	public static void chainDestruction(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		IBlockState target = event.getState();
		World world = event.getWorld();
		BlockPos firstPos = event.getPos();

		if (player.isSneaking() && target.getBlock() == ModBlocks.BLOCK_TEMPORARY) {
			event.setCanceled(true);
			replaceAllBlocks(world, firstPos, ModBlocks.BLOCK_TEMPORARY.getDefaultState(), Blocks.AIR.getDefaultState(), 16);
		}
	}

	private static void replaceAllBlocks(World world, BlockPos origin, IBlockState toReplace, IBlockState toPlace, int radius) {
		Deque<BlockPos> queue = new ArrayDeque<BlockPos>();
		queue.offerLast(origin);
		world.setBlockState(origin, toPlace, 2);
		Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(origin, toReplace);

		while (!queue.isEmpty()) {
			BlockPos pos = queue.pollFirst();
			if (origin.distanceSq(pos) <= radius * radius) {
				for (BlockPos posIn : Utils.getAllInSphere(pos, 3)) { // (sqrt(3))^2 = 3
					if (world.getBlockState(posIn) == toReplace) {
						queue.offerLast(posIn);
						world.setBlockState(posIn, toPlace/*, 2*/);
						Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(posIn, toReplace);
					}
				}
			}
		}
	}
}