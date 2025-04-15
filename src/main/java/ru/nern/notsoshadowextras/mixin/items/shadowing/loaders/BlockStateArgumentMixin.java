package ru.nern.notsoshadowextras.mixin.items.shadowing.loaders;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.nern.notsoshadowextras.util.ShadowItem;

@Mixin(BlockStateArgument.class)
public abstract class BlockStateArgumentMixin {

    @Redirect(method = "setBlockState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;read(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)V"))
    public void notsoshadowextras$interceptBlockEntityLoad(BlockEntity instance, NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup){
        ShadowItem.searchInNbt(registryLookup, instance, nbt);
    }
}
