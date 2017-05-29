package appeng.tools;

import appeng.api.definitions.IDefinition;
import appeng.api.definitions.IDefinitions;
import appeng.api.module.AEStateEvent;
import appeng.api.module.Module;
import appeng.api.module.Module.ModuleEventHandler;
import appeng.core.lib.module.AEStateEventImpl;
import appeng.core.AppEng;
import appeng.core.api.material.Material;
import appeng.core.lib.bootstrap.FeatureFactory;
import appeng.tools.api.ITools;
import appeng.tools.definitions.ToolsItemDefinitions;
import appeng.tools.definitions.ToolsMaterialDefinitions;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;

@Module(ITools.NAME)
@Mod(modid = AppEngTools.MODID, name = AppEngTools.MODNAME, version = AppEng.VERSION, dependencies = "required-after:" + AppEng.MODID, acceptedMinecraftVersions = ForgeVersion.mcVersion)
public class AppEngTools implements ITools {

	@Module.Instance(NAME)
	public static final AppEngTools INSTANCE = null;

	public static final String MODID = AppEng.MODID + "|" + ITools.NAME;

	public static final String MODNAME = AppEng.NAME + " | " + ITools.NAME;

	private FeatureFactory registry;

	private ToolsItemDefinitions itemDefinitions;
	private ToolsMaterialDefinitions materialDefinitions;

	@Override
	public <T, D extends IDefinitions<T, ? extends IDefinition<T>>> D definitions(Class<T> clas){
		if(clas == Item.class){
			return (D) itemDefinitions;
		}
		if(clas == Material.class){
			return (D) materialDefinitions;
		}
		return null;
	}

	@ModuleEventHandler
	public void preInitAE(AEStateEvent.AEPreInitlizationEvent event){
		registry = new FeatureFactory();
		this.materialDefinitions = new ToolsMaterialDefinitions(registry);
		this.itemDefinitions = new ToolsItemDefinitions(registry);
		registry.preInit(event);
	}

	@EventHandler
	public void preInitForge(FMLPreInitializationEvent event){

	}

	@ModuleEventHandler
	public void initAE(final AEStateEvent.AEInitializationEvent event){
		registry.init(event);
	}

	@EventHandler
	public void initForge(final FMLInitializationEvent event){

	}

	@ModuleEventHandler
	public void postInitAE(final AEStateEvent.AEPostInitializationEvent event){
		registry.postInit(event);
	}

	@EventHandler
	public void postInitForge(final FMLPostInitializationEvent event){

	}

	@ModuleEventHandler
	public void handleIMCEventAE(AEStateEvent.ModuleIMCMessageEvent event){

	}

	@EventHandler
	public void handleIMCEventForge(IMCEvent event){

	}

	/*@ModuleEventHandler
	public void serverAboutToStartAE(FMLServerAboutToStartEvent event){

	}*/

	@EventHandler
	public void serverAboutToStartForge(FMLServerAboutToStartEvent event){

	}

	/*@ModuleEventHandler
	public void serverStartingAE(FMLServerStartingEvent event){

	}*/

	@EventHandler
	public void serverStartingForge(FMLServerStartingEvent event){

	}

	/*@ModuleEventHandler
	public void serverStoppingAE(FMLServerStoppingEvent event){

	}*/

	@EventHandler
	public void serverStoppingForge(FMLServerStoppingEvent event){

	}

	/*@ModuleEventHandler
	public void serverStoppedAE(FMLServerStoppedEvent event){

	}*/

	@EventHandler
	public void serverStoppedForge(FMLServerStoppedEvent event){

	}

}
