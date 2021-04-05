package ryoryo.temporaryblock.config;

import java.io.File;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryoryo.polishedlib.util.Utils;
import ryoryo.temporaryblock.TemporaryBlock;
import ryoryo.temporaryblock.crafting.RecipeChargePlacer;
import ryoryo.temporaryblock.util.References;

// @Config(modid = LibMisc.MOD_ID)
public class ModConfig {
	private static Configuration config;

	public ModConfig(File configFile) {
		config = new Configuration(configFile);
		loadConfigs();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(References.MOD_ID)) {
			loadConfigs();
		}
	}

	private void loadConfigs() {
		try {
			generalConfigs(EnumConfigCategory.GENERAL.getName());
		} catch (Exception e) {
			TemporaryBlock.LOGGER.error("Error loading config.");
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
			RecipeChargePlacer.reloadMap(chargeItems);
		}
	}

	public Configuration getConfig() {
		return config;
	}

	// General--------------------------------------------------------------------------------------
	public static List<String> chargeItems;

	public void generalConfigs(String general) {
		chargeItems = Utils.getStringList(config, "PlacerChargeItems", general,
				new String[] {
						Blocks.COBBLESTONE.getRegistryName() + ">" + 1,
						Blocks.DIRT.getRegistryName() + ">" + 1
				}, "Map of items and counts for charging Temporary Block Placer. Format: \"minecraft:dirt>1\"");
	}

	public static enum EnumConfigCategory {
		GENERAL("general", "General Settings"),;

		public final String name;
		public final String comment;

		EnumConfigCategory(String name, String comment) {
			this.name = name;
			this.comment = comment;
		}

		public String getName() {
			return this.name;
		}

		public String getComment() {
			return this.comment;
		}

		public static int getLength() {
			return EnumConfigCategory.values().length;
		}
	}
}
