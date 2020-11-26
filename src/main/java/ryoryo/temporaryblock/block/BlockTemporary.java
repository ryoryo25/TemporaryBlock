package ryoryo.temporaryblock.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import ryoryo.polishedlib.block.BlockBase;

public class BlockTemporary extends BlockBase {
	public BlockTemporary() {
		super(Material.ROCK, "temporary_block");
		this.setSoundType(SoundType.METAL);
		this.setHardness(0.0F);
		this.setResistance(10000.0F);
	}

	// drop nothing
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 0;
	}
}