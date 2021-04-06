package ryoryo.temporaryblock.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import ryoryo.temporaryblock.client.handler.RenderSelectionBox;

public class ClientProxy extends CommonProxy {
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(RenderSelectionBox.class);
	}
}