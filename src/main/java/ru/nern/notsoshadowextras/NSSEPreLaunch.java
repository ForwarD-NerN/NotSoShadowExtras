package ru.nern.notsoshadowextras;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class NSSEPreLaunch implements PreLaunchEntrypoint {
    // Loads the config in the pre-launch entrypoint to make maxCountPerStack work properly
    @Override
    public void onPreLaunch() {
        ConfigurationManager.onInit();
    }
}
