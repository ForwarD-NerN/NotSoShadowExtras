package ru.nern.notsoshadowextras.mixin.additions;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    // Swapping the block entity of a newly placed block if there is StoredBlockEntityTag(for /nsse swap to work)
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void notsoshadowextras$swapBlockEntity(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = context.getStack();
        if(context.getWorld().isClient || !stack.getComponents().contains(DataComponentTypes.CUSTOM_DATA)) return;

        Optional<String> storedBlockEntity = stack.getComponents().get(DataComponentTypes.CUSTOM_DATA).copyNbt().getString("StoredBlockEntity");

        if(storedBlockEntity.isEmpty()) return;
        Identifier identifier = Identifier.tryParse(storedBlockEntity.get());
        if(identifier == null) return;

        BlockPos pos = context.getBlockPos();

        Optional<BlockEntity> blockEntity = Registries.BLOCK_ENTITY_TYPE.getOptionalValue(identifier).map(type -> {
            try {
                return !type.blocks.isEmpty() ? type.instantiate(context.getBlockPos(), type.blocks.iterator().next().getDefaultState()) : null;
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
