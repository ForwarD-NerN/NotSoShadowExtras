package ru.nern.notsoshadowextras.mixin.dupe_fix.sound;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;)V", ordinal = 0))
    private void notsoshadowextras$decrement(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(NSSE.config().Sound_Suppression.DupeFix) context.getStack().decrement(1);
    }

    @WrapWithCondition(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V", ordinal = 0)
    )
    private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack instance, int amount) {
        return !NSSE.config().Sound_Suppression.DupeFix;
    }
}
