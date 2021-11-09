/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.event.CrystalBlockRenderEvent;
import tech.mmmax.kami.api.event.RenderEntityEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.gui.component.IComponent;
import tech.mmmax.kami.api.gui.context.Context;
import tech.mmmax.kami.api.gui.helpers.MouseHelper;
import tech.mmmax.kami.api.gui.helpers.Rect;
import tech.mmmax.kami.api.gui.render.IRenderer;
import tech.mmmax.kami.api.gui.widget.IWidget;
import tech.mmmax.kami.api.management.FriendManager;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.api.wrapper.IMinecraft;
import tech.mmmax.kami.impl.gui.components.value.ICustomComponent;

public class Chams
extends Module {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public static Chams INSTANCE;
    Value<String> page = new ValueBuilder().withDescriptor("Page").withValue("Players").withModes("Players", "Crystals").register(this);
    Value<Boolean> players = new ValueBuilder().withDescriptor("Living").withValue(false).withPageParent(this.page).withPage("Players").register(this);
    Value<Entity> playerVisualizer = new ValueBuilder().withDescriptor("Crystal Visualizer").withValue(null).withComponent(new VisualizerWidget(null)).withPageParent(this.page).withPage("Players").register(this);
    Value<Boolean> playerCancel = new ValueBuilder().withDescriptor("Cancel", "playerCancel").withValue(false).withPageParent(this.page).withPage("Players").register(this);
    Value<Boolean> Self = new ValueBuilder().withDescriptor("Self", "playerSelf").withValue(false).withPageParent(this.page).withPage("Players").register(this);
    Value<Boolean> playerTexture = new ValueBuilder().withDescriptor("Texture", "playerTexture").withValue(false).withPageParent(this.page).withPage("Players").register(this);
    Value<Boolean> playerSecondaryTexture = new ValueBuilder().withDescriptor("Secondary Texture", "playerSecondaryTexture").withValue(false).withPageParent(this.page).withPage("Players").register(this);
    Value<Color> playerSecondaryTextureColor = new ValueBuilder().withDescriptor("Secondary Texture Color", "playerSecondaryTextureColor").withValue(new Color(255, 255, 255)).withPageParent(this.page).withPage("Players").register(this);
    Value<Color> playerFill = new ValueBuilder().withDescriptor("Fill", "playerFill").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Players").register(this);
    Value<Color> playerLine = new ValueBuilder().withDescriptor("Line", "playerLine").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Players").register(this);
    Value<Color> friendFill = new ValueBuilder().withDescriptor("Friend Fill", "friendFill").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Players").register(this);
    Value<Color> friendLine = new ValueBuilder().withDescriptor("Friend Line", "friendLine").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Players").register(this);
    Value<Boolean> playerGlint = new ValueBuilder().withDescriptor("Glint", "playerGlint").withValue(true).withPageParent(this.page).withPage("Players").register(this);
    Value<Boolean> playerImage = new ValueBuilder().withDescriptor("Player Image").withValue(false).withPageParent(this.page).withPage("Players").register(this);
    Value<Color> playerGlintColor = new ValueBuilder().withDescriptor("Glint Color", "playerGlintColor").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Players").register(this);
    Value<Color> friendGlintColor = new ValueBuilder().withDescriptor("Friend Glint Color").withValue(new Color(255, 255, 255, 255)).register(this);
    public Value<Boolean> noArmor = new ValueBuilder().withDescriptor("No Armor").withValue(false).withPageParent(this.page).withPage("Players").register(this);
    Value<Boolean> crystals = new ValueBuilder().withDescriptor("Crystals").withValue(true).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Entity> crystalVisualizer = new ValueBuilder().withDescriptor("Crystal Visualizer").withValue(null).withComponent(new VisualizerWidget((Entity)new EntityEnderCrystal(null))).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Boolean> crystalCancel = new ValueBuilder().withDescriptor("Cancel", "crystalCancel").withValue(false).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Boolean> crystalTexture = new ValueBuilder().withDescriptor("Texture", "crystalTexture").withValue(false).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Boolean> crystalSecondaryTexture = new ValueBuilder().withDescriptor("Secondary Texture", "crystalSecondaryTexture").withValue(false).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalSecondaryTextureColor = new ValueBuilder().withDescriptor("Secondary Texture Color", "crystalSecondaryTextureColor").withValue(new Color(255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Number> crystalRotateSpeed = new ValueBuilder().withDescriptor("Rotate Speed").withValue(1).withRange(0, 10).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Number> crystalScale = new ValueBuilder().withDescriptor("Crystal Scale").withValue(1).withRange(0, 3).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Boolean> crystalGlint = new ValueBuilder().withDescriptor("Glint", "crystalGlint").withValue(true).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalFill1 = new ValueBuilder().withDescriptor("Fill 1", "crystalFill1").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalLine1 = new ValueBuilder().withDescriptor("Line 1", "crystalLine1").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalGlint1 = new ValueBuilder().withDescriptor("Glint 1", "crystalGlint1").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalFill2 = new ValueBuilder().withDescriptor("Fill 2", "crystalFill2").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalLine2 = new ValueBuilder().withDescriptor("Line 2", "crystalLine2").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalGlint2 = new ValueBuilder().withDescriptor("Glint 2", "crystalGlint2").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalFill3 = new ValueBuilder().withDescriptor("Fill 3", "crystalFill3").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalLine3 = new ValueBuilder().withDescriptor("Line 3", "crystalLine3").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Color> crystalGlint3 = new ValueBuilder().withDescriptor("Glint 3", "crystalGlint3").withValue(new Color(255, 255, 255, 255)).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Boolean> crystalShader = new ValueBuilder().withDescriptor("Crystal Shader").withValue(false).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Number> lineWidth = new ValueBuilder().withDescriptor("Line Width").withValue(Float.valueOf(2.0f)).withRange(0.1f, 5f).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Number> lineWidthInterp = new ValueBuilder().withDescriptor("Line Width Interp").withValue(Float.valueOf(5.0f)).withRange(0.1f, 15f).withPageParent(this.page).withPage("Crystals").register(this);
    Value<Boolean> customBlendFunc = new ValueBuilder().withDescriptor("Blend Func").withValue(true).withPageParent(this.page).withPage("Crystals").register(this);
    boolean cancel = false;
    Action currentAction = Action.NONE;

    public Chams() {
        super("Chams", Feature.Category.Render);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void renderEntity(RenderEntityEvent event) {
        boolean texture;
        Color line = null;
        Color secondaryTextureColor;
        if (Chams.mc.player == null || Chams.mc.world == null || event.entityIn == null) {
            return;
        }
        ((VisualizerWidget)this.playerVisualizer.getComponent()).entity = Chams.mc.player;
        if (event.entityIn == Chams.mc.player && !this.Self.getValue().booleanValue()) {
            return;
        }
        if (event.entityIn instanceof EntityEnderCrystal && !this.crystals.getValue().booleanValue()) {
            return;
        }
        if (event.entityIn instanceof EntityLivingBase && !this.players.getValue().booleanValue()) {
            return;
        }
        RenderUtil.prepare();
        GL11.glPushAttrib((int)1048575);
        if (this.customBlendFunc.getValue().booleanValue()) {
            GL11.glBlendFunc((int)770, (int)32772);
        }
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2848);
        boolean image = event.entityIn instanceof EntityEnderCrystal ? false : this.playerImage.getValue();
        boolean cancelRender = event.entityIn instanceof EntityLivingBase ? this.playerCancel.getValue().booleanValue() : this.crystalCancel.getValue().booleanValue();
        boolean texture2d = event.entityIn instanceof EntityLivingBase ? this.playerTexture.getValue().booleanValue() : this.crystalTexture.getValue().booleanValue();
        boolean secondaryTexture = event.entityIn instanceof EntityLivingBase ? this.playerSecondaryTexture.getValue().booleanValue() : this.crystalSecondaryTexture.getValue().booleanValue();
        Color color = secondaryTextureColor = event.entityIn instanceof EntityLivingBase ? this.playerSecondaryTextureColor.getValue() : this.crystalSecondaryTextureColor.getValue();
        Color color2 = event.entityIn instanceof EntityLivingBase ? (FriendManager.INSTANCE.isFriend(event.entityIn) ? this.friendLine.getValue() : this.playerLine.getValue()) : (line = this.crystalLine1.getValue());
        Color fill = event.entityIn instanceof EntityLivingBase ? (FriendManager.INSTANCE.isFriend(event.entityIn) ? this.friendFill.getValue() : this.playerFill.getValue()) : this.crystalFill1.getValue();
        boolean bl = texture = event.entityIn instanceof EntityLivingBase ? this.playerGlint.getValue().booleanValue() : this.crystalGlint.getValue().booleanValue();
        Color textureColor = event.entityIn instanceof EntityLivingBase ? (FriendManager.INSTANCE.isFriend(event.entityIn) ? this.friendGlintColor.getValue() : this.playerGlintColor.getValue()) : this.crystalGlint1.getValue();
        float limbSwingAmt = event.entityIn instanceof EntityEnderCrystal ? event.limbSwingAmount * this.crystalRotateSpeed.getValue().floatValue() : event.limbSwingAmount;
        float scale = event.entityIn instanceof EntityEnderCrystal ? this.crystalScale.getValue().floatValue() : event.scale;
        GlStateManager.glLineWidth((float)RenderUtil.getInterpolatedLinWid(Chams.mc.player.getDistance(event.entityIn) + 1.0f, this.lineWidth.getValue().floatValue(), this.lineWidthInterp.getValue().floatValue()));
        if (!image) {
            GlStateManager.disableAlpha();
            ColorUtil.glColor(fill);
            if (texture2d) {
                GL11.glEnable((int)3553);
            } else {
                GL11.glDisable((int)3553);
            }
            this.currentAction = Action.FILL;
            event.modelBase.render(event.entityIn, event.limbSwing, limbSwingAmt, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scale);
            GL11.glDisable((int)3553);
            if (secondaryTexture) {
                this.currentAction = Action.NONE;
                ColorUtil.glColor(secondaryTextureColor);
                event.modelBase.render(event.entityIn, event.limbSwing, limbSwingAmt, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scale);
            }
            GL11.glPolygonMode((int)1032, (int)6913);
            this.currentAction = Action.LINE;
            ColorUtil.glColor(line);
            event.modelBase.render(event.entityIn, event.limbSwing, limbSwingAmt, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scale);
            this.currentAction = Action.GLINT;
            GL11.glPolygonMode((int)1032, (int)6914);
            if (texture) {
                mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
                GL11.glEnable((int)3553);
                GL11.glBlendFunc((int)768, (int)771);
                ColorUtil.glColor(textureColor);
                event.modelBase.render(event.entityIn, event.limbSwing, limbSwingAmt, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scale);
                if (this.customBlendFunc.getValue().booleanValue()) {
                    GL11.glBlendFunc((int)770, (int)32772);
                } else {
                    GL11.glBlendFunc((int)770, (int)771);
                }
            }
            if (event.entityIn instanceof EntityLivingBase) {
                // empty if block
            }
            event.limbSwingAmount = limbSwingAmt;
            this.currentAction = Action.NONE;
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popAttrib();
        RenderUtil.release();
        if (cancelRender) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderCrystal(CrystalBlockRenderEvent event) {
        Color glintColor;
        Color fillColor;
        Color lineColor;
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.currentAction == Action.NONE) {
            return;
        }
        switch (event.box) {
            case 1: {
                lineColor = this.crystalLine2.getValue();
                fillColor = this.crystalFill2.getValue();
                glintColor = this.crystalGlint2.getValue();
                break;
            }
            case 2: {
                lineColor = this.crystalLine3.getValue();
                fillColor = this.crystalFill3.getValue();
                glintColor = this.crystalGlint3.getValue();
                break;
            }
            default: {
                lineColor = this.crystalLine1.getValue();
                fillColor = this.crystalFill1.getValue();
                glintColor = this.crystalGlint1.getValue();
            }
        }
        ColorUtil.glColor(this.currentAction == Action.LINE ? lineColor : (this.currentAction == Action.GLINT ? glintColor : fillColor));
        event.renderer.render(event.scale);
        event.setCanceled(true);
    }

    static enum Action {
        FILL,
        LINE,
        GLINT,
        NONE;

    }

    public static class VisualizerWidget
    implements IComponent,
    IWidget<Entity>,
    ICustomComponent<Entity> {
        int height = 100;
        Entity entity;
        Rect dims;
        Value<Entity> entityValue;
        int rotation = 0;

        public VisualizerWidget(Entity entity) {
            this.entity = entity;
            if (this.entity instanceof EntityEnderCrystal) {
                EntityEnderCrystal crystal = (EntityEnderCrystal)this.entity;
                crystal.setShowBottom(false);
            }
            this.dims = new Rect(0, 0, 0, this.height);
        }

        @Override
        public void draw(Context context, MouseHelper mouse) {
            this.getDims().setHeight(this.height);
            context.getRenderer().renderRect(this.getDims(), context.getColorScheme().getTertiaryBackgroundColor(), context.getColorScheme().getTertiaryBackgroundColor(), IRenderer.RectMode.Fill, context);
            if (this.entity == null) {
                return;
            }
            double scale = (this.getDims().getWidth() - 10) / 2;
            double height = (double)this.getDims().getHeight() / (double)this.entity.height;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)((float)this.getDims().getX() + (float)(this.getDims().getWidth() / 2)), (float)((float)this.getDims().getY() + (float)this.getDims().getHeight()), (float)50.0f);
            GlStateManager.scale((float)((float)(-scale)), (float)((float)height), (float)((float)scale));
            GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.rotate((float)this.rotation++, (float)0.0f, (float)1.0f, (float)0.0f);
            RenderHelper.enableStandardItemLighting();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.0f);
            RenderManager rendermanager = IMinecraft.mc.getRenderManager();
            rendermanager.setPlayerViewY(180.0f);
            rendermanager.setRenderShadow(false);
            this.entity.setWorld((World)IMinecraft.mc.world);
            rendermanager.renderEntity(this.entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
            rendermanager.setRenderShadow(true);
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
        }

        @Override
        public void click(Context context, MouseHelper mouse, int button) {
        }

        @Override
        public void release(Context context, MouseHelper mouse, int state) {
        }

        @Override
        public void key(Context context, int key, char character) {
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public Rect getDims() {
            return this.dims;
        }

        @Override
        public boolean isDraggable() {
            return false;
        }

        @Override
        public boolean isActive() {
            return this.entityValue.isActive();
        }

        @Override
        public Entity getValue() {
            return this.entity;
        }

        @Override
        public void setValue(Entity value) {
        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public void setTitle(String title) {
        }

        @Override
        public Rect getDisplayDims() {
            return this.getDims();
        }

        @Override
        public void setValue(Value<Entity> value) {
            this.entityValue = value;
        }
    }
}

