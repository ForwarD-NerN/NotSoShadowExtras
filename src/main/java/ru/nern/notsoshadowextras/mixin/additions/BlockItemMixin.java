package ru.nern.notsoshadowextras.mixin.additions;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    // Swapping the block entity of a newly placed block if there is StoredBlockEntityTag(for /nsse swap to work)
    @Inject(method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private void notsoshadowextras$swapBlockEntity(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = context.getItemInHand();
        if(context.getLevel().isClientSide() || !stack.getComponents().has(DataComponents.CUSTOM_DATA)) return;

        Optional<String> storedBlockEntity = stack.getComponents().get(DataComponents.CUSTOM_DATA).copyTag().getString("StoredBlockEntity");

        if(storedBlockEntity.isEmpty()) return;
        Identifier identifier = Identifier.tryParse(storedBlockEntity.get());
        if(identifier == null) return;

        BlockPos pos = context.getClickedPos();

        Optional<BlockEntity> blockEntity = BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(identifier).map(type -> {
            try {
                return !type.validBlocks.isEmpty() ? type.create(context.getClickedPos(), type.validBlocks.iterator().next().defaultBlockState()) : null;
            } catch (Throwable throwable) {
                return null;
            }
        });
        if(blockEntity.isPresent()) {
            context.getLevel().removeBlockEntity(pos);
            context.getLevel().setBlockEntity(blockEntity.get());
        }
    }
}
