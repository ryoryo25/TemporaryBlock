package ryoryo.temporaryblock.proxy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ryoryo.temporaryblock.TemporaryBlock;
import ryoryo.temporaryblock.block.ModBlocks;
import ryoryo.temporaryblock.handler.ChainDestructionHandler;
import ryoryo.temporaryblock.handler.TemporaryBlockReplaceHandler;
import ryoryo.temporaryblock.item.ModItems;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
	}

	public void init(FMLInitializationEvent event) {
		TemporaryBlock.REGISTER.addRecipe("temporary_block_placer", new ItemStack(ModItems.ITEM_TEMPORARY_BLOCK_PLACER), "sCs", "CsC", "sCs", 's', Items.STICK, 'C', Blocks.COBBLESTONE);

		MinecraftForge.EVENT_BUS.register(new TemporaryBlockReplaceHandler());
		MinecraftForge.EVENT_BUS.register(new ChainDestructionHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void loadComplete(FMLLoadCompleteEvent event) {
	}
}