package ru.nern.notsoshadowextras.mixin.sound_suppression;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    @Inject(method = "tryEat", at = @At(value = "RETURN", ordinal = 1))
    private static void notsoshadowextras$emitGameEvent(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
       world.emitGameEvent(player, GameEvent.EAT, pos);
    }

    @WrapWithCondition(
            method = "tryEat",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;)V", ordinal = 0)
    )
    private static boolean notsoshadowextras$wrapDecrementWithCondition(WorldAccess instance, @Nullable Entity entity, RegistryEntry<GameEvent> event, BlockPos pos) {
        return false;
    }
}
