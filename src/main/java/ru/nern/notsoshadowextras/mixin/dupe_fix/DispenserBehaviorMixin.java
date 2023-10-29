package ru.nern.notsoshadowextras.mixin.dupe_fix;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

//I'm feeling like a paper dev right now
public class DispenserBehaviorMixin {

    //idk why, but this thing works in the dev environment, but doesn't work in the game. Maybe I'll come back to this later.
    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$18")
    public static class RespawnAnchorChargeBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/block/RespawnAnchorBlock;charge(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
        private void notsoshadowextra$consumeGlowstone(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently", remap = false,
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextra$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$10")
    public static class FlintAndSteelBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
        private void notsoshadowextra$damageFlintAndSteel(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix
                    && stack.damage(1, pointer.world().random, null)) stack.setCount(0);
        }

        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/block/dispenser/DispenserBehavior$10;isSuccess()Z"), cancellable = true)
        private void notsoshadowextra$cancelFlintAndSteelDamage(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix) cir.setReturnValue(stack);
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$15")
    public static class WitherSkullPlacementBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
        private void notsoshadowextra$removeWitherSkull(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently", remap = false,
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextra$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$16")
    public static class CarvedPumpkinPlacementBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
        private void notsoshadowextra$removePumpkin(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently", remap = false,
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextra$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$19")
    public static class CopperWaxBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
        private void notsoshadowextra$removeHoneycomb(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextra$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
        }
    }
}
