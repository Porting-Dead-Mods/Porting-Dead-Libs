package com.portingdeadmods.portingdeadlibs.mixins;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibsClient;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeTab.ItemDisplayParameters.class)
public abstract class ItemDisplayParametersMixin {

    @Inject(method = "needsUpdate", at = @At("HEAD"), cancellable = true)
    private void injectNeedsUpdate(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeTab.ItemDisplayParameters self = (CreativeModeTab.ItemDisplayParameters) (Object) this;

        boolean shouldUpdate =
                !self.enabledFeatures().equals(enabledFeatures)
                        || self.hasPermissions() != hasPermissions
                        || self.holders() != holders
                        || PortingDeadLibsClient.areTabsDirty();

        cir.setReturnValue(shouldUpdate);
        cir.cancel();

        if (shouldUpdate) {
            PortingDeadLibsClient.markTabsClean();
        }
    }
}