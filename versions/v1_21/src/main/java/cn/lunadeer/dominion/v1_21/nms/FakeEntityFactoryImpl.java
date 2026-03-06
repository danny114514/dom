package cn.lunadeer.dominion.v1_21.nms;

import cn.lunadeer.dominion.nms.FakeEntity;
import cn.lunadeer.dominion.nms.FakeEntityFactory;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fake entity factory implementation for Minecraft 1.21.
 */
public class FakeEntityFactoryImpl implements FakeEntityFactory {

    private static final AtomicInteger ENTITY_COUNTER = new AtomicInteger(0);
    private static final boolean USE_FALLBACK;

    static {
        boolean fallback;
        try {
            Field counterField = Entity.class.getDeclaredField("ENTITY_COUNTER");
            counterField.setAccessible(true);
            AtomicInteger nmsCounter = (AtomicInteger) counterField.get(null);
            try {
                Field counterField2 = FakeEntityFactoryImpl.class.getDeclaredField("ENTITY_COUNTER");
                counterField2.setAccessible(true);
                counterField2.set(null, nmsCounter);
                fallback = false;
            } catch (Exception e) {
                fallback = true;
            }
        } catch (ReflectiveOperationException e) {
            fallback = true;
        }
        USE_FALLBACK = fallback;
        if (USE_FALLBACK) {
            System.out.println("[Dominion] Using fallback entity counter (Folia detected)");
        }
    }

    @Override
    public FakeEntity createBlockDisplay(Location location, BlockData blockData) {
        int entityId = nextEntityId();
        return new FakeEntityImpl(entityId, location, FakeEntity.DisplayType.BLOCK_DISPLAY, blockData, null);
    }

    @Override
    public FakeEntity createItemDisplay(Location location, ItemStack itemStack) {
        int entityId = nextEntityId();
        return new FakeEntityImpl(entityId, location, FakeEntity.DisplayType.ITEM_DISPLAY, null, itemStack);
    }

    @Override
    public int nextEntityId() {
        return ENTITY_COUNTER.incrementAndGet();
    }
}
