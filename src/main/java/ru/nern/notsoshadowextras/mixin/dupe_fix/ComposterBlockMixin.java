package ru.nern.notsoshadowextras.mixin.dupe_fix;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
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

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
    @Inject(method = "onUse", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/ComposterBlock;addToComposter(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/block/BlockState;"))
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
