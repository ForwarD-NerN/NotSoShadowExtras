package ru.nern.notsoshadowextras.mixin.update_suppression.dupe_fix;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//We're decrementing a real stack before addToComposter and passing a copy next
@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
    @ModifyVariable(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;addItem(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/level/block/state/BlockState;", shift = At.Shift.BEFORE),
            argsOnly = true)
    private static ItemStack notsoshadowextras$decrementItemCount(ItemStack realStack) {
        ItemStack fakeStack = realStack.copy();
        realStack.shrink(1);
        return fakeStack;
    }

    @ModifyVariable(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;addItem(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/level/block/state/BlockState;", shift = At.Shift.BEFORE),
            argsOnly = true)
    private ItemStack notsoshadowextras$decrementItemCount2(ItemStack stack, @Local(argsOnly = true) Player player) {
        ItemStack fakeStack = stack.copy();
        stack.consume(1, player);
        return fakeStack;
    }
}
