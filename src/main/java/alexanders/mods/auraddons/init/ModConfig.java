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

        @Config.RangeInt(min = 0)
        public int autoWrathRitualTime = 800;
        @Config.RangeInt(min = 0)
        public int auraTransporterRitualTime = 500;
        @Config.RangeInt(min = 0)
        public int disruptionCatalystRitualTime = 600;
        @Config.RangeInt(min = 0)
        public int witherProoferRitualTime = 800;

        @Config.RangeInt(min = 0)
        public int witherSpawnerTime = 200;

        @Config.RangeDouble(min = 0)
        public double witherProoferRange = 20;
        
        @Config.Comment("Makes beacons with a RainbowBeacon on top of them update their beam color 4x as often")
        public boolean smoothRainbowBeacon = true;
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
        @Config.Comment("Also enables the animal spawner recipe for spawning withers")
        public boolean enableWitherProofer = true;
        public boolean enableRainbowBeacon = true;
    }

    public static class Aura {
        @Config.RangeInt(min = 0)
        public int autoWrathPulseCost = 1000;
        @Config.RangeInt(min = 0)
        public int autoWrathMobDamageCost = 1000;
        @Config.RangeInt(min = 0)
        public int freezerSnowCreationCost = 500;
        @Config.RangeInt(min = 0)
        public int freezerIceCreationCost = 1000;
        @Config.RangeInt(min = 0)
        public int freezerHardIceCreationCost = 8000;
        @Config.RangeInt(min = 0)
        public int dampeningFeatherAuraPerMeter = 4000;
        @Config.RangeInt(min = 0)
        public int potionEnhancerCostPerLevel = 5000;
        @Config.RangeInt(min = 0)
        public int witherProoferCost = 10000;
        @Config.RangeDouble(min = 1)
        public double auraTransporterRange = 6000;
        @Config.RangeDouble(min = 0)
        public int auraTransporterDrainRange = 1500;
        @Config.RangeDouble(min = 0)
        public int auraTransporterStoreRange = 1500;
        @Config.RangeInt(min = 1)
        public int auraTransporterAuraAmount = 100000;
        @Config.RangeDouble(min = 0, max = 1)
        public double auraTransporterAuraMultiplierPerBlock = 0.99;
        @Config.RangeInt(min = 0)
        public int witherSpawnerCost = 200000;
    }
}
