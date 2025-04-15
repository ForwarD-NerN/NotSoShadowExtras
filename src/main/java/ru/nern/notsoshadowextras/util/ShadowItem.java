package ru.nern.notsoshadowextras.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.Collection;
import java.util.UUID;

public interface ShadowItem {

    static void searchInNbt(RegistryWrapper.WrapperLookup registries, BlockEntity instance, NbtCompound nbt) {
        if (instance instanceof Inventory inv) {
            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).notsoshadowextras$getShadowUuid() != null) {
                        ((ShadowItem) (Object) stack).notsoshadowextras$removeSlot(inv, index);
                    }
                }
            } catch (Exception ignored) {}

            instance.read(nbt, registries);

            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).notsoshadowextras$getShadowUuid() != null) {
                        ((ShadowItem) (Object) stack).notsoshadowextras$addSlot(inv, index);
                        inv.setStack(index, ShadowUtil.getOrAdd(((ShadowItem) (Object) stack).notsoshadowextras$getShadowUuid(), stack));
                    }
                }
            } catch (Exception ignored) {}
        } else {
            instance.read(nbt, registries);
        }
    }

    UUID notsoshadowextras$getShadowUuid();

    UUID notsoshadowextras$getClientShadowUuid();

    void notsoshadowextras$setShadowUuid(UUID uuid);

    void notsoshadowextras$setClientShadowUuid(UUID uuid);

    void notsoshadowextras$addSlot(Inventory inventory, int slot);

    void notsoshadowextras$removeSlot(Inventory inventory, int slot);

    Collection<Inventory> notsoshadowextras$getShadowInventories();
}
