package ru.nern.notsoshadowextras.mixin.items.shadowing;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.util.ShadowItem;

@Mixin(SimpleInventory.class)
public abstract class SimpleInventoryMixin {

    @Shadow public abstract ItemStack getStack(int slot);

    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
    public void notsoshadowextras$trackRemove(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack curr = getStack(slot);
        if (((ShadowItem) (Object) curr).notsoshadowextras$getShadowUuid() != null) {
            ((ShadowItem) (Object) curr).notsoshadowextras$removeSlot((Inventory) this, slot);
        }
    }

    @Inject(method = "setStack", at = @At("HEAD"))
    public void notsoshadowextras$trackSet(int slot, ItemStack next, CallbackInfo ci) {
        ItemStack curr = getStack(slot);
        if (((ShadowItem) (Object) curr).notsoshadowextras$getShadowUuid() != null) {
            ((ShadowItem) (Object) curr).notsoshadowextras$removeSlot((Inventory) this, slot);
        }
        if (((ShadowItem) (Object) next).notsoshadowextras$getShadowUuid() != null) {
            ((ShadowItem) (Object) next).notsoshadowextras$addSlot((Inventory) this, slot);
        }
    }
}
