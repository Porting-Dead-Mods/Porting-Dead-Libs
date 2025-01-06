package com.portingdeadmods.portingdeadlibs.api.data;

import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class PDLDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PortingDeadLibs.MODID);
}
