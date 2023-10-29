package ru.nern.notsoshadowextras.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InputSlotFiller.class)
public class InputSlotFillerMixin<C extends Inventory> {
    @Shadow protected AbstractRecipeScreenHandler<C> handler;

    @Shadow protected PlayerInventory inventory;

    /**
     * @author f
     * @reason f
     */
    @Overwrite
    public void returnInputs() {
        for (int i = 0; i < this.handler.getCraftingSlotCount(); ++i) {
            if (!this.handler.canInsertIntoSlot(i)) continue;
            ItemStack itemStack = this.handler.getSlot(i).getStack().copy();

            this.handler.getSlot(i).setStackNoCallbacks(itemStack);
            this.inventory.offer(itemStack, false);
        }
        this.handler.clearCraftingSlots();
    }
}
