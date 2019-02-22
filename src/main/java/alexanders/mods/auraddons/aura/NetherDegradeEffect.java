package alexanders.mods.auraddons.aura;

import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class NetherDegradeEffect implements IDrainSpotEffect {
    public static final ResourceLocation NAME = new ResourceLocation(MOD_ID, ModNames.NETHER_DEGRADE_EFFECT);

    @Override
    public void update(World world, Chunk chunk, IAuraChunk auraChunk, BlockPos pos, Integer spot) {
        if (spot < 0) {
            int aura = IAuraChunk.getAuraInArea(world, pos, 25);
            if (aura < 0) {
                int amount = Math.min(30000, Math.abs(aura) / 100000);
                if (amount > 1) {
                    int dist = MathHelper.clamp(Math.abs(aura) / 75000, 5, 45);
                    if (dist > 0) {
                        for (int i = amount / 2 + world.rand.nextInt(amount / 2); i >= 0; i--) {
                            BlockPos blockPos = new BlockPos(pos.getX() + world.rand.nextGaussian() * dist, pos.getY() + world.rand.nextGaussian() * dist,
                                                             pos.getZ() + world.rand.nextGaussian() * dist);
                            if (blockPos.distanceSq(pos) <= dist * dist && world.isBlockLoaded(blockPos)) {
                                IBlockState state = world.getBlockState(blockPos);
                                Block block = state.getBlock();

                                IBlockState newState = null;
                                if (block instanceof BlockNetherrack) {
                                    newState = Blocks.SOUL_SAND.getDefaultState();
                                } else if (block instanceof BlockNetherBrick) {
                                    newState = Blocks.NETHERRACK.getDefaultState();
                                } else if (block instanceof BlockOre) {
                                    newState = Blocks.NETHER_BRICK.getDefaultState();
                                } else if (block instanceof BlockGlowstone) {
                                    newState = Blocks.QUARTZ_ORE.getDefaultState();
                                }
                                if (newState != null) world.setBlockState(blockPos, newState);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean appliesHere(Chunk chunk, IAuraChunk auraChunk, IAuraType type) {
        return type == NaturesAuraAPI.TYPE_NETHER;
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }
}
