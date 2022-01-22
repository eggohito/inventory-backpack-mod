package net.jptrzy.inventory.backpack.integrations.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.jptrzy.inventory.backpack.Main;
import net.jptrzy.inventory.backpack.client.Client;
import net.jptrzy.inventory.backpack.client.renderer.BackpackArmorRenderer;
import net.jptrzy.inventory.backpack.client.renderer.EnderBackpackArmorRenderer;
import net.jptrzy.inventory.backpack.config.ModConfig;
import net.jptrzy.inventory.backpack.item.BackpackItem;
import net.jptrzy.inventory.backpack.util.Utils;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class EnderBackpackTrinket extends BackpackTrinket {

    public static void register() {
        EnderBackpackTrinket trinket = new EnderBackpackTrinket();
        TrinketsApi.registerTrinket(Main.ENDER_BACKPACK, trinket);
        TrinketRendererRegistry.registerRenderer(Main.ENDER_BACKPACK, trinket);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return entity instanceof PlayerEntity && ModConfig.trinkets;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.world.isClient() && entity instanceof PlayerEntity
                && ((PlayerEntity) entity).currentScreenHandler instanceof PlayerScreenHandler){
            Utils.onEquip((ServerPlayerEntity) entity, stack);
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.world.isClient() && entity instanceof PlayerEntity
                && ((PlayerEntity) entity).currentScreenHandler instanceof PlayerScreenHandler){
            Utils.onUnEquip((ServerPlayerEntity) entity, stack);
        }
    }

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        BipedEntityModel<LivingEntity> model = Client.ENDER_BACKPACK_ARMOR_RENDERER.getModel();

        model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
        model.animateModel(entity, limbAngle, limbDistance, tickDelta);

        TrinketRenderer.followBodyRotations(entity, model);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(EnderBackpackArmorRenderer.BACKPACK_MODEL_TEXTURE));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }
}
