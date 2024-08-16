package ru.nern.notsoshadowextras;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nern.fconfiglib.v1.ConfigManager;
import ru.nern.fconfiglib.v1.LoggerWrapper;
import ru.nern.fconfiglib.v1.annotations.InRangeInt;
import ru.nern.fconfiglib.v1.json.JsonConfigManager;
import ru.nern.notsoshadowextras.config.ConfigFixes;

import java.util.Collections;

public class NSSE implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("notsoshadowextras");

	public static int CONFIG_VERSION = 2;

	private static final LoggerWrapper wrapper = new LoggerWrapper() {
		@Override
		public void info(String message) {
			LOGGER.info(message);
		}

		@Override
		public void warn(String message) {
			LOGGER.warn(message);
		}

		@Override
		public void error(String message) {
			LOGGER.error(message);
		}
	};

	public static ConfigManager<Config, JsonObject> configManager = JsonConfigManager
			.builderOf(Config.class)
			.modId("notsoshadowextras")
			.fixers(Sets.newLinkedHashSet(Collections.singleton(ConfigFixes.V2_FIXER)))
			.logger(wrapper)
			.version(CONFIG_VERSION).create();

	@Override
	public void onInitialize() {
		NSSECommands.init();
	}

	public static Config config() {
		return configManager.config();
	}
	
	public static class Config
	{
		public UpdateSuppression Update_Suppression = new UpdateSuppression();
		public SoundSuppression Sound_Suppression = new SoundSuppression();
		public Blocks Blocks = new Blocks();
		public Items Items = new Items();
		public Misc Misc = new Misc();

		public static class UpdateSuppression {
			public boolean CrashFix = true;
			public boolean DupeFix = false;
			public boolean HopperDupeFix = false;
			public boolean ItemDamageFix = false;
			public boolean AlertAboutCrash = true;
			public boolean HideStackTrace = false;
		}

		public static class SoundSuppression {
			public boolean InfiniteCakeFix = false;
			public boolean DupeFix = false;
			public boolean ItemDamageFix = false;
		}

		public static class Blocks {
			public boolean CopperBulb_1gt = false;
			public boolean Crafter_1gt = false;
		}

		public static class Items {
			@InRangeInt(min = 1)
			public int MaxCountPerStack = 99; // 1095216660 is the maximum. Past that, the game underflows the value
		}

		public static class Misc {
			public boolean DisableLightRecalculationDataFixer = true;
		}
	}


}
