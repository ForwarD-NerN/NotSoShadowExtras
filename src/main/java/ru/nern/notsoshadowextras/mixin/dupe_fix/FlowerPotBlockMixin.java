package ru.nern.notsoshadowextras.mixin.dupe_fix;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

//Fixes the flower dupe
@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin {
    @Inject(method = "onUse", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0))
    private void notsoshadowextra$consumeItem(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix && !player.getAbilities().creativeMode) player.getStackInHand(hand).decrement(1);
    }

    @WrapWithCondition(
            method = "onUse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
    )
    private boolean notsoshadowextra$wrapDecrementWithCondition(ItemStack stack, int amount) {
        return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
    }
}
