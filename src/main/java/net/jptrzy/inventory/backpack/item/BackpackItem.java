package net.jptrzy.inventory.backpack.item;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.screen.BackpackScreenHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends DyeableArmorItem implements ExtendedScreenHandlerFactory {

    private static final Text TITLE = new TranslatableText("container." + Main.MOD_ID + ".backpack");
//    public SimpleInventory inventory = new SimpleInventory(27);

    public BackpackItem() {
        super(
                ArmorMaterials.LEATHER,
                EquipmentSlot.CHEST,
                new Item.Settings().group(ItemGroup.COMBAT)
        );
    }

//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack itemStack = user.getStackInHand(hand);
//
//        if(!(itemStack.getItem() instanceof BackpackItem)){
//            return TypedActionResult.pass(itemStack);
//        }
//
//        if (world.isClient) {
//            return TypedActionResult.success(itemStack, true);
//        }
//
////        user.openHandledScreen(((BackpackItem) itemStack.getItem()).createScreenHandlerFactory());
////        ((ServerWorldAccess) world).getPlayerByUuid(user.getUuid()).openHandledScreen(itemStack);
//        user.openHandledScreen(this);
//
//        return TypedActionResult.success(itemStack, true);
//    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new BackpackScreenHandler(syncId, player);
    }

//    public NamedScreenHandlerFactory createScreenHandlerFactory() {
//        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new BackpackScreenHandler(i, playerEntity), TITLE);
//    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
    }

    @Override
    public Text getDisplayName() {
        return null;
    }

    public static boolean isWearingIt(PlayerEntity player){
        return player.getInventory().armor.get(2).getItem() instanceof BackpackItem;
    }

    public static ItemStack getIt(PlayerEntity player){
        return player.getInventory().armor.get(2);
    }

    public static void requestBackpackMenu(boolean open){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        NbtCompound tag = new NbtCompound();
        tag.putBoolean("Open", open);
        buf.writeNbt(tag);
        ClientPlayNetworking.send(Main.id("open_backpack"), buf);
    }
}
