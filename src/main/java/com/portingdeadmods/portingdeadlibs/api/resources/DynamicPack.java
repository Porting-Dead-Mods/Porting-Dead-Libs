package com.portingdeadmods.portingdeadlibs.api.resources;

import com.google.gson.JsonElement;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibs;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*
 * Most of this code is from the Create mod <3
 *
 * Thank you to the contributors of Create
 */
public class DynamicPack implements PackResources {
    private final Map<String, IoSupplier<InputStream>> files = new HashMap<>();

    private final String packId;
    private final PackType packType;
    private final PackMetadataSection metadata;
    private final PackLocationInfo packLocationInfo;

    public DynamicPack(Identifier packId, PackType packType) {
        this(packId.toString(), packType, PackSource.BUILT_IN);
    }

    public DynamicPack(Identifier packId, PackType packType, PackSource source) {
        this(packId.toString(), packType, source);
    }

    private DynamicPack(String packId, PackType packType, PackSource source) {
        this.packId = packId;
        this.packType = packType;

        this.metadata = new PackMetadataSection(Component.empty(), SharedConstants.getCurrentVersion().packVersion(packType).minorRange());
        this.packLocationInfo = new PackLocationInfo(packId, Component.literal(packId), source, Optional.empty());
    }

    private static String getPath(PackType packType, Identifier resourceLocation) {
        return packType.getDirectory() + "/" + resourceLocation.getNamespace() + "/" + resourceLocation.getPath();
    }

    public DynamicPack put(Identifier location, IoSupplier<InputStream> stream) {
        files.put(getPath(packType, location), stream);
        return this;
    }

    public DynamicPack put(Identifier location, byte[] bytes) {
        return put(location, () -> new ByteArrayInputStream(bytes));
    }

    public DynamicPack put(Identifier location, String string) {
        return put(location, string.getBytes(StandardCharsets.UTF_8));
    }

    // Automatically suffixes the Identifier with .json
    public DynamicPack put(Identifier location, JsonElement json) {
        return put(location.withSuffix(".json"), PortingDeadLibs.GSON.toJson(json));
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String @NotNull ... elements) {
        return files.getOrDefault(String.join("/", elements), null);
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull Identifier resourceLocation) {
        return files.getOrDefault(getPath(packType, resourceLocation), null);
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String path, @NotNull ResourceOutput resourceOutput) {
        Identifier resourceLocation = Identifier.fromNamespaceAndPath(namespace, path);
        String directoryAndNamespace = packType.getDirectory() + "/" + namespace + "/";
        String prefix = directoryAndNamespace + path + "/";
        files.forEach((filePath, streamSupplier) -> {
            if (filePath.startsWith(prefix))
                resourceOutput.accept(resourceLocation.withPath(filePath.substring(directoryAndNamespace.length())), streamSupplier);
        });
    }

    @Override
    public @NotNull Set<String> getNamespaces(PackType packType) {
        Set<String> namespaces = new HashSet<>();
        String dir = packType.getDirectory() + "/";

        for (String path : files.keySet()) {
            if (path.startsWith(dir)) {
                String relative = path.substring(dir.length());
                if (relative.contains("/")) {
                    namespaces.add(relative.substring(0, relative.indexOf("/")));
                }
            }
        }

        return namespaces;
    }

    @Override
    public @org.jspecify.annotations.Nullable <T> T getMetadataSection(MetadataSectionType<T> metadataSectionType) throws IOException {
        return null;
    }

    @Override
    public @NotNull PackLocationInfo location() {
        return packLocationInfo;
    }

    @Override
    public @NotNull String packId() {
        return packId;
    }

    @Override
    public void close() {
    } // NO-OP

    public RepositorySource toSource(Pack.Position position) {
        return new DynamicPackSource(this.packId, this.packType, position, this);
    }

}
