package ru.nern.notsoshadowextras.mixin.block;

import net.minecraft.block.CrafterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {
    @ModifyConstant(method = {"onPlaced", "neighborUpdate"}, constant = @Constant(intValue = 4))
    private int nospatches$bringBackCrafter1gt(int value) {
        return NSSE.config().Blocks.Crafter_1gt ? 1 : value;
    }
}
