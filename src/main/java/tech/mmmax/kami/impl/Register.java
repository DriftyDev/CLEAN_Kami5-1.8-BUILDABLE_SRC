/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.impl;

import tech.mmmax.kami.api.management.BindManager;
import tech.mmmax.kami.api.management.CommandManager;
import tech.mmmax.kami.api.management.FeatureManager;
import tech.mmmax.kami.api.management.FriendManager;
import tech.mmmax.kami.api.management.SavableManager;
import tech.mmmax.kami.api.utils.color.RainbowUtil;
import tech.mmmax.kami.api.utils.player.RotationUtil;
import tech.mmmax.kami.impl.features.commands.Bind;
import tech.mmmax.kami.impl.features.commands.Friend;
import tech.mmmax.kami.impl.features.commands.Help;
import tech.mmmax.kami.impl.features.hud.ArmorHud;
import tech.mmmax.kami.impl.features.hud.Coords;
import tech.mmmax.kami.impl.features.hud.FeatureList;
import tech.mmmax.kami.impl.features.hud.Info;
import tech.mmmax.kami.impl.features.hud.Watermark;
import tech.mmmax.kami.impl.features.hud.Welcomer;
import tech.mmmax.kami.impl.features.modules.client.FontModule;
import tech.mmmax.kami.impl.features.modules.client.HudColors;
import tech.mmmax.kami.impl.features.modules.client.Manager;
import tech.mmmax.kami.impl.features.modules.client.Optimizer;
import tech.mmmax.kami.impl.features.modules.client.Safety;
import tech.mmmax.kami.impl.features.modules.client.gui.ClickGuiModule;
import tech.mmmax.kami.impl.features.modules.client.gui.PhobosGuiModule;
import tech.mmmax.kami.impl.features.modules.client.gui.SecondGuiModule;
import tech.mmmax.kami.impl.features.modules.combat.AutoTrap;
import tech.mmmax.kami.impl.features.modules.combat.BedAura;
import tech.mmmax.kami.impl.features.modules.combat.Bow32K;
import tech.mmmax.kami.impl.features.modules.combat.Burrow;
import tech.mmmax.kami.impl.features.modules.combat.ChorusAura;
import tech.mmmax.kami.impl.features.modules.combat.CrystalAura;
import tech.mmmax.kami.impl.features.modules.combat.Desync;
import tech.mmmax.kami.impl.features.modules.combat.GapDisease;
import tech.mmmax.kami.impl.features.modules.combat.HoleFill;
import tech.mmmax.kami.impl.features.modules.combat.Offhand;
import tech.mmmax.kami.impl.features.modules.combat.Platformer;
import tech.mmmax.kami.impl.features.modules.combat.Surround;
import tech.mmmax.kami.impl.features.modules.misc.ChatSuffix;
import tech.mmmax.kami.impl.features.modules.misc.ChunkCoordExploit;
import tech.mmmax.kami.impl.features.modules.misc.Disabler;
import tech.mmmax.kami.impl.features.modules.misc.Dupe5B;
import tech.mmmax.kami.impl.features.modules.misc.FakePlayer;
import tech.mmmax.kami.impl.features.modules.misc.HotbarRefill;
import tech.mmmax.kami.impl.features.modules.misc.MultiTask;
import tech.mmmax.kami.impl.features.modules.misc.NettyTest;
import tech.mmmax.kami.impl.features.modules.misc.NoMiningTrace;
import tech.mmmax.kami.impl.features.modules.misc.SpeedMine;
import tech.mmmax.kami.impl.features.modules.misc.TotemPopCounter;
import tech.mmmax.kami.impl.features.modules.player.ChorusManipulation;
import tech.mmmax.kami.impl.features.modules.player.ElytraFly;
import tech.mmmax.kami.impl.features.modules.player.Holesnap;
import tech.mmmax.kami.impl.features.modules.player.JumpSpeed;
import tech.mmmax.kami.impl.features.modules.player.MiddleClick;
import tech.mmmax.kami.impl.features.modules.player.NoInteract;
import tech.mmmax.kami.impl.features.modules.player.PacketFly;
import tech.mmmax.kami.impl.features.modules.player.ReverseStep;
import tech.mmmax.kami.impl.features.modules.player.Sprint;
import tech.mmmax.kami.impl.features.modules.player.Step;
import tech.mmmax.kami.impl.features.modules.player.Strafe;
import tech.mmmax.kami.impl.features.modules.player.TargetStrafe;
import tech.mmmax.kami.impl.features.modules.player.Velocity;
import tech.mmmax.kami.impl.features.modules.player.YPort;
import tech.mmmax.kami.impl.features.modules.render.BlockHighlight;
import tech.mmmax.kami.impl.features.modules.render.BurrowESP;
import tech.mmmax.kami.impl.features.modules.render.Chams;
import tech.mmmax.kami.impl.features.modules.render.ChorusViewer;
import tech.mmmax.kami.impl.features.modules.render.CrossHair;
import tech.mmmax.kami.impl.features.modules.render.HoleEsp;
import tech.mmmax.kami.impl.features.modules.render.Nametags;
import tech.mmmax.kami.impl.features.modules.render.NoRender;
import tech.mmmax.kami.impl.features.modules.render.PopESP;
import tech.mmmax.kami.impl.features.modules.render.RubberESP;
import tech.mmmax.kami.impl.features.modules.render.Trails;
import tech.mmmax.kami.impl.features.modules.render.ViewModel;
import tech.mmmax.kami.impl.gui.ClickGui;

public class Register {
    public static Register INSTANCE;

    public void registerAll() {
        this.registerManagers();
        this.registerFeatures();
        this.registerGui();
    }

