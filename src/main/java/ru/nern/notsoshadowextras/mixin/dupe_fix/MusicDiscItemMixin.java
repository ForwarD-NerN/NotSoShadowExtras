package ru.nern.notsoshadowextras.mixin.dupe_fix;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
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

@Mixin(MusicDiscItem.class)
public class MusicDiscItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/entity/JukeboxBlockEntity;setStack(Lnet/minecraft/item/ItemStack;)V"))
    private void notsoshadowextra$consumeItem(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix) context.getStack().decrement(1);
    }

    @WrapWithCondition(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
    )
    private boolean notsoshadowextra$wrapDecrementWithCondition(ItemStack stack, int amount) {
        return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
    }
}
