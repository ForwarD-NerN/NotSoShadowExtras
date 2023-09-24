package ru.nern.notsoshadowextras.mixin.entity;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {
    @WrapWithCondition(
            method = "interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
    )
    private boolean notsoshadowextras_wrapDecrementWithCondition(ItemStack stack, int amount) {
        return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
    }

    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setHeldItemStack(Lnet/minecraft/item/ItemStack;)V"))
    private void notsoshadowextras_moveBeforeBlockUpdate(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix && !player.getAbilities().creativeMode) player.getStackInHand(hand).decrement(1);
    }
}
