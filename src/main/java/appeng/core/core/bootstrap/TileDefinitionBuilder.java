package appeng.core.core.bootstrap;

import appeng.api.bootstrap.DefinitionFactory;
import appeng.api.definitions.IBlockDefinition;
import appeng.api.definitions.ITileDefinition;
import appeng.api.entry.TileRegistryEntry;
import appeng.core.api.bootstrap.IBlockBuilder;
import appeng.core.api.bootstrap.ITileBuilder;
import appeng.core.api.bootstrap.TileBlockCustomizer;
import appeng.core.core.block.TileBlockBase;
import appeng.core.lib.bootstrap.DefinitionBuilder;
import appeng.core.lib.definitions.TileDefinition;
import appeng.core.lib.entry.TileRegistryEntryImpl;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class TileDefinitionBuilder<T extends TileEntity> extends DefinitionBuilder<Class<T>, TileRegistryEntry<T>, ITileDefinition<T>, TileDefinitionBuilder<T>> implements ITileBuilder<T, TileDefinitionBuilder<T>> {

	private Function<ITileDefinition<T>, IBlockDefinition<Block>> block;

	public TileDefinitionBuilder(DefinitionFactory factory, ResourceLocation registryName, Class<T> tile){
		super(factory, registryName, tile, "tile_entity");
	}

	@Override
	public <B extends Block & ITileEntityProvider> TileDefinitionBuilder<T> setBlock(@Nonnull Function<ITileDefinition<T>, IBlockDefinition<B>> block){
		this.block = (Function) block;
		return null;
	}

	@Override
	public <B extends Block & ITileEntityProvider> TileDefinitionBuilder<T> createBlock(@Nonnull TileBlockCustomizer<T, B> customizer){
		return setBlock(def -> customizer.customize(factory.definitionBuilder(registryName, tileBlockIh(customizer.createBlock(def.maybe().get())))).setFeature(feature).build());
	}

	@Override
	public TileDefinitionBuilder<T> createDefaultBlock(Material material){
		return createBlock(tile -> new TileBlockBase<>(material, tile));
	}

	@Override
	public TileDefinitionBuilder<T> createDefaultBlockWithItem(Material material){
		return createBlock(new TileBlockCustomizer<T, TileBlockBase>() {

			@Nonnull
			@Override
			public TileBlockBase createBlock(TileRegistryEntry<T> tile){
				return new TileBlockBase(material, tile);
			}

			@Nonnull
			@Override
			public IBlockBuilder<TileBlockBase, ?> customize(@Nonnull IBlockBuilder<TileBlockBase, ?> builder){
				return builder.createDefaultItem();
			}

		});
	}

	@Override
	protected ITileDefinition<T> def(Class<T> t){
		if(t == null){
			return new TileDefinition<T>(registryName, null);
		}

		TileDefinition<T> definition = new TileDefinition<T>(registryName, new TileRegistryEntryImpl<T>(registryName, t));
		IBlockDefinition block = this.block.apply(definition);
		definition.setBlock(block);
		factory.addDefault(block);
		return definition;
	}

	@Override
	protected void register(TileRegistryEntry<T> t){
		GameRegistry.registerTileEntity(t.getTileClass(), registryName.toString());
	}

	public DefinitionFactory.InputHandler<Block, Block> tileBlockIh(Block block){
		return new DefinitionFactory.InputHandler<Block, Block>(block){};
	}

}
