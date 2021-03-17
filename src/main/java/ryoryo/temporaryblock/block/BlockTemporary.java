package ryoryo.temporaryblock.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ryoryo.polishedlib.block.BlockBase;

public class BlockTemporary extends BlockBase {
	public BlockTemporary() {
		super(Material.ROCK, "temporary_block");
		this.setSoundType(SoundType.METAL);
		this.setHardness(0.0F);
		this.setResistance(15.0F);
	}

	// drop nothing
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 0;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState stateSide = blockAccess.getBlockState(pos.offset(side));
		Block blockSide = stateSide.getBlock();

		return blockState != stateSide || blockSide != this;
	}
}