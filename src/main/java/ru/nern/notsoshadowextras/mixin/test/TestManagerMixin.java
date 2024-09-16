package ru.nern.notsoshadowextras.mixin.test;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.test.TestManager;
import net.minecraft.util.crash.CrashException;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TestManager.class)
public class TestManagerMixin {
    //Used to catch exceptions in game tests. Useful when testing update suppression, and yes, I didn't come with anything better
    @WrapMethod(method = "tick")
    private void notsoshadowextras$gameTestSuppressionCrashFix(Operation<Void> original) {
        try {
            original.call();
        } catch (StackOverflowError | ClassCastException | IllegalArgumentException | CrashException ignored) {}
    }
}
