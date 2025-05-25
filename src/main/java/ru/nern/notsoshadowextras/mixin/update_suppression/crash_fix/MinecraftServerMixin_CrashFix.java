package ru.nern.notsoshadowextras.mixin.update_suppression.crash_fix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.DataFix;
import net.minecraft.datafixer.fix.ChunkDeleteLightFix;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.notsoshadowextras.NSSE;
import ru.nern.notsoshadowextras.NSSEUtils;
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
            if(NSSE.config().Update_Suppression.AlertAboutCrash) NSSEUtils.alertDimensionAboutCrash(world, NSSEUtils.getReason(error.getCause()));
        }
    }




}
