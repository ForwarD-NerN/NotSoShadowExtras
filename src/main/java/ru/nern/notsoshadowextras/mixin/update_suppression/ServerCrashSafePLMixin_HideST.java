package ru.nern.notsoshadowextras.mixin.update_suppression;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.protocol.game.ServerPacketListener;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPacketListener.class)
public interface ServerCrashSafePLMixin_HideST {
    @WrapWithCondition(method = "onPacketError",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    private boolean notsoshadowextras$hideStackTrace(Logger instance, String string, Object o, Object o2) {
        return false;
    }
}
