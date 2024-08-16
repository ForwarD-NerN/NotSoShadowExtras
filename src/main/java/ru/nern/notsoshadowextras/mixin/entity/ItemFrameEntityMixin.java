package ru.nern.notsoshadowextras.mixin.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

    @WrapWithCondition(
            method = "interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V")
    )
    private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack instance, int amount, LivingEntity entity) {
        return !NSSE.config().Update_Suppression.DupeFix;
    }

    @ModifyArg(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setHeldItemStack(Lnet/minecraft/item/ItemStack;)V"), index = 0)
    private ItemStack notsoshadowextras$copyHeldStack(ItemStack stack) {
        return NSSE.config().Update_Suppression.DupeFix ? stack.copy() : stack;
    }


    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setHeldItemStack(Lnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void notsoshadowextras$consumeItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir, ItemStack itemStack) {
        if(NSSE.config().Update_Suppression.DupeFix) itemStack.decrementUnlessCreative(1, player);
    }
}
