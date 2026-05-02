package ru.nern.notsoshadowextras.mixin.update_suppression.dupe_fix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {

    @WrapWithCondition(
            method = "interact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V")
    )
    private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack instance, int amount, LivingEntity entity) {
        return false;
    }

    @ModifyArg(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;setItem(Lnet/minecraft/world/item/ItemStack;)V"), index = 0)
    private ItemStack notsoshadowextras$copyHeldStack(ItemStack stack) {
        return stack.copy();
    }


    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;setItem(Lnet/minecraft/world/item/ItemStack;)V"))
    private void notsoshadowextras$consumeItem(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir, @Local ItemStack itemStack) {
        itemStack.consume(1, player);
    }
}
