package appeng.core.api.bootstrap;

import appeng.api.bootstrap.IDefinitionBuilder;
import appeng.api.definitions.IEntityDefinition;
import net.minecraftforge.fml.common.registry.EntityEntry;

public interface IEntityBuilder<E extends EntityEntry, EE extends IEntityBuilder<E, EE>> extends IDefinitionBuilder<E, IEntityDefinition<E>, EE> {

}
