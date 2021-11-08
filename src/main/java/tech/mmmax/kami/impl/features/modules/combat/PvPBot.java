/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package tech.mmmax.kami.impl.features.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tech.mmmax.kami.api.feature.Feature;
import tech.mmmax.kami.api.feature.module.Module;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.utils.player.TargetUtils;
import tech.mmmax.kami.api.utils.world.HoleUtils;
import tech.mmmax.kami.api.value.Value;
import tech.mmmax.kami.api.value.builder.ValueBuilder;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public class PvPBot
extends Module {
    Value<Number> targetRange = new ValueBuilder().withDescriptor("Target Range").withValue(15).withRange(5, 50).register(this);
    Value<Boolean> doubles = new ValueBuilder().withDescriptor("Doubles").withValue(true).register(this);
    Entity target;
    List<HoleUtils.Hole> holes;
    boolean initialized;
    ExecutorService service = Executors.newCachedThreadPool();

    public PvPBot() {
        super("PvP Bot", Feature.Category.Combat);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (!this.initialized) {
            this.service.submit(new HoleCallable());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        this.target = TargetUtils.getTarget(this.targetRange.getValue().doubleValue());
        if (this.holes == null) {
            return;
        }
        HoleUtils.Hole targetHole = this.holes.stream().min(Comparator.comparingDouble(this::rateHole)).orElse(null);
        if (targetHole == null) {
            return;
        }
    }

    double rateHole(HoleUtils.Hole hole) {
        return HoleType.getFromHole(hole).getVal() * this.comparePos(hole.pos1);
    }

    double comparePos(BlockPos pos) {
        return PvPBot.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) + this.target.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    public class HoleCallable
    implements Callable<Object> {
        @Override
        public Object call() throws Exception {
            PvPBot.this.initialized = true;
            this.doThreading();
            PvPBot.this.initialized = false;
            return null;
        }

        public void doThreading() {
            while (true) {
                if (NullUtils.nullCheck()) {
                    continue;
                }
                PvPBot.this.holes = HoleUtils.getHoles(PvPBot.this.targetRange.getValue().doubleValue(), IMinecraft.mc.player.getPosition(), PvPBot.this.doubles.getValue());
                System.out.println("aaaa");
            }
        }
    }

    public static enum HoleType {
        BedrockSafe(2.0),
        ObsidianUnsafe(3.0),
        DoubleUnsafe(4.0);

        double val;

        private HoleType(double val) {
            this.val = val;
        }

        public double getVal() {
            return this.val;
        }

        public static HoleType getFromHole(HoleUtils.Hole hole) {
            if (hole.bedrock) {
                return BedrockSafe;
            }
            if (hole.doubleHole) {
                return DoubleUnsafe;
            }
            return ObsidianUnsafe;
        }
    }
}

