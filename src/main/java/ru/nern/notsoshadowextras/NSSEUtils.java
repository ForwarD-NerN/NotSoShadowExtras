package ru.nern.notsoshadowextras;

import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Unique;
import ru.nern.notsoshadowextras.crash_fix.UpdateSuppressionReason;

public class NSSEUtils {
    @Unique
    public static UpdateSuppressionReason getReason(Throwable cause) {
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
    public static void alertDimensionAboutCrash(ServerLevel world, UpdateSuppressionReason reason) {
        world.getServer().getPlayerList().broadcastAll(
                new ClientboundSystemChatPacket(Component.literal(String.format("%s Suppression crash just occurred.", reason.getName())).withStyle(ChatFormatting.GRAY), false), world.dimension());
    }

    @Unique
    public static void alertEveryoneAboutCrash(MinecraftServer server, UpdateSuppressionReason reason) {
        server.getPlayerList().broadcastAll(new ClientboundSystemChatPacket(Component.literal(String.format("%s Suppression crash just occurred.", reason.getName())).withStyle(ChatFormatting.GRAY), false));
    }
}
