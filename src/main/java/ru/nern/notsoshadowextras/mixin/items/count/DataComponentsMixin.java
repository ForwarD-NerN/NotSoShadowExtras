package ru.nern.notsoshadowextras.mixin.items.count;

import net.minecraft.core.component.DataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(DataComponents.class)
public class DataComponentsMixin {
    @ModifyConstant(method = "method_58570", constant = @Constant(intValue = 99))
    private static int notsoshadowextras$maxCountPerStack(int value) {
        return NSSE.config().Items.MaxCountPerStack;
    }
}
