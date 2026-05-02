package ru.nern.notsoshadowextras.mixin.update_suppression;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    //Fixes a dupe with Update_Suppression.CrashFix enabled. Apparently, the easiest way to fix this is just to add try catch
    @WrapOperation(
            method = "tryMoveInItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setChanged()V")
    )
    private static void notsoshadowextras$catchException(Container instance, Operation<Void> original) {
        try {
            original.call(instance);
        }catch (Exception ignored) {}
    }
}
