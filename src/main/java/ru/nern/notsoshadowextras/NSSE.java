package ru.nern.notsoshadowextras;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nern.fconfiglib.v1.ConfigManager;
import ru.nern.fconfiglib.v1.api.annotations.mixins.MixinOption;
import ru.nern.fconfiglib.v1.api.annotations.restrictions.InRangeInt;
import ru.nern.fconfiglib.v1.api.annotations.validation.ConfigValidators;
import ru.nern.fconfiglib.v1.json.JsonConfigManager;
import ru.nern.fconfiglib.v1.log.Sl4jLoggerWrapper;
import ru.nern.fconfiglib.v1.validation.RestrictionsConfigValidator;
import ru.nern.fconfiglib.v1.validation.VersionConfigValidator;
import ru.nern.notsoshadowextras.config.ConfigFixes;

public class NSSE implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("notsoshadowextras");
	public static int CONFIG_VERSION = 2;

	public static ConfigManager<Config, JsonObject> configManager = JsonConfigManager
			.builderOf(Config.class)
			.modId("notsoshadowextras")
			.fixers(fixers ->
					fixers.put(2, ConfigFixes.V2_FIXER))
			.logger(Sl4jLoggerWrapper.createFrom(LOGGER))
			.version(CONFIG_VERSION).create();

	@Override
	public void onInitialize() {
		NSSECommands.init();
	}

	public static Config config() {
		return configManager.config();
	}

	@ConfigValidators({VersionConfigValidator.class, RestrictionsConfigValidator.class})
	public static class Config {
		public UpdateSuppression Update_Suppression = new UpdateSuppression();
		public SoundSuppression Sound_Suppression = new SoundSuppression();
		public Blocks Blocks = new Blocks();
		public Items Items = new Items();
		public Additions Additions = new Additions();
		public Misc Misc = new Misc();

		public static class UpdateSuppression {
			@MixinOption("update_suppression.MinecraftServerMixin_CrashFix")
			public boolean CrashFix = true;

			@MixinOption("update_suppression.dupe_fix.*")
			public boolean DupeFix = false;

			@MixinOption("update_suppression.HopperBlockEntityMixin")
			public boolean HopperDupeFix = false;

			@MixinOption("update_suppression.damage_fix.*")
			public boolean ItemDamageFix = false;

			public boolean AlertAboutCrash = true;

			@MixinOption("update_suppression.ServerCrashSafePLMixin_HideST")
			public boolean HideStackTrace = false;
		}

		public static class SoundSuppression {
			@MixinOption("sound_suppression.dupe_fix.*")
			public boolean DupeFix = false;

			@MixinOption("sound_suppression.CakeBlockMixin")
			public boolean InfiniteCakeFix = false;

		}

		public static class Blocks {
			@MixinOption("blocks.BulbBlockMixin")
			public boolean CopperBulb_1gt = false;

			@MixinOption("blocks.CrafterBlockMixin")
			public boolean Crafter_1gt = false;
		}

		public static class Additions {
			@MixinOption("additions.BlockItemMixin")
			public boolean BlockEntitySwapCommand = true;
		}

		public static class Items {
			@InRangeInt(min = 1)
			public int MaxCountPerStack = 99; // 1095216660 is the maximum. Past that, the game underflows the value

			@MixinOption(value = {
					"items.shadowing.*",
					"items.shadowing.loaders.*"
			})
			public boolean PersistentShadowItems = true;
		}

		public static class Misc {
			@MixinOption("misc.DataFixerBuilderMixin")
			public boolean DisableLightRecalculationDataFixer = true;
		}
	}


}
