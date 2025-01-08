/*

- BLOCKS REGISTERING TEMPLATE - 

- PLACEHOLDERS:
 - <MODCLASS>
 - <ITEMCLASS> - If you use the template for items, replace this with the class name of it

package

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class NTBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(<MODCLASS>.MODID);

	public static final DeferredBlock<Block> block = registerBlockAndItem("block", Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties) {
		return registerBlockAndItem(name, blockConstructor, properties, true, true);
	}

	// NOTE: This also attempts to generate the item model for the block, when running datagen
	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties, boolean addToTab, boolean genItemModel) {
		DeferredBlock<T> block = BLOCKS.registerBlock(name, blockConstructor, properties);
		DeferredItem<BlockItem> blockItem = <ITEMCLASS>.registerItem(name, props -> new BlockItem(block.get(), props), new Item.Properties(), addToTab);
		if (genItemModel) {
			<ITEMCLASS>.BLOCK_ITEMS.add(blockItem);
		}
		return block;
	}

	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties, BiFunction<T, Item.Properties, BlockItem> blockItemConstructor) {
		DeferredBlock<T> block = BLOCKS.registerBlock(name, blockConstructor, properties);
		DeferredItem<BlockItem> blockItem = <ITEMCLASS>.registerItem(name, props -> blockItemConstructor.apply(block.get(), props), new Item.Properties());
		<ITEMCLASS>.BLOCK_ITEMS.add(blockItem);
		return block;
	}
}
 */