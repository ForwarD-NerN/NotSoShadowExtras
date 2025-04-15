package ru.nern.notsoshadowextras.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShadowUtil {

    private static final Cache<UUID, ItemStack> SHADOW_MAP = CacheBuilder.newBuilder()
            .weakValues()
            .build();

    public static final Set<Inventory> UPDATE_SET = new HashSet<>();

    public static ItemStack getOrAdd(UUID uuid, ItemStack itemStack) {
        ItemStack reference = SHADOW_MAP.getIfPresent(uuid);
        if (reference != null)
            return reference;
        ((ShadowItem)(Object)itemStack).notsoshadowextras$setShadowUuid(uuid);
        SHADOW_MAP.put(uuid, itemStack);
        return itemStack;
    }

    public static void remove(UUID uuid) {
        SHADOW_MAP.invalidate(uuid);
    }
}
