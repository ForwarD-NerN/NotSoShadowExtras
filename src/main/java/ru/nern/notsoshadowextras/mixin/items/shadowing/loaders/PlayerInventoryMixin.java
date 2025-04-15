package ru.nern.notsoshadowextras.mixin.items.shadowing.loaders;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.util.ShadowItem;
import ru.nern.notsoshadowextras.util.ShadowUtil;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow @Final private DefaultedList<ItemStack> main;

    @Shadow public abstract ItemStack getStack(int slot);

    @Inject(method = "setStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    public void notsoshadowextras$setTracker(int slot, ItemStack stack, CallbackInfo ci) {
        if (((ShadowItem) (Object) stack).notsoshadowextras$getShadowUuid() != null) {
            ((ShadowItem) (Object) stack).notsoshadowextras$addSlot((Inventory) (Object) this, slot);
            main.set(slot, ShadowUtil.getOrAdd(((ShadowItem) (Object) stack).notsoshadowextras$getShadowUuid(), stack));
        }
    }

    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    public void notsoshadowextras$removeTracker(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack curr = getStack(slot);

        if (((ShadowItem) (Object) curr).notsoshadowextras$getShadowUuid() != null) {
            ((ShadowItem) (Object) curr).notsoshadowextras$removeSlot((Inventory) (Object) this, slot);
        }
    }
}
