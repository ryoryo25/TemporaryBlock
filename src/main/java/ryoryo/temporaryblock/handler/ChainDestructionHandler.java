package ryoryo.temporaryblock.handler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryoryo.temporaryblock.block.ModBlocks;

public class ChainDestructionHandler {
	@SubscribeEvent
	public void chainDestruction(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		IBlockState target = event.getState();
		World world = event.getWorld();
		BlockPos firstPos = event.getPos();

		if(player.isSneaking() && target.getBlock() == ModBlocks.BLOCK_TEMPORARY) {
			event.setCanceled(true);
			destroyAllBlocks(world, firstPos, ModBlocks.BLOCK_TEMPORARY, 16);
		}
	}

	private void destroyAllBlocks(World world, BlockPos origin, Block toDestroy, int radius) {
		Deque<BlockPos> queue = new ArrayDeque<BlockPos>();
		queue.offerLast(origin);
		//			world.destroyBlock(firstPos, false);
		while(!queue.isEmpty()) {
			BlockPos pos = queue.pollFirst();
			if(origin.distanceSq(pos) <= radius * radius) {
				for(BlockPos posIn : getAllInSphere(pos, 3)) { // (sqrt(3))^2 = 3
					if(world.getBlockState(posIn).getBlock() == toDestroy) {
						queue.offerLast(posIn);
						world.setBlockState(posIn, Blocks.AIR.getDefaultState(), 2);
						Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(posIn, toDestroy.getDefaultState());
					}
				}
			}
		}
	}

	private Set<BlockPos> getAllInSphere(BlockPos center, int radiusSq) {
		int radius = (int) Math.floor(Math.sqrt((double) radiusSq));
		Set<BlockPos> all = new HashSet<>();
		BlockPos from = center.add(-radius, -radius, -radius);
		BlockPos to = center.add(radius, radius, radius);

		for(BlockPos pos : BlockPos.getAllInBox(from, to)) {
			if(pos.getY() < 0 || pos.getY() > 255 || center.distanceSq(pos) > radiusSq)
				continue;
			all.add(pos);
		}

		return all;
	}
}