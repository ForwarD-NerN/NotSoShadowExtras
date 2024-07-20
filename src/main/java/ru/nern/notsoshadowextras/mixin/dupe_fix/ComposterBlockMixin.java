package ru.nern.notsoshadowextras.mixin.dupe_fix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NSSE;

//We're decrementing a real stack before addToComposter and passing a copy next
@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
    @ModifyVariable(method = "compost", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ComposterBlock;addToComposter(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/block/BlockState;", shift = At.Shift.BEFORE),
            argsOnly = true)
    private static ItemStack nospatches$decrementItemCount(ItemStack realStack) {
        if(!NSSE.config.blocks.updateSuppressionDupeFix) return realStack;
        ItemStack fakeStack = realStack.copy();
        realStack.decrement(1);
        return fakeStack;
    }

    @ModifyVariable(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ComposterBlock;addToComposter(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/block/BlockState;", shift = At.Shift.BEFORE),
            argsOnly = true)
    private ItemStack nospatches$decrementItemCount2(ItemStack stack, @Local(argsOnly = true) PlayerEntity player) {
        if(!NSSE.config.blocks.updateSuppressionDupeFix) return stack;
        ItemStack fakeStack = stack.copy();
        stack.decrementUnlessCreative(1, player);
        return fakeStack;
    }
}
