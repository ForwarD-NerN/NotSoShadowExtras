package ru.nern.notsoshadowextras.mixin.items.count;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(Container.class)
public interface ContainerMixin {
    @ModifyConstant(method = "getMaxStackSize", constant = @Constant(intValue = 99))
    private int notsoshadowextras$maxCountPerStack(int value) {
        return NSSE.config().Items.MaxCountPerStack;
    }
}
