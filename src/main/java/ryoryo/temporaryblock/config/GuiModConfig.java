package ryoryo.temporaryblock.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import ryoryo.temporaryblock.TemporaryBlock;
import ryoryo.temporaryblock.config.ModConfig.EnumConfigCategory;
import ryoryo.temporaryblock.util.References;

public class GuiModConfig extends GuiConfig {
	public GuiModConfig(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), References.MOD_ID, false, false, References.MOD_NAME + " Configurations(WIP)");
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (EnumConfigCategory cat : EnumConfigCategory.values()) {
			TemporaryBlock.config.getConfig().setCategoryComment(cat.name, cat.comment);
			list.add(new ConfigElement(TemporaryBlock.config.getConfig().getCategory(cat.name)));
		}

		return list;
	}
}