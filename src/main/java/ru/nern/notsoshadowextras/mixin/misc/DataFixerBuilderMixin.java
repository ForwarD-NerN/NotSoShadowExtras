package ru.nern.notsoshadowextras.mixin.misc;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixerBuilder;
import net.minecraft.datafixer.fix.ChunkDeleteLightFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.notsoshadowextras.NSSE;

@Mixin(value = DataFixerBuilder.class, remap = false)
public class DataFixerBuilderMixin {

    //Just prevents ChunkDeleteLightFix from being added to the df list and prevents light recalculation from occurring when updating to 1.20
    @Inject(at = @At("HEAD"), method = "addFixer", cancellable = true)
    private void notsoshadowextras$disableDeleteLightDataFixer(DataFix fix, CallbackInfo ci) {
        if(fix instanceof ChunkDeleteLightFix) ci.cancel();
    }
}
