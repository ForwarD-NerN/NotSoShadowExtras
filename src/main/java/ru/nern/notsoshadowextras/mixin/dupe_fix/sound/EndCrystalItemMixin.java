package ru.nern.notsoshadowextras.mixin.dupe_fix.sound;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;)V"))
    private void notsoshadowextras$decrement(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(NSSE.config().Sound_Suppression.DupeFix) context.getStack().decrement(1);
    }

    @WrapWithCondition(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
    )
    private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack instance, int amount, @Local(argsOnly = true) ItemUsageContext context) {
        if(context.getWorld().isClient) {
            return true;
        }
        return !NSSE.config().Sound_Suppression.DupeFix;
    }
}
