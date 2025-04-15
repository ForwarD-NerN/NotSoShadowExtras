package ru.nern.notsoshadowextras.mixin.items.shadowing.loaders;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nern.notsoshadowextras.util.ShadowItem;
import ru.nern.notsoshadowextras.util.ShadowUtil;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Inject(method = "createFromNbt", at = @At("RETURN"))
    private static void interceptBlockEntityLoad(BlockPos pos, BlockState state, NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfoReturnable<BlockEntity> cir){
        if (cir.getReturnValue() instanceof Inventory inv) {
            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).notsoshadowextras$getShadowUuid() != null) {
                        ((ShadowItem) (Object) stack).notsoshadowextras$addSlot(inv, index);
                        inv.setStack(index, ShadowUtil.getOrAdd(((ShadowItem) (Object) stack).notsoshadowextras$getShadowUuid(), stack));
                    }
                }
            } catch (Exception ignored) {}
        }
    }

}
