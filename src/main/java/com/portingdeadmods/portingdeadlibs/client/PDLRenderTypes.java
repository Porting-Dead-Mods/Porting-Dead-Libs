package com.portingdeadmods.portingdeadlibs.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.util.OptionalDouble;

public class PDLRenderTypes {
	// FIXME: THIS PROBABLY DOESNT WORK YET
	public static final RenderType LINES_NONTRANSLUCENT = createDefault(
			PortingDeadLibs.MODID+":nontranslucent_lines",
			RenderSetup.builder(RenderPipelines.LINES)
					.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
					.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
	);

	private static RenderType createDefault(String name, RenderSetup.RenderSetupBuilder builder) {
		//return RenderType.create(name, format, mode, 256, false, false, state);
		return RenderType.create(name, builder.createRenderSetup());
	}
}
