/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.render.IRenderer;
import tech.mmmax.kami.api.management.FriendManager;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.impl.gui.ClickGui;

public class Nametags
extends Module {
    public static Nametags INSTANCE;
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Colors").withModes("Colors", "Items", "Size").register(this);
    Value<Color> background = new ValueBuilder().withDescriptor("Background").withValue(new Color(0, 0, 0, 25)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> textColor = new ValueBuilder().withDescriptor("Text Color").withValue(new Color(255, 255, 255)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> lineColor = new ValueBuilder().withDescriptor("Line Color").withValue(new Color(0, 0, 0, 150)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Color> boxColor = new ValueBuilder().withDescriptor("Box Color").withValue(new Color(25, 25, 25, 255)).withPageParent(this.page).withPage("Colors").register(this);
    Value<Number> lineWidth = new ValueBuilder().withDescriptor("Line Width").withValue(1).withRange(0.1, 5).withPageParent(this.page).withPage("Items").register(this);
    Value<Boolean> ping = new ValueBuilder().withDescriptor("Ping").withValue(true).withPageParent(this.page).withPage("Items").register(this);
    Value<Boolean> health = new ValueBuilder().withDescriptor("Health").withValue(true).withPageParent(this.page).withPage("Items").register(this);
    Value<Boolean> hands = new ValueBuilder().withDescriptor("Hands").withValue(true).withPageParent(this.page).withPage("Items").register(this);
    Value<Boolean> armor = new ValueBuilder().withDescriptor("Armor").withValue(true).withPageParent(this.page).withPage("Items").register(this);
    Value<Boolean> playerBox = new ValueBuilder().withDescriptor("Player Box").withValue(false).withPageParent(this.page).withPage("Items").register(this);
    Value<Number> playerBoxWidth = new ValueBuilder().withDescriptor("Box Width").withValue(30).withRange(10, 60).withPageParent(this.page).withPage("Items").register(this);
    Value<Number> padding = new ValueBuilder().withDescriptor("Padding").withValue(1).withRange(0, 5).withPageParent(this.page).withPage("Size").register(this);
    Value<Number> scale = new ValueBuilder().withDescriptor("Scale").withValue(1).withRange(0, 10).withPageParent(this.page).withPage("Size").register(this);

    void handlePage(String page) {
        this.background.setActive(page.equals("Colors"));
        this.textColor.setActive(page.equals("Colors"));
        this.lineColor.setActive(page.equals("Colors"));
        this.lineWidth.setActive(page.equals("Colors"));
        this.boxColor.setActive(page.equals("Colors"));
        this.ping.setActive(page.equals("Items"));
        this.health.setActive(page.equals("Items"));
        this.hands.setActive(page.equals("Items"));
        this.armor.setActive(page.equals("Items"));
        this.playerBox.setActive(page.equals("Items"));
        this.playerBoxWidth.setActive(page.equals("Items"));
        this.padding.setActive(page.equals("Size"));
        this.scale.setActive(page.equals("Size"));
    }

    public Nametags() {
        super("Nametags", Feature.Category.Render);
        INSTANCE = this;

        this.handlePage(page.getValue());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        for (Entity entity : Nametags.mc.world.loadedEntityList) {
            EntityPlayer player;
            if (!(entity instanceof EntityPlayer) || (player = (EntityPlayer)entity).getEntityId() == Nametags.mc.player.getEntityId()) continue;
            Vec3d pos = Nametags.interpolateEntityByTicks(entity, event.getPartialTicks());
            double x = pos.x;
            double distance = pos.y + 0.65;
            double z = pos.z;
            double y = distance + (player.isSneaking() ? 0.0 : 0.08);
            Vec3d entityPos = Nametags.interpolateEntity(entity, event.getPartialTicks());
            float linWid = RenderUtil.getInterpolatedLinWid((float)Nametags.mc.player.getDistance(player.posX, player.posY, player.posZ), this.lineWidth.getValue().floatValue(), this.lineWidth.getValue().floatValue());
            GL11.glLineWidth((float)linWid);
            GL11.glDisable((int)2848);
            GlStateManager.pushMatrix();
            this.prepareTranslation(x, y, z, this.scale.getValue().doubleValue() * (Nametags.mc.player.getDistance(entityPos.x, entityPos.y, entityPos.z) / 2.0));
            GlStateManager.pushMatrix();
            String nametag = this.getNametagString(player);
            int width = ClickGui.CONTEXT.getRenderer().getTextWidth(nametag);
            int height = ClickGui.CONTEXT.getRenderer().getTextHeight(nametag);
            Rect nametagRect = new Rect(-width / 2, -height / 2, width + this.padding.getValue().intValue() * 2, height + this.padding.getValue().intValue() * 2);
            ClickGui.CONTEXT.getRenderer().renderRect(nametagRect, this.background.getValue(), this.background.getValue(), IRenderer.RectMode.Fill, ClickGui.CONTEXT);
            ClickGui.CONTEXT.getRenderer().renderRect(nametagRect, this.lineColor.getValue(), this.lineColor.getValue(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
            ClickGui.CONTEXT.getRenderer().renderText(nametag, nametagRect.getX() + this.padding.getValue().intValue(), nametagRect.getY() + this.padding.getValue().intValue(), this.textColor.getValue(), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
            ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
            if (this.hands.getValue().booleanValue()) {
                stacks.add(player.getHeldItem(EnumHand.MAIN_HAND));
            }
            if (this.armor.getValue().booleanValue()) {
                stacks.addAll((Collection<ItemStack>)player.inventory.armorInventory);
            }
            if (this.hands.getValue().booleanValue()) {
                stacks.add(player.getHeldItem(EnumHand.OFF_HAND));
            }
            int offset = this.padding.getValue().intValue();
            int offAdd = nametagRect.getWidth() / stacks.size();
            for (ItemStack stack : stacks) {
                int stackX = nametagRect.getX() + offset;
                int stackY = nametagRect.getY() - (16 + this.padding.getValue().intValue());
                this.renderStack(player, stack, stackX, stackY);
                offset += offAdd;
            }
            if (this.playerBox.getValue().booleanValue()) {
                Rect boxRect = new Rect(-this.playerBoxWidth.getValue().intValue(), nametagRect.getY() + this.padding.getValue().intValue(), this.playerBoxWidth.getValue().intValue() * 2, 100);
                ClickGui.CONTEXT.getRenderer().renderRect(boxRect, this.boxColor.getValue(), this.boxColor.getValue(), IRenderer.RectMode.Outline, ClickGui.CONTEXT);
            }
            GlStateManager.popMatrix();
            this.release();
            GlStateManager.popMatrix();
        }
    }

    void renderStack(EntityPlayer player, ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        String stackCount = stack.getCount() > 1 ? String.valueOf(stack.getCount()) : "";
        int width = ClickGui.CONTEXT.getRenderer().getTextWidth(stackCount);
        GlStateManager.enableAlpha();
        GlStateManager.disableDepth();
        ClickGui.CONTEXT.getRenderer().renderText(stackCount, x + 17 - width, y + 9, this.textColor.getValue(), ClickGui.CONTEXT.getColorScheme().doesTextShadow());
        if (stack.getItem().showDurabilityBar(stack)) {
            this.renderDuraBar(stack, x, y);
        }
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.popMatrix();
    }

    void renderDuraBar(ItemStack stack, int x, int y) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double health = stack.getItem().getDurabilityForDisplay(stack);
        int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
        int i = Math.round(13.0f - (float)health * 13.0f);
        int j = rgbfordisplay;
        this.draw(bufferbuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
        this.draw(bufferbuilder, x + 2, y + 13, i, 1, j >> 16 & 0xFF, j >> 8 & 0xFF, j & 0xFF, 255);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double)(x + 0), (double)(y + 0), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + 0), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + 0), 0.0).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    String getNametagString(EntityPlayer player) {
        if (player == null) {
            return "";
        }
        String pingS = mc.getConnection() != null ? mc.getConnection().getPlayerInfo(Nametags.mc.player.getUniqueID()).getResponseTime() + "ms" : "-1ms";
        String healthS = (int)(player.getHealth() + player.getAbsorptionAmount()) + "";
        String str = (FriendManager.INSTANCE.isFriend((Entity)player) ? ChatFormatting.AQUA : "") + player.getName() + ChatFormatting.RESET + (FriendManager.INSTANCE.isFriend((Entity)player) ? ChatFormatting.AQUA : "");
        str = str + " ";
        str = str + (this.ping.getValue() != false ? pingS : "");
        str = str + " ";
        str = str + (this.health.getValue() != false ? healthS : "");
        return str;
    }

    public static Vec3d interpolateEntityByTicks(Entity entity, float renderPartialTicks) {
        return new Vec3d(Nametags.calculateDistanceWithPartialTicks(entity.posX, entity.lastTickPosX, renderPartialTicks) - Nametags.mc.getRenderManager().viewerPosX, Nametags.calculateDistanceWithPartialTicks(entity.posY, entity.lastTickPosY, renderPartialTicks) - Nametags.mc.getRenderManager().viewerPosY, Nametags.calculateDistanceWithPartialTicks(entity.posZ, entity.lastTickPosZ, renderPartialTicks) - Nametags.mc.getRenderManager().viewerPosZ);
    }

    public static Vec3d interpolateEntity(Entity entity, float renderPartialTicks) {
        return new Vec3d(Nametags.calculateDistanceWithPartialTicks(entity.posX, entity.lastTickPosX, renderPartialTicks), Nametags.calculateDistanceWithPartialTicks(entity.posY, entity.lastTickPosY, renderPartialTicks), Nametags.calculateDistanceWithPartialTicks(entity.posZ, entity.lastTickPosZ, renderPartialTicks));
    }

    public static double calculateDistanceWithPartialTicks(double originalPos, double finalPos, float renderPartialTicks) {
        return finalPos + (originalPos - finalPos) * (double)mc.getRenderPartialTicks();
    }

    public void prepareTranslation(double x, double y, double z, double distanceScale) {
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)y + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-Nametags.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)Nametags.mc.getRenderManager().playerViewX, (float)(Nametags.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-(distanceScale / 100.0)), (double)(-(distanceScale / 100.0)), (double)(distanceScale / 100.0));
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
    }

    public void release() {
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
    }
}

