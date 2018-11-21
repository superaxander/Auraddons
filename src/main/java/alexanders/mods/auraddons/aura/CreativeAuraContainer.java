package alexanders.mods.auraddons.aura;

import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.api.aura.container.IAuraContainer;

public class CreativeAuraContainer implements IAuraContainer {
    @Override
    public int storeAura(int amountToStore, boolean simulate) {
        return 0;
    }

    @Override
    public int drainAura(int amountToDrain, boolean simulate) {
        return amountToDrain;
    }

    @Override
    public int getStoredAura() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxAura() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getAuraColor() {
        return 0xFFEE82EE;
    }

    @Override
    public boolean isAcceptableType(IAuraType type) {
        return true;
    }
}
