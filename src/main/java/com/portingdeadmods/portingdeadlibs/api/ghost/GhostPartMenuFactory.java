package com.portingdeadmods.portingdeadlibs.api.ghost;

import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface GhostPartMenuFactory {
    @Nullable
    PDLAbstractContainerMenu<?> create(
			GhostMultiblockControllerBE controller,
            BlockPos partPos,
            int containerId,
            Inventory inventory,
            Player player
    );
}
