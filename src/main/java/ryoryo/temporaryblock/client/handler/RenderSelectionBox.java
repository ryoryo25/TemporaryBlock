package ryoryo.temporaryblock.client.handler;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ryoryo.polishedlib.util.Utils;
import ryoryo.temporaryblock.item.ItemTemporaryBlockPlacer;

@SideOnly(Side.CLIENT)
public class RenderSelectionBox {

	@SubscribeEvent
	public void drawBlockHighlight(RenderWorldLastEvent event) {
		EntityPlayer player = Utils.getPlayer();
		World world = Utils.getWorld();

		if(player != null && world != null) {
			ItemStack stack = player.getHeldItemMainhand();

			if(!stack.isEmpty() && stack.getItem() instanceof ItemTemporaryBlockPlacer) {
				BlockPos pos = ItemTemporaryBlockPlacer.getAvailablePos(world, player);
				if(pos != null && world.isAirBlock(pos)) {
					renderSelectionBox(pos, world, player, event.getPartialTicks());
				}
			}
		}
	}

	public void renderSelectionBox(BlockPos pos, World world, EntityPlayer player, float partialTickItem) {
		double d3 = 0.002D;

		GlStateManager.enableBlend();
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.4F);
		GL11.glLineWidth(3.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTickItem;
		double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTickItem;
		double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTickItem;
		IBlockState state = world.getBlockState(pos);
		AxisAlignedBB aabb = state.getBoundingBox(world, pos).offset(pos).grow(d3, d3, d3).offset(-x, -y, -z);

		RenderGlobal.drawSelectionBoundingBox(aabb, 1.0F, 1.0F, 1.0F, 0.4F);
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
}