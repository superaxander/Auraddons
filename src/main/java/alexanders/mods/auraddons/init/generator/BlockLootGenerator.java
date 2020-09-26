package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.init.ModBlocks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;

public class BlockLootGenerator implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator dataGenerator;
    private final Map<Block, Function<Block, LootTable.Builder>> lootFunctions = new HashMap<>();

    public BlockLootGenerator(DataGenerator dataGeneratorIn) {
        this.dataGenerator = dataGeneratorIn;

        for (Block block : ModBlocks.blockRegistry) {
            if (block instanceof SlabBlock) lootFunctions.put(block, LootTableHooks::generateSlab);
            else lootFunctions.put(block, LootTableHooks::generateDefault);
        }
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) throws IOException {
        for (Block block : lootFunctions.keySet()) {
            Function<Block, LootTable.Builder> function = lootFunctions.get(block);
            LootTable table = function.apply(block).setParameterSet(LootParameterSets.BLOCK).build();
            ResourceLocation name = block.getRegistryName();
            assert name != null;
            Path path = dataGenerator.getOutputFolder().resolve("data/" + name.getNamespace() + "/loot_tables/blocks/" + name.getPath() + ".json");
            IDataProvider.save(GSON, cache, LootTableManager.toJson(table), path);
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return "Loot Tables";
    }

    private static class LootTableHooks extends BlockLootTables {
        public static LootTable.Builder generateDefault(Block block) {
            return dropping(block);
        }

        public static LootTable.Builder generateSlab(Block block) {
            return droppingSlab(block);
        }
    }
}
