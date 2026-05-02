package ru.nern.notsoshadowextras.mixin.sound_suppression;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    @Inject(method = "eat", at = @At(value = "RETURN", ordinal = 1))
    private static void notsoshadowextras$emitGameEvent(LevelAccessor world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<InteractionResult> cir) {
       world.gameEvent(player, GameEvent.EAT, pos);
    }

    @WrapWithCondition(
            method = "eat",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", ordinal = 0)
    )
    private static boolean notsoshadowextras$wrapDecrementWithCondition(LevelAccessor instance, @Nullable Entity entity, Holder<GameEvent> event, BlockPos pos) {
        return false;
    }
}
