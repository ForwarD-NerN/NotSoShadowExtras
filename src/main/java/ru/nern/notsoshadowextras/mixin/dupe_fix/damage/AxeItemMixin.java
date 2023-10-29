package ru.nern.notsoshadowextras.mixin.dupe_fix.damage;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

import java.util.function.Consumer;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0))
    private void notsoshadowextras$damageAxe(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
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
