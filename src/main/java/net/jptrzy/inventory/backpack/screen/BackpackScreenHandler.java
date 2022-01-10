package net.jptrzy.inventory.backpack.screen;

import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.mixin.ScreenHandlerAccessor;
import net.jptrzy.inventory.backpack.mixin.SlotAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class BackpackScreenHandler extends PlayerScreenHandler {

    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory.player);
    }

    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(syncId, playerInventory);
    }

    public BackpackScreenHandler(int syncId, PlayerEntity player) {
        super(player.getInventory(), !player.world.isClient(), player);


        // Without this ScreenHandler thinks that it is PlayerScreenHandler and not BackpackScreenHandler from the client site. (I've worked on it for too long.)
        ((ScreenHandlerAccessor) this).setSyncId(syncId);

        this.enableSyncing();

        Inventory inventory = player.getInventory();
        for(Slot slot : slots){
            if (slot.inventory == inventory && slot.getIndex() < inventory.size() - 5) {
                ((SlotAccessor) slot).setY(((SlotAccessor) slot).getY() + 58);
            }
        }
        Slot anchor = slots.get(9);
        int left = anchor.x;
        int top = anchor.y - 58;

        ItemStack backpack = player.getMainHandStack();
        SimpleInventory inv = ((BackpackItem) backpack.getItem()).inventory;
        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 9; ++j) {
                int k = j + i * 9+36;
                addSlot(new Slot(inv, k, left + j * 18, top + i * 18));
            }
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


    //alternatively in constructor '((ScreenHandlerAccessor) this).setType(Main.BACKPACK_SCREEN_HANDLER);'
    @Override
    public ScreenHandlerType<?> getType() {
        return Main.BACKPACK_SCREEN_HANDLER;
    }
}
