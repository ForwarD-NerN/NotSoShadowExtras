package ru.nern.notsoshadowextras.mixin.update_suppression.damage_fix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PumpkinBlock.class)
public class PumpkinBlockMixin {
    @Inject(method = "useItemOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 0))
    private void notsoshadowextras$damageShears(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        player.getItemInHand(hand).hurtAndBreak(1, player, hand.asEquipmentSlot());
    }

    @WrapWithCondition(
            method = "useItemOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V")
    )
    private boolean notsoshadowextras$wrapDamageWithCondition(ItemStack stack, int amount, LivingEntity entity, EquipmentSlot slot) {
        return false;
    }
}
