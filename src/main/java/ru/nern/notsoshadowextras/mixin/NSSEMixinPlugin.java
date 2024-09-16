package ru.nern.notsoshadowextras.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import ru.nern.fconfiglib.v1.MixinConfigHelper;
import ru.nern.notsoshadowextras.NSSE;

import java.util.List;
import java.util.Set;

public class NSSEMixinPlugin implements IMixinConfigPlugin {
    private MixinConfigHelper helper;

    @Override
    public void onLoad(String mixinPackage) {
        NSSE.configManager.init();
        this.helper = new MixinConfigHelper(mixinPackage)
                .init(NSSE.configManager);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        //NSSE.LOGGER.info("Mixin: {}. Should be applied: {}", mixinClassName, this.helper.shouldApplyMixin(mixinClassName));
        return this.helper.shouldApplyMixin(mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
