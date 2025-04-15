package ru.nern.notsoshadowextras.mixin.items.shadowing.loaders;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.BlockDataObject;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.nern.notsoshadowextras.util.ShadowItem;

@Mixin(BlockDataObject.class)
public abstract class BlockDataObjectMixin {

    @Redirect(method = "setNbt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;read(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)V"))
    private void interceptBlockEntityLoad(BlockEntity instance, NbtCompound nbt, RegistryWrapper.WrapperLookup registries){
        ShadowItem.searchInNbt(registries, instance, nbt);
    }
}
