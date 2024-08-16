package ru.nern.notsoshadowextras.mixin;

/*
This is for testing purposes only
@Mixin(InputSlotFiller.class)

public class InputSlotFillerMixin<C extends Inventory> {
    @Shadow protected AbstractRecipeScreenHandler<C> handler;

    @Shadow protected PlayerInventory inventory;




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

 */

