package ru.nern.notsoshadowextras.mixin.items.count;

import net.minecraft.component.DataComponentTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(DataComponentTypes.class)
public class DataComponentTypesMixin {
    @ModifyConstant(method = "method_58570", constant = @Constant(intValue = 99))
    private static int nospatches$maxCountPerStack(int value) {
        return NSSE.config().Items.MaxCountPerStack;
    }
}
