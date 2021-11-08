/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.lwjgl.opengl.GL11
 */
package tech.mmmax.kami.impl.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.Timer;
import tech.mmmax.kami.api.utils.color.ColorUtil;
import tech.mmmax.kami.api.utils.render.RenderUtil;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;

public class Trails
extends Module {
    Value<Number> lineWidth = new ValueBuilder().withDescriptor("Line Width").withValue(2).withRange(0.1, 5).register(this);
    Value<Number> lifetime = new ValueBuilder().withDescriptor("Lifetime").withValue(1000).withRange(0, 5000).register(this);
    Value<Boolean> fade = new ValueBuilder().withDescriptor("Fade").withValue(true).register(this);
    Value<Boolean> xp = new ValueBuilder().withDescriptor("XP").withValue(false).register(this);
    Value<Boolean> arrow = new ValueBuilder().withDescriptor("Arrow").withValue(false).register(this);
    Value<Color> startColor = new ValueBuilder().withDescriptor("Start Color").withValue(new Color(255, 255, 255)).register(this);
    Value<Color> endColor = new ValueBuilder().withDescriptor("End Color").withValue(new Color(0, 255, 72)).register(this);
    Value<Boolean> self = new ValueBuilder().withDescriptor("Self").withValue(false).register(this);
    Value<Number> selfTime = new ValueBuilder().withDescriptor("Self Time").withValue(1000).withRange(0, 2000).register(this);
    Value<Boolean> target = new ValueBuilder().withDescriptor("Target").withValue(false).register(this);
    Value<Number> targetTime = new ValueBuilder().withDescriptor("Target Time").withValue(1000).withRange(0, 2000).register(this);
    Map<UUID, ItemTrail> trails = new HashMap<UUID, ItemTrail>();

    public Trails() {
        super("Trails", Feature.Category.Render);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        for (Entity entity : Trails.mc.world.loadedEntityList) {
            if (!this.allowEntity(entity)) continue;
            if (this.trails.containsKey(entity.getUniqueID())) {
                if (entity.isDead) {
                    if (this.trails.get((Object)entity.getUniqueID()).timer.isPaused()) {
                        this.trails.get((Object)entity.getUniqueID()).timer.resetDelay();
                    }
                    this.trails.get((Object)entity.getUniqueID()).timer.setPaused(false);
                    continue;
                }
                this.trails.get((Object)entity.getUniqueID()).positions.add(new Position(entity.getPositionVector()));
                continue;
            }
            this.trails.put(entity.getUniqueID(), new ItemTrail(entity));
        }
        if (this.self.getValue().booleanValue()) {
            if (this.trails.containsKey(Trails.mc.player.getUniqueID())) {
                ItemTrail playerTrail = this.trails.get(Trails.mc.player.getUniqueID());
                playerTrail.timer.resetDelay();
                ArrayList<Position> toRemove = new ArrayList<Position>();
                for (Position position : playerTrail.positions) {
                    if (System.currentTimeMillis() - position.time <= this.selfTime.getValue().longValue()) continue;
                    toRemove.add(position);
                }
                playerTrail.positions.removeAll(toRemove);
                playerTrail.positions.add(new Position(Trails.mc.player.getPositionVector()));
            } else {
                this.trails.put(Trails.mc.player.getUniqueID(), new ItemTrail((Entity)Trails.mc.player));
            }
        } else if (this.trails.containsKey(Trails.mc.player.getUniqueID())) {
            this.trails.remove(Trails.mc.player.getUniqueID());
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        for (Map.Entry<UUID, ItemTrail> entry : this.trails.entrySet()) {
            if (entry.getValue().entity.isDead || Trails.mc.world.getEntityByID(entry.getValue().entity.getEntityId()) == null) {
                if (entry.getValue().timer.isPaused()) {
                    entry.getValue().timer.resetDelay();
                }
                entry.getValue().timer.setPaused(false);
            }
            if (entry.getValue().timer.isPassed()) continue;
            this.drawTrail(entry.getValue());
        }
    }

    public void drawTrail(ItemTrail trail) {
        double fadeAmount = this.normalize(System.currentTimeMillis() - trail.timer.getStartTime(), 0.0, this.lifetime.getValue().doubleValue());
        int alpha = (int)(fadeAmount * 255.0);
        alpha = MathHelper.clamp((int)alpha, (int)0, (int)255);
        alpha = 255 - alpha;
        alpha = trail.timer.isPaused() ? 255 : alpha;
        Color fadeColor = ColorUtil.newAlpha(this.startColor.getValue(), alpha);
        RenderUtil.prepare();
        GL11.glLineWidth((float)this.lineWidth.getValue().floatValue());
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.builder = RenderUtil.tessellator.getBuffer();
        RenderUtil.builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        this.buildBuffer(RenderUtil.builder, trail, this.endColor.getValue(), this.fade.getValue() != false ? fadeColor : this.endColor.getValue());
        RenderUtil.tessellator.draw();
        RenderUtil.release();
    }

    public void buildBuffer(BufferBuilder builder, ItemTrail trail, Color start, Color end) {
        for (Position p : trail.positions) {
            Vec3d pos = RenderUtil.updateToCamera(p.pos);
            double value = this.normalize(trail.positions.indexOf(p), 0.0, trail.positions.size());
            RenderUtil.addBuilderVertex(builder, pos.x, pos.y, pos.z, ColorUtil.interpolate((float)value, start, end));
        }
    }

    boolean allowEntity(Entity e) {
        return e instanceof EntityEnderPearl || e instanceof EntityExpBottle && this.xp.getValue() != false || e instanceof EntityArrow && this.arrow.getValue() != false;
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static class Position {
        public Vec3d pos;
        public long time;

        public Position(Vec3d pos) {
            this.pos = pos;
            this.time = System.currentTimeMillis();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Position position = (Position)o;
            return this.time == position.time && Objects.equals(this.pos, position.pos);
        }

        public int hashCode() {
            return Objects.hash(this.pos, this.time);
        }
    }

    public class ItemTrail {
        public Entity entity;
        public List<Position> positions;
        public Timer timer;

        public ItemTrail(Entity entity) {
            this.entity = entity;
            this.positions = new ArrayList<Position>();
            this.timer = new Timer();
            this.timer.setDelay(Trails.this.lifetime.getValue().longValue());
            this.timer.setPaused(true);
        }
    }
}

