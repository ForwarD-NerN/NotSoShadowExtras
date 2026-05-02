package ru.nern.notsoshadowextras.mixin.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CopperBulbBlock.class)
public abstract class CopperBulbBlockMixin extends Block {

    @Shadow public abstract void checkAndFlip(BlockState state, ServerLevel world, BlockPos pos);

    public CopperBulbBlockMixin(Properties settings) {
        super(settings);
    }

    @Redirect(
            method = {"onPlace", "neighborChanged"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CopperBulbBlock;checkAndFlip(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V")
    )
    private void notsoshadowextras$bringBackCopperBulb1gt(CopperBulbBlock instance, BlockState state, ServerLevel world, BlockPos pos) {
        world.scheduleTick(pos, this, 1);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.checkAndFlip(state, world, pos);
    }
}
