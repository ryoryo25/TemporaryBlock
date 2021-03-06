package ryoryo.temporaryblock.proxy;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ryoryo.polishedlib.util.handlers.RecipeHandler;
import ryoryo.temporaryblock.block.ModBlocks;
import ryoryo.temporaryblock.handler.BridgeOldVersionHandler;
import ryoryo.temporaryblock.handler.ChainDestructionHandler;
import ryoryo.temporaryblock.handler.TemporaryBlockReplaceHandler;
import ryoryo.temporaryblock.item.ItemTemporaryBlockPlacer;
import ryoryo.temporaryblock.item.ModItems;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
	}

	public void init(FMLInitializationEvent event) {

		RecipeHandler.addRecipe("temporary_block_placer",
				//				ItemTemporaryBlockPlacer.getStackWithDurability(0),
				ItemTemporaryBlockPlacer.getNewStackWithDurability(512),
				"iCi",
				" i ",
				" i ",
				'i', "ingotIron",
				'C', Blocks.COBBLESTONE);
		//		RecipeChargePlacer.addRecipe("charge_temporary_block_placer");

		MinecraftForge.EVENT_BUS.register(TemporaryBlockReplaceHandler.class);
		MinecraftForge.EVENT_BUS.register(ChainDestructionHandler.class);
		MinecraftForge.EVENT_BUS.register(BridgeOldVersionHandler.class);
	}

	public void postInit(FMLPostInitializationEvent event) {}

	public void loadComplete(FMLLoadCompleteEvent event) {}
}