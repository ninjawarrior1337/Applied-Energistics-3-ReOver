package appeng.miscellaneous;

import appeng.api.bootstrap.DefinitionFactory;
import appeng.api.bootstrap.InitializationComponentsHandler;
import appeng.api.config.ConfigurationLoader;
import appeng.api.definitions.IDefinition;
import appeng.api.definitions.IDefinitions;
import appeng.api.entry.TileRegistryEntry;
import appeng.api.module.AEStateEvent;
import appeng.api.module.Module;
import appeng.api.module.Module.ModuleEventHandler;
import appeng.core.AppEng;
import appeng.core.crafting.definitions.CraftingBlockDefinitions;
import appeng.core.crafting.definitions.CraftingItemDefinitions;
import appeng.core.crafting.definitions.CraftingTileDefinitions;
import appeng.core.lib.bootstrap.InitializationComponentsHandlerImpl;
import appeng.miscellaneous.api.IMiscellaneous;
import appeng.miscellaneous.config.MiscellaneousConfig;
import appeng.miscellaneous.proxy.MiscProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Module(IMiscellaneous.NAME)
public class AppEngMiscellaneous implements IMiscellaneous {

	public static final Logger logger = LogManager.getLogger(AppEng.NAME + "|"+ NAME);

	@Module.Instance
	public static final AppEngMiscellaneous INSTANCE = null;

	@SidedProxy(modId = AppEng.MODID, clientSide = "appeng.miscellaneous.proxy.MiscClientProxy", serverSide = "appeng.miscellaneous.proxy.MiscServerProxy")
	public static MiscProxy proxy;

	public MiscellaneousConfig config;

	private InitializationComponentsHandler initHandler = new InitializationComponentsHandlerImpl();

	private DefinitionFactory registry;

	private CraftingItemDefinitions itemDefinitions;
	private CraftingBlockDefinitions blockDefinitions;
	private CraftingTileDefinitions tileDefinitions;

	@Override
	public <T, D extends IDefinitions<T, ? extends IDefinition<T>>> D definitions(Class<T> clas){
		if(clas == Item.class){
			return (D) itemDefinitions;
		}
		if(clas == Block.class){
			return (D) blockDefinitions;
		}
		if(clas == TileRegistryEntry.class){
			return (D) tileDefinitions;
		}
		return null;
	}

	@ModuleEventHandler
	public void preInitAE(AEStateEvent.AEPreInitializationEvent event){
		ConfigurationLoader<MiscellaneousConfig> configLoader = event.configurationLoader();
		try{
			configLoader.load(MiscellaneousConfig.class);
		} catch(IOException e){
			logger.error("Caught exception loading configuration", e);
		}
		config = configLoader.configuration();

		registry = event.factory(initHandler, proxy);
		this.itemDefinitions = new CraftingItemDefinitions(registry);
		this.blockDefinitions = new CraftingBlockDefinitions(registry);
		this.tileDefinitions = new CraftingTileDefinitions(registry);

		this.itemDefinitions.init(registry);
		this.blockDefinitions.init(registry);
		this.tileDefinitions.init(registry);

		initHandler.preInit();
		proxy.preInit(event);

		try{
			configLoader.save();
		} catch(IOException e){
			logger.error("Caught exception saving configuration", e);
		}
	}

	@ModuleEventHandler
	public void initAE(final AEStateEvent.AEInitializationEvent event){
		initHandler.init();
		proxy.init(event);
	}

	@ModuleEventHandler
	public void postInitAE(final AEStateEvent.AEPostInitializationEvent event){
		initHandler.postInit();
		proxy.postInit(event);
	}

	@ModuleEventHandler
	public void handleIMCEventAE(AEStateEvent.ModuleIMCMessageEvent event){

	}

}
