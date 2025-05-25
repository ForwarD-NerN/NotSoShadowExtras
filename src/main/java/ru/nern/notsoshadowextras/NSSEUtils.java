package ru.nern.notsoshadowextras;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
    public static void alertDimensionAboutCrash(ServerWorld world, UpdateSuppressionReason reason) {
        world.getServer().getPlayerManager().sendToDimension(
                new GameMessageS2CPacket(Text.literal(String.format("%s Suppression crash just occurred.", reason.getName())).formatted(Formatting.GRAY), false), world.getRegistryKey());
    }

    @Unique
    public static void alertEveryoneAboutCrash(MinecraftServer server, UpdateSuppressionReason reason) {
        server.getPlayerManager().sendToAll(new GameMessageS2CPacket(Text.literal(String.format("%s Suppression crash just occurred.", reason.getName())).formatted(Formatting.GRAY), false));
    }
}
