package com.portingdeadmods.portingdeadlibs.registries;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import com.portingdeadmods.portingdeadlibs.api.capabilities.fluidref.IFluidReferenceHandler;
import com.portingdeadmods.portingdeadlibs.api.capabilities.itemref.IItemReferenceHandler;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.CapabilityRegistrationHelper;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = PortingDeadLibs.MODID)
public class PDLCapabilities {
	public static final class ItemReference {
		public static final BlockCapability<IItemReferenceHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(create("item_reference"), IItemReferenceHandler.class);
		public static final EntityCapability<IItemReferenceHandler, @Nullable Direction> ENTITY = EntityCapability.createSided(create("item_reference"), IItemReferenceHandler.class);
		public static final ItemCapability<IItemReferenceHandler, @Nullable Void> ITEM = ItemCapability.createVoid(create("item_reference"), IItemReferenceHandler.class);

		private ItemReference() {}
	}

	public static final class FluidReference {
		public static final BlockCapability<IFluidReferenceHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(create("fluid_reference"), IFluidReferenceHandler.class);
		public static final EntityCapability<IFluidReferenceHandler, @Nullable Direction> ENTITY = EntityCapability.createSided(create("fluid_reference"), IFluidReferenceHandler.class);
		public static final ItemCapability<IFluidReferenceHandler, @Nullable Void> ITEM = ItemCapability.createVoid(create("fluid_reference"), IFluidReferenceHandler.class);

		private FluidReference() {}
	}

	private static ResourceLocation create(String path) {
		return PortingDeadLibs.rl(path);
	}

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        CapabilityRegistrationHelper.registerItemCaps(event, PDLItems.ITEMS);
        CapabilityRegistrationHelper.registerBECaps(event, PDLBlockEntityTypes.BLOCK_ENTITIES);
    }
}
