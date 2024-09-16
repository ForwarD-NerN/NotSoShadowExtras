package ru.nern.notsoshadowextras.mixin.update_suppression.dupe_fix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//Fixes the generic any block dupe
@Mixin(BlockItem.class)
public abstract class BlockItemMixin {


    //You know what? Before, I thought that this was a damn easy fix. Just swapping lines around. I was wrong! This thing broke 2 times already. So maybe, just maybe. This best solution is to just try catch it all and call it a day
    @WrapOperation(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z")
    )
    private boolean notsoshadowextras$catchException(World world, BlockPos pos, BlockState state, int flags, Operation<Boolean> original) {
        try {
            return original.call(world, pos, state, flags);
        }catch (Exception ignored) {
            return true;
        }
    }

    @WrapOperation(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;copyComponentsToBlockEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V")
    )
    private void notsoshadowextras$catchException(World world, BlockPos pos, ItemStack stack, Operation<Void> original) {
        try {
            original.call(world, pos, stack);
        }catch (Exception ignored) {}
    }
}
