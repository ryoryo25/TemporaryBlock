package ryoryo.temporaryblock.util;

public class References {
	public static final String MOD_ID = "temporaryblock";
	public static final String MOD_NAME = "TemporaryBlock";

	public static final String MOD_VERSION_MAJOR = "GRADLE.VERSION_MAJOR";
	public static final String MOD_VERSION_MINOR = "GRADLE.VERSION_MINOR";
	public static final String MOD_VERSION_PATCH = "GRADLE.VERSION_PATCH";
	public static final String MOD_VERSION = MOD_VERSION_MAJOR + "." + MOD_VERSION_MINOR + "." + MOD_VERSION_PATCH;

	public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.5.2768,);"
			//			+ "required-after:polishedlib@[1.1.1,);";
			+ "required-after:polishedlib;";

	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";
	public static final String MOD_GUI_FACTORY = "ryoryo.temporaryblock.config.GuiFactoryModConfig";

	public static final String PROXY_CLIENT = "ryoryo.temporaryblock.proxy.ClientProxy";
	public static final String PROXY_COMMON = "ryoryo.temporaryblock.proxy.CommonProxy";

	public static final String CHAT_RUN_OUT_DURABILITY = "chat.run_out_durability";
	public static final String TOOLTIP_REMAIN_USAGE = "tooltip.remain_usage";
	public static final String TOOLTIP_DROP = "tooltip.drop";
}