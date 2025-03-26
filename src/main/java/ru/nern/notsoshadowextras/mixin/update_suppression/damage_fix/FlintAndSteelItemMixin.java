package ru.nern.notsoshadowextras.mixin.update_suppression.damage_fix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 0))
    private void notsoshadowextras$damageFlintAndSteelCampfire(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(context.getPlayer() != null)
            context.getStack().damage(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
    }

    @WrapWithCondition(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V", ordinal = 0)
    )
    private boolean notsoshadowextras$wrapDamageWithConditionCampfire(ItemStack stack, int amount, LivingEntity entity, EquipmentSlot slot) {
        return false;
    }

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    private void notsoshadowextras$damageFlintAndSteel(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(context.getPlayer() instanceof ServerPlayerEntity) {
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();
            Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)playerEntity, context.getBlockPos(), itemStack);
            itemStack.damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
        }
    }

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;)V", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void notsoshadowextras$cancelFlintAndSteelDamage(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
