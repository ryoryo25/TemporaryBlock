package ryoryo.temporaryblock.block;

import net.minecraft.block.Block;
import ryoryo.temporaryblock.TemporaryBlock;

public class ModBlocks {
	public static final Block BLOCK_TEMPORARY = new BlockTemporary();

	public static void init() {
		TemporaryBlock.REGISTER.registerBlock(BLOCK_TEMPORARY, "temporary_block");
	}
}