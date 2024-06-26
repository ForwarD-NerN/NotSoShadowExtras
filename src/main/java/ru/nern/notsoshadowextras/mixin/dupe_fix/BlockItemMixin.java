package ru.nern.notsoshadowextras.mixin.dupe_fix;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

import java.util.Optional;

//Fixes the generic any block dupe
@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    //Mojang: we have save code at home
    //Save code at home:
    @WrapWithCondition(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V")
    )
    private boolean notsoshadowextras$wrapDecrementWithCondition(ItemStack stack, int amount, LivingEntity entity) {
        return !NotSoShadowExtras.config.blocks.updateSuppressionDupeFix;
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z"))
    private void notsoshadowextras$moveBeforeBlockPlacement(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(NotSoShadowExtras.config.blocks.updateSuppressionDupeFix) context.getStack().decrementUnlessCreative(1, context.getPlayer());
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("RETURN"))
    private void notsoshadowextras$swapBlockEntity(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = context.getStack();
        if(context.getWorld().isClient || !stack.getComponents().contains(DataComponentTypes.CUSTOM_DATA)) return;

        Identifier identifier = Identifier.tryParse(stack.getComponents().get(DataComponentTypes.CUSTOM_DATA).copyNbt().getString("StoredBlockEntity"));
        if(identifier == null) return;

        BlockPos pos = context.getBlockPos();

        Optional<BlockEntity> blockEntity = Registries.BLOCK_ENTITY_TYPE.getOrEmpty(identifier).map(type -> {
            try {
                return type.instantiate(context.getBlockPos(), context.getWorld().getBlockState(pos));
            } catch (Throwable throwable) {
                return null;
            }
        });
        if(blockEntity.isPresent()) {
            context.getWorld().removeBlockEntity(pos);
            context.getWorld().addBlockEntity(blockEntity.get());
        }
    }
}
