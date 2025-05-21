package com.gregtechceu.gtceu.data.pack;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.addon.AddonFinder;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.data.pack.GTDynamicDataPack.writeJson;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GTDynamicResourcePack implements PackResources {

    private static final FileToIdConverter TEXTURE_ID_CONVERTER = new FileToIdConverter("textures", ".png");
    private static final FileToIdConverter BLOCK_STATE_ID_CONVERTER = FileToIdConverter.json("blockstates");
    private static final FileToIdConverter BLOCK_MODEL_ID_CONVERTER = FileToIdConverter.json("models/block");
    private static final FileToIdConverter ITEM_MODEL_ID_CONVERTER = FileToIdConverter.json("models/item");

    protected static final ObjectSet<String> CLIENT_DOMAINS = new ObjectOpenHashSet<>();
    protected static final GTDynamicPackContents CONTENTS = new GTDynamicPackContents();

    private final String name;

    static {
        CLIENT_DOMAINS.addAll(Sets.newHashSet(GTCEu.MOD_ID, "minecraft", "forge", "c"));
    }

    public GTDynamicResourcePack(String name) {
        this(name, AddonFinder.getAddons().stream().map(IGTAddon::addonModId).collect(Collectors.toSet()));
    }

    public GTDynamicResourcePack(String name, Collection<String> domains) {
        this.name = name;
        CLIENT_DOMAINS.addAll(domains);
    }

    public static void clearClient() {
        CONTENTS.clearData();
    }

    public static void addBlockModel(ResourceLocation loc, JsonElement obj) {
        byte[] modelBytes = obj.toString().getBytes(StandardCharsets.UTF_8);
        ResourceLocation l = getBlockModelLocation(loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = GTCEu.getGameDir().resolve("gtceu/dumped/assets");
            writeJson(l, null, parent, modelBytes);
        }
        CONTENTS.addToData(l, modelBytes);
    }

    public static void addBlockModel(ResourceLocation loc, Supplier<JsonElement> obj) {
        addBlockModel(loc, obj.get());
    }

    public static void addItemModel(ResourceLocation loc, JsonElement obj) {
        byte[] modelBytes = obj.toString().getBytes(StandardCharsets.UTF_8);
        ResourceLocation l = getItemModelLocation(loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = GTCEu.getGameDir().resolve("gtceu/dumped/assets");
            writeJson(l, null, parent, modelBytes);
        }
        CONTENTS.addToData(l, modelBytes);
    }

    public static void addItemModel(ResourceLocation loc, Supplier<JsonElement> obj) {
        addItemModel(loc, obj.get());
    }

    public static void addBlockState(ResourceLocation loc, JsonElement stateJson) {
        byte[] stateBytes = stateJson.toString().getBytes(StandardCharsets.UTF_8);
        ResourceLocation l = getBlockStateLocation(loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = GTCEu.getGameDir().resolve("gtceu/dumped/assets");
            writeJson(l, null, parent, stateBytes);
        }
        CONTENTS.addToData(l, stateBytes);
    }

    public static void addBlockState(ResourceLocation loc, Supplier<JsonElement> generator) {
        addBlockState(loc, generator.get());
    }

    public static void addBlockTexture(ResourceLocation loc, byte[] data) {
        ResourceLocation l = getTextureLocation("block", loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = GTCEu.getGameDir().resolve("gtceu/dumped/assets");
            writeByteArray(l, null, parent, data);
        }
        CONTENTS.addToData(l, data);
    }

    public static void addItemTexture(ResourceLocation loc, byte[] data) {
        ResourceLocation l = getTextureLocation("item", loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = GTCEu.getGameDir().resolve("gtceu/dumped/assets");
            writeByteArray(l, null, parent, data);
        }
        CONTENTS.addToData(l, data);
    }

    @ApiStatus.Internal
    public static void writeByteArray(ResourceLocation id, @Nullable String subdir, Path parent, byte[] data) {
        try {
            Path file;
            if (subdir != null) {
                file = parent.resolve(id.getNamespace()).resolve(subdir).resolve(id.getPath() + ".png"); // assume PNG
            } else {
                file = parent.resolve(id.getNamespace()).resolve(id.getPath()); // assume the file type is also appended
                                                                                // if a full path is given.
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(data);
            }
        } catch (IOException e) {
            GTCEu.LOGGER.error("Failed to save texture for id {} to disk.", id, e);
        }
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type == PackType.CLIENT_RESOURCES) {
            return CONTENTS.getResource(location);
        }
        return null;
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if (packType == PackType.CLIENT_RESOURCES) {
            CONTENTS.listResources(namespace, path, resourceOutput);
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return type == PackType.CLIENT_RESOURCES ? CLIENT_DOMAINS : Set.of();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T getMetadataSection(MetadataSectionSerializer<T> metaReader) {
        if (metaReader == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(Component.literal("GTCEu dynamic assets"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
        }
        return null;
    }

    @Override
    public String packId() {
        return this.name;
    }

    @Override
    public void close() {
        // NOOP
    }

    public static ResourceLocation getBlockStateLocation(ResourceLocation blockId) {
        return BLOCK_STATE_ID_CONVERTER.idToFile(blockId);
    }

    public static ResourceLocation getBlockModelLocation(ResourceLocation blockId) {
        return BLOCK_MODEL_ID_CONVERTER.idToFile(blockId);
    }

    public static ResourceLocation getItemModelLocation(ResourceLocation itemId) {
        return ITEM_MODEL_ID_CONVERTER.idToFile(itemId);
    }

    public static ResourceLocation getTextureLocation(@Nullable String path, ResourceLocation textureId) {
        if (path != null) {
            textureId = textureId.withPrefix(path + "/");
        }
        return TEXTURE_ID_CONVERTER.idToFile(textureId);
    }
}
