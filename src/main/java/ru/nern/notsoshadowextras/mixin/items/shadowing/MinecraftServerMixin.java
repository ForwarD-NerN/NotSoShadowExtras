package ru.nern.notsoshadowextras.mixin.items.shadowing;

import net.minecraft.inventory.Inventory;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.notsoshadowextras.NSSE;
import ru.nern.notsoshadowextras.util.ShadowUtil;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V", shift = At.Shift.AFTER))
    public void notsoshadowextras$afterWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        try {
            for (Inventory inv : ShadowUtil.UPDATE_SET) {
                try {
                    inv.markDirty();
                } catch (Throwable ex) {
                    NSSE.LOGGER.error("Caught Exception while propagating shadow stack updates: ", ex);
                }
            }
        } catch (Throwable error) {
            NSSE.LOGGER.error("Caught Exception while propagating shadow stack updates: ", error);
        } finally {
            ShadowUtil.UPDATE_SET.clear();
        }
    }
}
