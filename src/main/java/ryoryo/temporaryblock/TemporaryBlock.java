package ryoryo.temporaryblock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ryoryo.temporaryblock.config.ModConfig;
import ryoryo.temporaryblock.proxy.CommonProxy;
import ryoryo.temporaryblock.util.References;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.MOD_VERSION, dependencies = References.MOD_DEPENDENCIES, acceptedMinecraftVersions = References.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true, guiFactory = References.MOD_GUI_FACTORY)
public class TemporaryBlock {
	@Instance(References.MOD_ID)
	public static TemporaryBlock INSTANCE;

	@SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_COMMON)
	public static CommonProxy proxy;

	public static ModConfig config;
	public static final Logger LOGGER = LogManager.getLogger(References.MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		INSTANCE = this;
		config = new ModConfig(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(config);
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {
		proxy.loadComplete(event);
	}
}