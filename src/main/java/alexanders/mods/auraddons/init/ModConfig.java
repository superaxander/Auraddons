package alexanders.mods.auraddons.init;

import net.minecraftforge.common.config.Config;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@Config(modid = MOD_ID, category = "")
public class ModConfig {
    public static General general = new General();
    public static Items items = new Items();
    public static Blocks blocks = new Blocks();
    public static Aura aura = new Aura();

    public static class General {
        @Config.Comment("Enables degrading nether blocks into soulsand when aura runs out")
        public boolean enableNetherDegradeEffect = true;

        public boolean allowFreezerHardIceCreation = true;
        public boolean allowFreezerSnowCreation = true;

        public int autoWrathRitualTime = 800;
    }

    public static class Items {
        public boolean enableCreativeAuraCache = true;
        public boolean enableDampeningFeather = true;
        public boolean enableSkyFeather = true;
    }

    public static class Blocks {
        public boolean enableAutoWrath = true;
        public boolean enableAncientFence = true;
        public boolean enableAncientFenceGate = true;
        public boolean enableInfusedStoneWall = true;
        public boolean enableInfusedBrickWall = true;
        public boolean enableAncientLadder = true;
        public boolean enableFreezer = true;
        public boolean enableHardIce = true;
    }

    public static class Aura {
        public int autoWrathPulseCost = 10;
        public int autoWrathMobDamageCost = 10;
        public int freezerSnowCreationCost = 5;
        public int freezerIceCreationCost = 10;
        public int freezerHardIceCreationCost = 80;
        public int dampeningFeatherAuraPerMeter = 40;
    }
}
