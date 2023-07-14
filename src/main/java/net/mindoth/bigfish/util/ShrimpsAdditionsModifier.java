package net.mindoth.bigfish.util;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mindoth.bigfish.config.BigFishCommonConfig;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ShrimpsAdditionsModifier extends LootModifier {
    public static final Supplier<Codec<ShrimpsAdditionsModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
            .fieldOf("item").forGetter(m -> m.item)).apply(inst, ShrimpsAdditionsModifier::new)));
    private final Item item;

    protected ShrimpsAdditionsModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if ( generatedLoot.get(0).is(ItemTags.FISHES) ) {
            double r = context.getRandom().nextDouble();
            if ( r <= BigFishCommonConfig.SHRIMPS_CHANCE.get() && BigFishCommonConfig.SHRIMPS_CHANCE.get() > 0 ) {
                generatedLoot.clear();
                generatedLoot.add(new ItemStack(item, 2));
            }
        }
        return generatedLoot;

    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}