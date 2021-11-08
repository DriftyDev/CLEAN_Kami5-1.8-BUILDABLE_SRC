/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 */
package tech.mmmax.kami.api.utils.player;

import java.util.Comparator;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import tech.mmmax.kami.api.management.FriendManager;
import tech.mmmax.kami.api.utils.player.PlayerUtils;
import tech.mmmax.kami.api.utils.world.HoleUtils;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public class TargetUtils
implements IMinecraft {
    public static EntityLivingBase getTarget(double targetRange) {
        return (EntityLivingBase) TargetUtils.mc.world.getLoadedEntityList().stream().filter(Objects::nonNull).filter(entity -> entity instanceof EntityPlayer).filter(TargetUtils::isAlive).filter(entity -> entity.getEntityId() != TargetUtils.mc.player.getEntityId()).filter(entity -> !FriendManager.INSTANCE.isFriend((Entity)entity)).filter(entity -> (double)TargetUtils.mc.player.getDistance(entity) <= targetRange).min(Comparator.comparingDouble(entity -> TargetUtils.mc.player.getDistance(entity))).orElse(null);
    }

    public static HoleUtils.Hole getTargetHole(double targetRange) {
        return HoleUtils.getHoles(targetRange, PlayerUtils.getPlayerPos(), false).stream().filter(hole -> TargetUtils.mc.player.getDistanceSq(hole.pos1) <= targetRange).min(Comparator.comparingDouble(hole -> TargetUtils.mc.player.getDistanceSq(hole.pos1))).orElse(null);
    }

    public static HoleUtils.Hole getTargetHoleVec3D(double targetRange) {
        return HoleUtils.getHoles(targetRange, PlayerUtils.getPlayerPos(), false).stream().filter(hole -> TargetUtils.mc.player.getPositionVector().distanceTo(new Vec3d((double)hole.pos1.getX() + 0.5, TargetUtils.mc.player.posY, (double)hole.pos1.getZ() + 0.5)) <= targetRange).min(Comparator.comparingDouble(hole -> TargetUtils.mc.player.getPositionVector().distanceTo(new Vec3d((double)hole.pos1.getX() + 0.5, TargetUtils.mc.player.posY, (double)hole.pos1.getZ() + 0.5)))).orElse(null);
    }

    public static boolean isAlive(Entity entity) {
        return TargetUtils.isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }
}

