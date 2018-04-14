package stevekung.mods.stevekunglib.util.loot;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

@Deprecated //TODO Remove in 1.13
public class SmeltWithDataFunction extends LootFunction
{
    private RandomValueRange metaRange;

    public SmeltWithDataFunction(LootCondition[] conditions, RandomValueRange metaRange)
    {
        super(conditions);
        this.metaRange = metaRange;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, Random rand, LootContext context)
    {
        ItemStack dropStack = new ItemStack(itemStack.getItem(), itemStack.getCount(), this.metaRange.generateInt(rand));
        return dropStack;
    }

    public static class Serializer extends LootFunction.Serializer<SmeltWithDataFunction>
    {
        public Serializer()
        {
            super(new ResourceLocation("stevekunglib:furnace_smelt_with_data"), SmeltWithDataFunction.class);
        }

        @Override
        public void serialize(JsonObject object, SmeltWithDataFunction functionClazz, JsonSerializationContext serializationContext) {}

        @Override
        public SmeltWithDataFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditions)
        {
            return new SmeltWithDataFunction(conditions, JsonUtils.deserializeClass(object, "data", deserializationContext, RandomValueRange.class));
        }
    }
}