    public void registerManagers() {
        SavableManager.INSTANCE = new SavableManager();
        BindManager.INSTANCE = new BindManager();
        FeatureManager.INSTANCE = new FeatureManager();
        RainbowUtil.INSTANCE = new RainbowUtil();
        FriendManager.INSTANCE = new FriendManager();
        RotationUtil.INSTANCE = new RotationUtil();
        CommandManager.INSTANCE = new CommandManager();
    }

    public void registerFeatures() {
        FeatureManager.INSTANCE.getFeatures().add(new Safety());
        FeatureManager.INSTANCE.getFeatures().add(new FontModule());
        FeatureManager.INSTANCE.getFeatures().add(new Manager());
        FeatureManager.INSTANCE.getFeatures().add(new HudColors());
        FeatureManager.INSTANCE.getFeatures().add(new Optimizer());
        FeatureManager.INSTANCE.getFeatures().add(new SecondGuiModule());
        FeatureManager.INSTANCE.getFeatures().add(new ClickGuiModule());
        FeatureManager.INSTANCE.getFeatures().add(new PhobosGuiModule());
        FeatureManager.INSTANCE.getFeatures().add(new Sprint());
        FeatureManager.INSTANCE.getFeatures().add(new Step());
        FeatureManager.INSTANCE.getFeatures().add(new ReverseStep());
        FeatureManager.INSTANCE.getFeatures().add(new JumpSpeed());
        FeatureManager.INSTANCE.getFeatures().add(new YPort());
        FeatureManager.INSTANCE.getFeatures().add(new PacketFly());
        FeatureManager.INSTANCE.getFeatures().add(new ChorusManipulation());
        FeatureManager.INSTANCE.getFeatures().add(new PacketFly());
        FeatureManager.INSTANCE.getFeatures().add(new Velocity());
        FeatureManager.INSTANCE.getFeatures().add(new NoInteract());
        FeatureManager.INSTANCE.getFeatures().add(new MiddleClick());
        FeatureManager.INSTANCE.getFeatures().add(new Strafe());
        FeatureManager.INSTANCE.getFeatures().add(new TargetStrafe());
        FeatureManager.INSTANCE.getFeatures().add(new Holesnap());
        FeatureManager.INSTANCE.getFeatures().add(new ElytraFly());
        FeatureManager.INSTANCE.getFeatures().add(new FakePlayer());
        FeatureManager.INSTANCE.getFeatures().add(new ChatSuffix());
        FeatureManager.INSTANCE.getFeatures().add(new SpeedMine());
        FeatureManager.INSTANCE.getFeatures().add(new Disabler());
        FeatureManager.INSTANCE.getFeatures().add(new Dupe5B());
        FeatureManager.INSTANCE.getFeatures().add(new MultiTask());
        FeatureManager.INSTANCE.getFeatures().add(new NettyTest());
        FeatureManager.INSTANCE.getFeatures().add(new ChunkCoordExploit());
        FeatureManager.INSTANCE.getFeatures().add(new HotbarRefill());
        FeatureManager.INSTANCE.getFeatures().add(new TotemPopCounter());
        FeatureManager.INSTANCE.getFeatures().add(new NoMiningTrace());
        FeatureManager.INSTANCE.getFeatures().add(new Chams());
        FeatureManager.INSTANCE.getFeatures().add(new HoleEsp());
        FeatureManager.INSTANCE.getFeatures().add(new Trails());
        FeatureManager.INSTANCE.getFeatures().add(new NoRender());
        FeatureManager.INSTANCE.getFeatures().add(new CrossHair());
        FeatureManager.INSTANCE.getFeatures().add(new ViewModel());
        FeatureManager.INSTANCE.getFeatures().add(new RubberESP());
        FeatureManager.INSTANCE.getFeatures().add(new PopESP());
        FeatureManager.INSTANCE.getFeatures().add(new Nametags());
        FeatureManager.INSTANCE.getFeatures().add(new ChorusViewer());
        FeatureManager.INSTANCE.getFeatures().add(new BurrowESP());
        FeatureManager.INSTANCE.getFeatures().add(new BlockHighlight());
        FeatureManager.INSTANCE.getFeatures().add(new CrystalAura());
        FeatureManager.INSTANCE.getFeatures().add(new Offhand());
        FeatureManager.INSTANCE.getFeatures().add(new HoleFill());
        FeatureManager.INSTANCE.getFeatures().add(new Burrow());
        FeatureManager.INSTANCE.getFeatures().add(new Surround());
        FeatureManager.INSTANCE.getFeatures().add(new AutoTrap());
        FeatureManager.INSTANCE.getFeatures().add(new Platformer());
        FeatureManager.INSTANCE.getFeatures().add(new ChorusAura());
        FeatureManager.INSTANCE.getFeatures().add(new BedAura());
        FeatureManager.INSTANCE.getFeatures().add(new GapDisease());
        FeatureManager.INSTANCE.getFeatures().add(new Desync());
        FeatureManager.INSTANCE.getFeatures().add(new Bow32K());
        FeatureManager.INSTANCE.getFeatures().add(new Watermark());
        FeatureManager.INSTANCE.getFeatures().add(new FeatureList());
        FeatureManager.INSTANCE.getFeatures().add(new Welcomer());
        FeatureManager.INSTANCE.getFeatures().add(new Info());
        FeatureManager.INSTANCE.getFeatures().add(new Coords());
        FeatureManager.INSTANCE.getFeatures().add(new ArmorHud());
        CommandManager.INSTANCE.getCommands().add(new Help());
        CommandManager.INSTANCE.getCommands().add(new Bind());
        CommandManager.INSTANCE.getCommands().add(new Friend());
    }

    public void registerGui() {
        ClickGui.INSTANCE = new ClickGui();
    }
}

