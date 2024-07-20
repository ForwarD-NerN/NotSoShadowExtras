package ru.nern.notsoshadowextras.mixin.items.count;

import net.minecraft.server.command.ItemCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(ItemCommand.class)
public class ItemCommandMixin {
    @ModifyConstant(method = "register", constant = @Constant(intValue = 99))
    private static int nospatches$maxCountPerStack(int value) {
        return NSSE.config.items.maxCountPerStack;
    }
}
