package ru.nern.notsoshadowextras.mixin.additions;

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
    // Swapping the block entity of a newly placed block if there is StoredBlockEntityTag(used in /nsse swap)
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
