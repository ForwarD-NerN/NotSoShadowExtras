package ru.nern.notsoshadowextras.mixin.update_suppression.dupe_fix;

//I feel like a paper dev right now
/*
public class DispenserBehaviorMixin {

    //idk why, but this thing works in the dev environment, but doesn't work in the game. Maybe I'll come back to this later.
    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$18")
    public static class RespawnAnchorChargeBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/block/RespawnAnchorBlock;charge(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
        private void notsoshadowextras$consumeGlowstone(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NSSE.config().Update_Suppression.DupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently", remap = false,
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NSSE.config().Update_Suppression.DupeFix;
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$10")
    public static class FlintAndSteelBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
        private void notsoshadowextras$damageFlintAndSteel(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            ////if(NotSoShadowExtras.config().Update_Suppression.DupeFix
                //    && stack.damage(1, pointer.world().random, null)) stack.setCount(0);
        }

        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/block/dispenser/DispenserBehavior$10;isSuccess()Z"), cancellable = true)
        private void notsoshadowextras$cancelFlintAndSteelDamage(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NSSE.config().Update_Suppression.DupeFix) cir.setReturnValue(stack);
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$15")
    public static class WitherSkullPlacementBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
        private void notsoshadowextras$removeWitherSkull(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NSSE.config().Update_Suppression.DupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently", remap = false,
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NSSE.config().Update_Suppression.DupeFix;
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$16")
    public static class CarvedPumpkinPlacementBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
        private void notsoshadowextras$removePumpkin(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NSSE.config().Update_Suppression.DupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently", remap = false,
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NSSE.config().Update_Suppression.DupeFix;
        }
    }

    @Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$19")
    public static class CopperWaxBehavior {
        @Inject(method = "dispenseSilently", remap = false, at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
        private void notsoshadowextras$removeHoneycomb(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
            if(NSSE.config().Update_Suppression.DupeFix) stack.decrement(1);
        }

        @WrapWithCondition(
                method = "dispenseSilently",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
        )
        private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack stack, int amount, BlockPointer pointer) {
            return !NSSE.config().Update_Suppression.DupeFix;
        }
    }
}

 */
