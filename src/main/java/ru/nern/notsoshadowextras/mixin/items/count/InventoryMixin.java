package ru.nern.notsoshadowextras.mixin.items.count;

import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(Inventory.class)
public interface InventoryMixin {
    @ModifyConstant(method = "getMaxCountPerStack", constant = @Constant(intValue = 99))
    private int nospatches$maxCountPerStack(int value) {
        return NSSE.config().Items.MaxCountPerStack;
    }
}
