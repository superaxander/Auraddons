package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.init.generator.Config;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@Config(modid = MOD_ID)
public class ModConfig {
    public static General general = new General();
    public static Aura aura = new Aura();

    public static class General {
        @Config.Comment("Enables degrading nether blocks into soulsand when aura runs out")
        public boolean enableNetherDegradeEffect = true;

        @Config.RangeDouble(min = 0)
        public double witherProoferRange = 20;

        @Config.Comment("Makes beacons with a RainbowBeacon on top of them update their beam color 4x as often")
        public boolean smoothRainbowBeacon = true;
    }

    public static class Aura {
        @Config.RangeInt(min = 0)
        public int autoWrathPulseCost = 1000;
        @Config.RangeInt(min = 0)
        public int autoWrathMobDamageCost = 1000;
        @Config.RangeInt(min = 0)
        public int dampeningFeatherAuraPerMeter = 4000;
        @Config.RangeInt(min = 0)
        public int potionEnhancerCostPerLevel = 5000;
        @Config.RangeInt(min = 0)
        public int witherProoferCost = 10000;
        @Config.RangeDouble(min = 1)
        public double auraTransporterRange = 60;
        @Config.RangeDouble(min = 0)
        public int auraTransporterDrainRange = 15;
        @Config.RangeDouble(min = 0)
        public int auraTransporterStoreRange = 15;
        @Config.RangeInt(min = 1)
        public int auraTransporterAuraAmount = 100000;
        @Config.RangeDouble(min = 0, max = 1)
        public double auraTransporterAuraMultiplierPerBlock = 0.99;
    }
}
