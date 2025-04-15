package ru.nern.notsoshadowextras.mixin.items.shadowing.loaders;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.CloneCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.nern.notsoshadowextras.util.ShadowItem;

@Mixin(CloneCommand.class)
public abstract class CloneCommandMixin {

    @Redirect(method = "execute",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;readComponentlessNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)V"))
    private static void interceptBlockEntityLoad(BlockEntity instance, NbtCompound nbt, RegistryWrapper.WrapperLookup registries){
        ShadowItem.searchInNbt(registries, instance, nbt);
    }
}
