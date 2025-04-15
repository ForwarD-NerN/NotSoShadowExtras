package ru.nern.notsoshadowextras.mixin.items.shadowing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import ru.nern.notsoshadowextras.NSSE;
import ru.nern.notsoshadowextras.util.ShadowItem;
import ru.nern.notsoshadowextras.util.ShadowUtil;

import java.util.UUID;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow public abstract Slot getSlot(int index);

    @Shadow public abstract ItemStack getCursorStack();

    @WrapOperation(method = "onSlotClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;internalOnSlotClick(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private void notsoshadowextras$handle_shadowing(ScreenHandler instance, int slotIndex, int button, SlotActionType actionType, PlayerEntity player, Operation<Void> original) {
        try {
            original.call(instance, slotIndex, button, actionType, player);
        } catch (Throwable error) {
            if (actionType != SlotActionType.SWAP &&
                    actionType != SlotActionType.PICKUP &&
                    actionType != SlotActionType.QUICK_CRAFT)
                throw error;

            ItemStack stack1 = this.getSlot(slotIndex).getStack();
            ItemStack stack2 = player.getInventory().getStack(button);
            ItemStack stack3 = this.getCursorStack();
            ItemStack shadow = null;

            // Compare stacks to check them for successful item shadow
            if (stack1 == stack2 || stack1 == stack3)
                shadow = stack1;
            else if (stack2 == stack3)
                shadow = stack2;

            if (shadow != null){
                NSSE.LOGGER.debug("New Shadow Item Created");
                UUID shadowUuid = ((ShadowItem) (Object) shadow).notsoshadowextras$getShadowUuid();
                if (shadowUuid == null)
                    shadowUuid = UUID.randomUUID();
                ShadowUtil.getOrAdd(shadowUuid, shadow);
            }
        }
    }
}
