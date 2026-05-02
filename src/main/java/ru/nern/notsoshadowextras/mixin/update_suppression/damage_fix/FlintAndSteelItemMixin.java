package ru.nern.notsoshadowextras.mixin.update_suppression.damage_fix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 0))
    private void notsoshadowextras$damageFlintAndSteelCampfire(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if(context.getPlayer() != null)
            context.getItemInHand().hurtAndBreak(1, context.getPlayer(), context.getHand().asEquipmentSlot());
    }

    @WrapWithCondition(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", ordinal = 0)
    )
    private boolean notsoshadowextras$wrapDamageWithConditionCampfire(ItemStack stack, int amount, LivingEntity entity, EquipmentSlot slot) {
        return false;
    }

    @Inject(method = "useOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlockState(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 1))
    private void notsoshadowextras$damageFlintAndSteel(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if(context.getPlayer() instanceof ServerPlayer) {
            Player playerEntity = context.getPlayer();
            ItemStack itemStack = context.getItemInHand();
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)playerEntity, context.getClickedPos(), itemStack);
            itemStack.hurtAndBreak(1, playerEntity, context.getHand().asEquipmentSlot());
        }
    }

    @Inject(method = "useOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;emitGameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void notsoshadowextras$cancelFlintAndSteelDamage(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
