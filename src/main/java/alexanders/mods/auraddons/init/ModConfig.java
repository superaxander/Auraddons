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
        public int auraTransporterRitualTime = 500;

        @Config.RangeDouble(min=1)
        public double auraTransporterRange = 30;
    }

    public static class Items {
        public boolean enableCreativeAuraCache = true;
        public boolean enableDampeningFeather = true;
        public boolean enableSkyFeather = true;
        public boolean enableSkyBottle = true;
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
        public boolean enablePotionEnhancer = true;
        public boolean enableDisruptionCatalyst = true;
        public boolean enableAuraTransporter = true;
    }

    public static class Aura {
        public int autoWrathPulseCost = 10;
        public int autoWrathMobDamageCost = 10;
        public int freezerSnowCreationCost = 5;
        public int freezerIceCreationCost = 10;
        public int freezerHardIceCreationCost = 80;
        public int dampeningFeatherAuraPerMeter = 40;
        public int potionEnhancerCostPerLevel = 50;
        public int auraTransporterDrainRange = 15;
        public int auraTransporterStoreRange = 15;
        
        @Config.RangeInt(min = 1)
        public int auraTransporterAuraAmount = 1000;
        
        @Config.RangeDouble(min = 0, max = 1)
        public double auraTransporterAuraMultiplierPerBlock = 0.97;
    }
}
