package ru.nern.notsoshadowextras.mixin.items.count;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @ModifyConstant(method = "method_57371", constant = @Constant(intValue = 99))
    private static int nospatches$maxCountPerStack(int value) {
        return NSSE.config.items.maxCountPerStack;
    }
}
