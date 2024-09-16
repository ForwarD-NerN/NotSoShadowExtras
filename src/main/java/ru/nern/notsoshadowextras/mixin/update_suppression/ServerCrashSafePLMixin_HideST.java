package ru.nern.notsoshadowextras.mixin.update_suppression;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.listener.ServerCrashSafePacketListener;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(ServerCrashSafePacketListener.class)
public interface ServerCrashSafePLMixin_HideST {
    @WrapWithCondition(method = "onPacketException",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
    private boolean notsoshadowextras$hideStackTrace(Logger instance, String string, Object o, Object o2) {
        return false;
    }
}
