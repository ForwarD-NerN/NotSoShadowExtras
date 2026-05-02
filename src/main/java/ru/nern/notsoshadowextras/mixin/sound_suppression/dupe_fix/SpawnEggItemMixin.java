package ru.nern.notsoshadowextras.mixin.sound_suppression.dupe_fix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", ordinal = 0))
    private void notsoshadowextras$decrement(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        context.getItemInHand().shrink(1);
    }

    @WrapWithCondition(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", ordinal = 0)
    )
    private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack instance, int amount) {
        return false;
    }
}
