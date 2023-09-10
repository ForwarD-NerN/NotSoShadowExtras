package ru.nern.notsoshadowextras.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.crash.CrashException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @WrapOperation(
            method = "tickWorlds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V")
    )
    private void notsoshadowextras_updateSuppressionCrashFix(ServerWorld instance, BooleanSupplier shouldKeepTicking, Operation<Void> original) {
        if (NotSoShadowExtras.config.blocks.updateSuppressionCrashFix) {
            try{
                original.call(instance, shouldKeepTicking);
            }catch (CrashException exception)
            {
                if(!(exception.getCause() instanceof ClassCastException || exception.getCause() instanceof StackOverflowError)) throw exception;
                if(NotSoShadowExtras.config.blocks.alertAboutUpdateSuppressionCrash) alertDimensionAboutCrash(instance);
            }
        } else {
            original.call(instance, shouldKeepTicking);
        }
    }
    private void alertDimensionAboutCrash(ServerWorld world)
    {
        MinecraftServer server = (MinecraftServer) (Object) this;
        server.getPlayerManager().sendToDimension(
                new GameMessageS2CPacket(Text.literal("Update Suppression crash just occurred!").formatted(Formatting.GRAY), false), world.getRegistryKey());
    }
}
