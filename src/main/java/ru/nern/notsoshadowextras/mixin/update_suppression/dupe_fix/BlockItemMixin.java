package ru.nern.notsoshadowextras.mixin.update_suppression.dupe_fix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//Fixes the generic any block dupe
@Mixin(BlockItem.class)
public abstract class BlockItemMixin {


    //You know what? Before, I thought that this was a damn easy fix. Just swapping lines around. I was wrong! This thing broke 2 times already. So maybe, just maybe. This best solution is to just try catch it all and call it a day
    @WrapOperation(
            method = "placeBlock(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z")
    )
    private boolean notsoshadowextras$catchException(Level world, BlockPos pos, BlockState state, int flags, Operation<Boolean> original) {
        try {
            return original.call(world, pos, state, flags);
        }catch (Exception ignored) {
            return true;
        }
    }

    @WrapOperation(
            method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;updateBlockEntityComponents(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V")
    )
    private void notsoshadowextras$catchException(Level world, BlockPos pos, ItemStack stack, Operation<Void> original) {
        try {
            original.call(world, pos, stack);
        }catch (Exception ignored) {}
    }
}
