package ru.nern.notsoshadowextras.mixin.items;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.GiveCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.nern.notsoshadowextras.NotSoShadowExtras;

import java.util.Collection;
import java.util.Iterator;

@Mixin(GiveCommand.class)
public class GiveCommandMixin {
    @ModifyConstant(method = "register", constant = @Constant(intValue = 1))
    private static int notsoshadowextras$removeGiveCommandLimits(int value) {
        return NotSoShadowExtras.config.items.giveCommandUnderstackedItems ? Integer.MIN_VALUE : value;
    }

    private static ItemStack split(ItemStack org, int amount) {
        //-10 / -5
        //-15
        //1

        int i = org.getCount() > 0 ? Math.min(amount, org.getCount()) : amount;
        System.out.println("I: " +i);
        ItemStack itemStack = org.copyWithCount(i);
        org.decrement(i);
        return itemStack;
    }

    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", shift = At.Shift.BY, by = -5), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void notsoshadowextras$giveUnderstackedItems(ServerCommandSource source, ItemStackArgument item, Collection<ServerPlayerEntity> targets, int count, CallbackInfoReturnable<Integer> cir, int i, int j, ItemStack itemStack, Iterator var7, ServerPlayerEntity serverPlayerEntity, int k) throws CommandSyntaxException {
        if(k <= 0) {
            ItemEntity itemEntity;
            ItemStack itemStack2 = item.createStack(k, false);

            boolean bl = serverPlayerEntity.getInventory().insertStack(itemStack2);
            if (!bl || !itemStack2.isEmpty()) {
                itemEntity = serverPlayerEntity.dropItem(itemStack2, false);
                if (itemEntity == null) return;
                itemEntity.resetPickupDelay();
                itemEntity.setOwner(serverPlayerEntity.getUuid());
                return;
            }
            itemStack2.setCount(1);
            itemEntity = serverPlayerEntity.dropItem(itemStack2, false);
            if (itemEntity != null) {
                itemEntity.setDespawnImmediately();
            }
            serverPlayerEntity.getWorld().playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((serverPlayerEntity.getRandom().nextFloat() - serverPlayerEntity.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            serverPlayerEntity.currentScreenHandler.sendContentUpdates();
            System.out.println("TRIGGERED4");
        }
    }
}
