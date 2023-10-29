package ru.nern.notsoshadowextras.mixin.dupe_fix.damage;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

import java.util.function.Consumer;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", ordinal = 0))
    private void notsoshadowextras$damageShears(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(NotSoShadowExtras.config.blocks.updateSuppressionItemDamageFix && context.getPlayer() != null)
            context.getStack().damage(1, context.getPlayer(), p -> p.sendToolBreakStatus(context.getHand()));
    }

    @WrapWithCondition(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V")
    )
    private boolean notsoshadowextras$wrapDamageWithCondition(ItemStack stack, int amount, LivingEntity entity, Consumer<?> breakCallback) {
        return !NotSoShadowExtras.config.blocks.updateSuppressionItemDamageFix;
    }
}
