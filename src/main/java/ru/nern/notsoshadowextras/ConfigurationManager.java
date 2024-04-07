package ru.nern.notsoshadowextras;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

import static ru.nern.notsoshadowextras.NotSoShadowExtras.config;

public class ConfigurationManager
{
    private static final String CONFIG_VERSION = FabricLoader.getInstance().getModContainer("notsoshadowextras").get().getMetadata().getVersion().getFriendlyString();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "notsoshadowextras_config.json");

    public static void loadConfig() {
        try {
            if (file.exists()) {
                StringBuilder contentBuilder = new StringBuilder();
                try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
                    stream.forEach(s -> contentBuilder.append(s).append("\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                config = gson.fromJson(contentBuilder.toString(), Config.class);
            } else {
                config = new Config();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setConfig(config);
    }

    public static void saveConfig() {
        config.lastLoadedVersion = CONFIG_VERSION;
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(getConfig()));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void onInit()
    {
        if(!file.exists())
        {
            saveConfig();
        }else{
            loadConfig();
            if(!config.lastLoadedVersion.equals(CONFIG_VERSION)) saveConfig();
        }
    }

    public static void setConfig(Config config) {
        NotSoShadowExtras.config = config;
    }

    public static Config getConfig() {
        return config;
    }

    public static class Config
    {
        private String lastLoadedVersion = "";
        public Blocks blocks;
        public Items items;
        public LightEngine light;

        public static class Blocks{
            public boolean updateSuppressionCrashFix = true;
            public boolean updateSuppressionDupeFix = false;
            public boolean updateSuppressionHopperDupeFix = false;
            public boolean updateSuppressionItemDamageFix = false;
            public boolean alertAboutUpdateSuppressionCrash = false;
            public boolean noSuppressionStacktrace = false;
            public boolean copperBulb1gt = false;
            public boolean crafter1gt = false;

        }

        public static class Items {
            public boolean giveCommandUnderstackedItems = false;
        }

        public static class LightEngine {
            public boolean disableDataFixerLightRecalculation = true;
        }

        public Config()
        {
            blocks = new Blocks();
            items = new Items();
            light = new LightEngine();
        }
    }
}
