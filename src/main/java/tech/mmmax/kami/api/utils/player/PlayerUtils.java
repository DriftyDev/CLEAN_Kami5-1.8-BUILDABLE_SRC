/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.math.BlockPos
 */
package tech.mmmax.kami.api.utils.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import tech.mmmax.kami.api.wrapper.IMinecraft;

public class PlayerUtils
implements IMinecraft {
    public static void setSpeed(EntityLivingBase entity, double speed) {
        double[] dir = PlayerUtils.forward(speed);
        entity.motionX = dir[0];
        entity.motionZ = dir[1];
    }

    public static double getDefaultMoveSpeed() {
        double baseSpeed = 0.2873;
        if (PlayerUtils.mc.player != null && PlayerUtils.mc.player.isPotionActive(Potion.getPotionById((int)1))) {
            int amplifier = PlayerUtils.mc.player.getActivePotionEffect(Potion.getPotionById((int)1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }

    public static double[] forward(double speed) {
        float forward = PlayerUtils.mc.player.movementInput.moveForward;
        float side = PlayerUtils.mc.player.movementInput.moveStrafe;
        float yaw = PlayerUtils.mc.player.prevRotationYaw + (PlayerUtils.mc.player.rotationYaw - PlayerUtils.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static double[] radians() {
        float forward = PlayerUtils.mc.player.movementInput.moveForward;
        float side = PlayerUtils.mc.player.movementInput.moveStrafe;
        float yaw = PlayerUtils.mc.player.prevRotationYaw + (PlayerUtils.mc.player.rotationYaw - PlayerUtils.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * cos + (double)side * sin;
        double posZ = (double)forward * sin - (double)side * cos;
        return new double[]{posX, posZ};
    }

    public static BlockPos getPlayerPos() {
        double decimalPoint = PlayerUtils.mc.player.posY - Math.floor(PlayerUtils.mc.player.posY);
        return new BlockPos(PlayerUtils.mc.player.posX, decimalPoint > 0.8 ? Math.floor(PlayerUtils.mc.player.posY) + 1.0 : Math.floor(PlayerUtils.mc.player.posY), PlayerUtils.mc.player.posZ);
    }
}

