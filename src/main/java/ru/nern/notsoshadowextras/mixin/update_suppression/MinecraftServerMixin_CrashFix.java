package ru.nern.notsoshadowextras.mixin.update_suppression;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.crash.CrashException;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import ru.nern.notsoshadowextras.NSSE;
import ru.nern.notsoshadowextras.crash_fix.UpdateSuppressionReason;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin_CrashFix {

    @Shadow @Final private static Logger LOGGER;

    @WrapOperation(
            method = "tickWorlds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V")
    )
    private void notsoshadowextras$updateSuppressionCrashFix(ServerWorld world, BooleanSupplier shouldKeepTicking, Operation<Void> original) {
        try {
            original.call(world, shouldKeepTicking);
        }catch (StackOverflowError | ClassCastException | IllegalArgumentException | CrashException error) {
            if(!NSSE.config().Update_Suppression.HideStackTrace) LOGGER.error("Exception occurred during world ticking", error);
            if(NSSE.config().Update_Suppression.AlertAboutCrash) this.alertDimensionAboutCrash(world, getReason(error.getCause()));
        }
    }

    @Unique
    private static UpdateSuppressionReason getReason(Throwable cause) {
        if(cause instanceof StackOverflowError) {
            return UpdateSuppressionReason.SO;
        }else if(cause instanceof ClassCastException) {
            return UpdateSuppressionReason.CCE;
        }else if(cause instanceof OutOfMemoryError) {
            return UpdateSuppressionReason.OOM;
        }else if(cause instanceof IllegalArgumentException) {
            return UpdateSuppressionReason.SOUND;
        }
        return UpdateSuppressionReason.UNKNOWN;
    }

    @Unique
    private void alertDimensionAboutCrash(ServerWorld world, UpdateSuppressionReason reason) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        server.getPlayerManager().sendToDimension(
                new GameMessageS2CPacket(Text.literal(String.format("%s Suppression crash just occurred.", reason.getName())).formatted(Formatting.GRAY), false), world.getRegistryKey());
    }
}
