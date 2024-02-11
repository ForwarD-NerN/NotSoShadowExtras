package ru.nern.notsoshadowextras.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    //Fixes a dupe with updateSuppressionCrashFix enabled. I don't know if it has any side effects, so I made it a separate rule.
    @WrapWithCondition(
            method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;markDirty()V")
    )
    private static boolean notsoshadowextras$hopperCrashFixDupeFix(Inventory instance) {
        return !NotSoShadowExtras.config.blocks.updateSuppressionHopperDupeFix;
    }
}
