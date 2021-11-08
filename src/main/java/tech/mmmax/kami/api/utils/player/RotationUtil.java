/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package tech.mmmax.kami.api.utils.player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tech.mmmax.kami.api.event.PacketEvent;
import tech.mmmax.kami.api.utils.NullUtils;
import tech.mmmax.kami.api.wrapper.IMinecraft;
import tech.mmmax.kami.mixin.mixins.access.ICPacketPlayer;

public class RotationUtil
implements IMinecraft {
    public static RotationUtil INSTANCE;
    public boolean rotating = false;
    public float yaw;
    public float pitch;
    public float rotatedYaw;
    public float rotatedPitch;

    public RotationUtil() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getTime() == PacketEvent.Time.Send && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            ((ICPacketPlayer)((CPacketPlayer)event.getPacket())).setYaw(this.yaw);
            ((ICPacketPlayer)((CPacketPlayer)event.getPacket())).setPitch(this.pitch);
            this.rotatedYaw = this.yaw;
            this.rotatedPitch = this.pitch;
        }
    }

    public void rotate(Vec3d toRotate) {
        float[] rotations = RotationUtil.getNeededRotations(toRotate);
        this.yaw = rotations[0];
        this.pitch = rotations[1];
    }

    public void resetRotations() {
        this.yaw = RotationUtil.mc.player.rotationYaw;
        this.pitch = RotationUtil.mc.player.rotationPitch;
    }

    public static float[] getNeededRotations(Vec3d vec) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees((float)(yaw - RotationUtil.mc.player.rotationYaw)), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees((float)(pitch - RotationUtil.mc.player.rotationPitch))};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + (double)RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
    }
}

