package ru.nern.notsoshadowextras.mixin.blocks;

import net.minecraft.block.CrafterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {
    @ModifyConstant(method = {"onPlaced", "neighborUpdate"}, constant = @Constant(intValue = 4))
    private int notsoshadowextras$bringBackCrafter1gt(int value) {
        return 1;
    }
}
