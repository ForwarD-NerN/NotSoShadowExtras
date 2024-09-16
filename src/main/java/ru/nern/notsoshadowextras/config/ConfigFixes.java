package ru.nern.notsoshadowextras.config;

import com.google.gson.JsonObject;
import ru.nern.fconfiglib.v1.api.ConfigFixer;
import ru.nern.notsoshadowextras.NSSE;

import static ru.nern.fconfiglib.v1.json.JsonConfigUtils.createPath;
import static ru.nern.fconfiglib.v1.json.JsonConfigUtils.move;

public class ConfigFixes {

    public static ConfigFixer<NSSE.Config, JsonObject> V2_FIXER = (config, raw) -> {
        NSSE.LOGGER.info("Converting to config V2...");
        createPath(raw, "Update_Suppression");
        createPath(raw, "Blocks");
        createPath(raw, "Items");
        createPath(raw, "Misc");

        move(raw, "blocks.updateSuppressionCrashFix", "Update_Suppression.CrashFix");
        move(raw, "blocks.updateSuppressionDupeFix", "Update_Suppression.DupeFix");
        move(raw, "blocks.updateSuppressionHopperDupeFix", "Update_Suppression.HopperDupeFix");
        move(raw, "blocks.updateSuppressionItemDamageFix", "Update_Suppression.ItemDamageFix");
        move(raw, "blocks.alertAboutUpdateSuppressionCrash", "Update_Suppression.AlertAboutCrash");
        move(raw, "blocks.noSuppressionStacktrace", "Update_Suppression.HideStackTrace");
        move(raw, "blocks.copperBulb1gt", "Blocks.CopperBulb_1gt");
        move(raw, "blocks.crafter1gt", "Blocks.Crafter_1gt");
        move(raw, "items.maxCountPerStack", "Items.MaxCountPerStack");
        move(raw, "light.disableDataFixerLightRecalculation", "Misc.DisableLightRecalculationDataFixer");
        NSSE.LOGGER.info("Conversion completed");
    };
}
