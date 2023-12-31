package ru.nern.notsoshadowextras.mixin.dupe_fix;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

//Fixes the generic any block dupe
@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    //Mojang: we have save code at home
    //Save code at home:
    @Shadow protected abstract @Nullable BlockState getPlacementState(ItemPlacementContext context);

    @WrapWithCondition(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
    )
    private boolean notsoshadowextra$wrapDecrementWithCondition(ItemStack stack, int amount, ItemPlacementContext context) {
        return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z"))
    private void notsoshadowextra$moveBeforeBlockPlacement(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix && (context.getPlayer() == null || !context.getPlayer().getAbilities().creativeMode)) context.getStack().decrement(1);
    }
}
