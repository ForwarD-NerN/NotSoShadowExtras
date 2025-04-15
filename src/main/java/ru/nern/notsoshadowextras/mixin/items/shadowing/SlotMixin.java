package ru.nern.notsoshadowextras.mixin.items.shadowing;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.notsoshadowextras.util.ShadowItem;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getStack();

    @Shadow @Final public Inventory inventory;

    @Shadow public abstract int getIndex();

    @Inject(method = "setStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "HEAD"))
    public void notsoshadowextras$rememberInventory(ItemStack next, ItemStack curr, CallbackInfo ci) {
        if (((ShadowItem)(Object)curr).notsoshadowextras$getShadowUuid() != null) {
            ((ShadowItem)(Object)curr).notsoshadowextras$removeSlot(this.inventory, getIndex());
        }
        if (((ShadowItem)(Object)next).notsoshadowextras$getShadowUuid() != null) {
            ((ShadowItem)(Object)next).notsoshadowextras$addSlot(this.inventory, getIndex());
        }
    }
}
