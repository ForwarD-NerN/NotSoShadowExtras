package ru.nern.notsoshadowextras;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotSoShadowExtras implements ModInitializer {
	public static ConfigurationManager.Config config = new ConfigurationManager.Config();
	public static final Logger LOGGER = LoggerFactory.getLogger("notsoshadowextras");

	@Override
	public void onInitialize()
	{
		ConfigurationManager.onInit();
	}
}
