package ru.nern.notsoshadowextras.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.network.NetworkThreadUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

@Mixin(NetworkThreadUtils.class)
public class NetworkThreadUtilsMixin {
    @WrapWithCondition(method = "method_11072",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    private static boolean notsoshadowextras$hideStackTrace(Logger instance, String string, Object o, Object o2) {
        return !NotSoShadowExtras.config.blocks.noSuppressionStacktrace;
    }
}
