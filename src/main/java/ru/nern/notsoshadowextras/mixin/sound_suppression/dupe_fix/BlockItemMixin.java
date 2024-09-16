package ru.nern.notsoshadowextras.mixin.sound_suppression.dupe_fix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V"))
    private void notsoshadowextras$decrement(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        context.getStack().decrementUnlessCreative(1, context.getPlayer());
    }

    @WrapWithCondition(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V")
    )
    private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack instance, int amount, LivingEntity entity) {
        return false;
    }
}
