package com.portingdeadmods.portingdeadlibs.api.client.screens.widgets;

import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;

import java.util.function.Consumer;

public record MenuWidgetContext(PDLAbstractContainerMenu<?> menu, Consumer<PanelWidget> onWidgetResizeFunc) {
}