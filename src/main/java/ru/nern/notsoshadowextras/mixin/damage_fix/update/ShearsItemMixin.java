package ru.nern.notsoshadowextras.mixin.damage_fix.update;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", ordinal = 0))
    private void notsoshadowextras$damageShears(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(NSSE.config().Update_Suppression.ItemDamageFix && context.getPlayer() != null)
            context.getStack().damage(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
    }

    @WrapWithCondition(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V")
    )
    private boolean notsoshadowextras$wrapDamageWithCondition(ItemStack instance, int amount, LivingEntity entity, EquipmentSlot slot) {
        return !NSSE.config().Update_Suppression.ItemDamageFix;
    }
}
