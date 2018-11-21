package alexanders.mods.auraddons.init;

import net.minecraftforge.common.config.Config;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@Config(modid = MOD_ID, category = "")
public class ModConfig {
    public static General general = new General();
    public static Items items = new Items();
    public static Blocks blocks = new Blocks();

    public static class General {
        @Config.Comment("Enables degrading nether blocks into soulsand when mana runs out")
        public boolean enableNetherDegradeEffect = true;
    }

    public static class Items {
        public boolean enableCreativeAuraCache = true;
    }

    public static class Blocks {
        public boolean enableAutoWrath = true;
    }
}
