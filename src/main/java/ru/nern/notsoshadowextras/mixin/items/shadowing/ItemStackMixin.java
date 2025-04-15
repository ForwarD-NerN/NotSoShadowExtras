package ru.nern.notsoshadowextras.mixin.items.shadowing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.notsoshadowextras.NSSE;
import ru.nern.notsoshadowextras.util.ShadowItem;
import ru.nern.notsoshadowextras.util.ShadowUtil;

import java.util.*;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ShadowItem {

    @Unique private UUID shadowUuid = null;

    @Unique private UUID clientShadowUuid = null;

    @Unique Set<Pair<Inventory, Integer>> slots = new HashSet<>();

    @Shadow public abstract boolean isEmpty();

    @Shadow private int count;

    @Shadow public abstract Item getItem();

    @ModifyReturnValue(method = "fromNbt", at = @At("RETURN"))
    private static Optional<ItemStack> notsoshadowextras$postFromNbt(Optional<ItemStack> optionalStack, RegistryWrapper.WrapperLookup registries, NbtElement nbtElement) {
        NbtCompound nbt = nbtElement.asCompound().get();

        if (nbt.contains("shadow")) {
            UUID shadowUuid = nbt.getString("shadow")
                    .map(UUID::fromString)
                    .get();
            ItemStack stack = optionalStack.orElse(null);
            if (stack == null)
                return optionalStack;

            ShadowUtil.getOrAdd(shadowUuid, stack);
            ((ShadowItem)(Object)stack).notsoshadowextras$setShadowUuid(shadowUuid);

            NSSE.LOGGER.debug("Shadowed item loaded from memory");
            NSSE.LOGGER.debug("uuid: " + shadowUuid);

            return Optional.of(stack);
        }

        return optionalStack;
    }

    @ModifyReturnValue(method = "toNbt*", at = @At("RETURN"))
    private NbtElement notsoshadowextras$postToNbt(NbtElement nbtElement, RegistryWrapper.WrapperLookup registries) {
        if (this.shadowUuid != null) {
            if (this.isEmpty()) {
                ShadowUtil.remove(this.shadowUuid);
                this.notsoshadowextras$setShadowUuid(null);
            } else {
                NbtCompound nbt = nbtElement.asCompound().get();

                nbt.putString("shadow", this.shadowUuid.toString());

                NSSE.LOGGER.debug("Shadowed item saved in memory");
                NSSE.LOGGER.debug("uuid: " + shadowUuid);

                return nbt;
            }
        }

        return nbtElement;
    }

    @Inject(method = "setCount", at = @At("RETURN"))
    public void notsoshadowextras$propogateUpdate(int count, CallbackInfo ci) {
        ShadowUtil.UPDATE_SET.addAll(notsoshadowextras$getShadowInventories());
    }

    @Override
    public void notsoshadowextras$setShadowUuid(UUID uuid) {
        this.shadowUuid = uuid;
        this.notsoshadowextras$setClientShadowUuid(uuid);
    }

    @Override
    public void notsoshadowextras$setClientShadowUuid(UUID uuid) {
        this.clientShadowUuid = uuid;
    }

    @Override
    public void notsoshadowextras$addSlot(Inventory inventory, int slot) {
        slots.add(new ImmutablePair<>(inventory, slot));
    }

    @Override
    public void notsoshadowextras$removeSlot(Inventory inventory, int slot) {
        slots.remove(new ImmutablePair<>(inventory, slot));
    }

    @Override
    public UUID notsoshadowextras$getClientShadowUuid() {
        return clientShadowUuid;
    }

    @Override
    public UUID notsoshadowextras$getShadowUuid() {
        return shadowUuid;
    }

    @Override
    public Collection<Inventory> notsoshadowextras$getShadowInventories() {
        return slots.stream().map(Pair::getLeft).toList();
    }
}
