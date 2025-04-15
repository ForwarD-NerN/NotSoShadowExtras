package ru.nern.notsoshadowextras.mixin.update_suppression;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    //Fixes a dupe with Update_Suppression.CrashFix enabled. Apparently, the easiest way to fix this is just to getOrAdd try catch
    @WrapOperation(
            method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;markDirty()V")
    )
    private static void notsoshadowextras$catchException(Inventory instance, Operation<Void> original) {
        try {
            original.call(instance);
        }catch (Exception ignored) {}
    }
}
