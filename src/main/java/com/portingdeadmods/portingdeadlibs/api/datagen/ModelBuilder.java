// FIXME: Port and expand

//package com.portingdeadmods.portingdeadlibs.api.datagen;
//
//import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
//import net.minecraft.client.data.models.BlockModelGenerators;
//import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
//import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
//import net.minecraft.client.data.models.blockstates.PropertyDispatch;
//import net.minecraft.client.renderer.block.dispatch.Variant;
//import net.minecraft.core.Direction;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//
//import java.util.function.BiFunction;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//public class ModelBuilder {
//    private Block defaultTextureBlock = Blocks.IRON_BLOCK;
//    private final Block block;
//    private final BlockStateProvider modelProvider;
//
//    private boolean active;
//    private boolean horizontalFacing;
//    private boolean facing;
//    private boolean cutout;
//    private Identifier up;
//    private Identifier down;
//    private Identifier north;
//    private Identifier east;
//    private Identifier south;
//    private Identifier west;
//    private Identifier defaultTexture;
//    private Identifier particle;
//
//    public ModelBuilder(Block block, BlockStateProvider modelProvider) {
//        this.block = block;
//        this.modelProvider = modelProvider;
//        this.defaultTexture = this.modelProvider.blockTexture(defaultTextureBlock);
//    }
//
//    public ModelBuilder cutout() {
//        this.cutout = true;
//        return this;
//    }
//
//    public ModelBuilder active() {
//        this.active = true;
//        return this;
//    }
//
//    public ModelBuilder facing() {
//        this.facing = true;
//        return this;
//    }
//
//    public ModelBuilder horizontalFacing() {
//        this.horizontalFacing = true;
//        return this;
//    }
//
//    public ModelBuilder front(Identifier frontTexture) {
//        this.north = frontTexture;
//        return this;
//    }
//
//    public ModelBuilder front(Function<Block, Identifier> frontTexture) {
//        return front(frontTexture.apply(this.block));
//    }
//
//    public ModelBuilder front(BiFunction<Block, String, Identifier> frontTexture, String suffix) {
//        return front(frontTexture.apply(this.block, suffix));
//    }
//
//    public ModelBuilder back(Identifier backTexture) {
//        this.south = backTexture;
//        return this;
//    }
//
//    public ModelBuilder back(Function<Block, Identifier> backTexture) {
//        return back(backTexture.apply(this.block));
//    }
//
//    public ModelBuilder back(BiFunction<Block, String, Identifier> backTexture, String suffix) {
//        return back(backTexture.apply(this.block, suffix));
//    }
//
//    public ModelBuilder sides(Identifier sidesTexture) {
//        this.north = sidesTexture;
//        this.east = sidesTexture;
//        this.south = sidesTexture;
//        this.west = sidesTexture;
//        return this;
//    }
//
//    public ModelBuilder sides(Function<Block, Identifier> sidesTexture) {
//        return sides(sidesTexture.apply(this.block));
//    }
//
//    public ModelBuilder sides(BiFunction<Block, String, Identifier> sidesTexture, String suffix) {
//        return sides(sidesTexture.apply(this.block, suffix));
//    }
//
//    public ModelBuilder top(Identifier topTexture) {
//        this.up = topTexture;
//        return this;
//    }
//
//    public ModelBuilder top(Function<Block, Identifier> topTexture) {
//        return top(topTexture.apply(this.block));
//    }
//
//    public ModelBuilder top(BiFunction<Block, String, Identifier> topTexture, String suffix) {
//        return top(topTexture.apply(this.block, suffix));
//    }
//
//    public ModelBuilder bottom(Identifier bottomTexture) {
//        this.down = bottomTexture;
//        return this;
//    }
//
//    public ModelBuilder bottom(Function<Block, Identifier> bottomTexture) {
//        return bottom(bottomTexture.apply(this.block));
//    }
//
//    public ModelBuilder bottom(BiFunction<Block, String, Identifier> bottomTexture, String suffix) {
//        return bottom(bottomTexture.apply(this.block, suffix));
//    }
//
//    public ModelBuilder defaultTexture(Identifier defaultTexture) {
//        this.defaultTexture = defaultTexture;
//        return this;
//    }
//
//    public ModelBuilder defaultTexture(Function<Block, Identifier> defaultTexture) {
//        return defaultTexture(defaultTexture.apply(this.block));
//    }
//
//    public ModelBuilder defaultTexture(BiFunction<Block, String, Identifier> defaultTexture, String suffix) {
//        return defaultTexture(defaultTexture.apply(this.block, suffix));
//    }
//
//    public ModelBuilder particle(Identifier defaultTexture) {
//        this.particle = defaultTexture;
//        return this;
//    }
//
//    public ModelBuilder particle(Function<Block, Identifier> particleTexture) {
//        return particle(particleTexture.apply(this.block));
//    }
//
//    public ModelBuilder particle(BiFunction<Block, String, Identifier> particleTexture, String suffix) {
//        return particle(particleTexture.apply(this.block, suffix));
//    }
//
//    public void create() {
//        BlockModelGenerators activeBuilder = null;
//        Consumer<BlockModelDefinitionGenerator> blockStateOutput = activeBuilder.blockStateOutput;
//        MultiVariantGenerator.Empty generator = MultiVariantGenerator.dispatch(this.block);
//        generator.with(PropertyDispatch.initial());
//        new Variant(this.key(block).withSuffix("_active"), );
//        if (this.active) {
//            blockStateOutput.
//            activeBuilder = this.modelProvider.models().withExistingParent(this.name(block) + "_active", "cube");
//            activeBuilder.texture("down", activeTextureOrDefault(this.down));
//            activeBuilder.texture("up", activeTextureOrDefault(this.up));
//            activeBuilder.texture("north", activeTextureOrDefault(this.north));
//            activeBuilder.texture("east", activeTextureOrDefault(this.east));
//            activeBuilder.texture("south", activeTextureOrDefault(this.south));
//            activeBuilder.texture("west", activeTextureOrDefault(this.west));
//            activeBuilder.texture("particle", textureOrDefault(this.particle));
//            if (cutout) {
//                activeBuilder.renderType("cutout");
//            }
//        }
//        BlockModelBuilder inactiveBuilder = this.modelProvider.models().withExistingParent(this.name(block), "cube");
//        inactiveBuilder.texture("down", textureOrDefault(this.down));
//        inactiveBuilder.texture("up", textureOrDefault(this.up));
//        inactiveBuilder.texture("north", textureOrDefault(this.north));
//        inactiveBuilder.texture("east", textureOrDefault(this.east));
//        inactiveBuilder.texture("south", textureOrDefault(this.south));
//        inactiveBuilder.texture("west", textureOrDefault(this.west));
//        inactiveBuilder.texture("particle", textureOrDefault(this.particle));
//        if (cutout) {
//            inactiveBuilder.renderType("cutout");
//        }
//        createBlockState(activeBuilder, inactiveBuilder);
//    }
//
//    private Identifier activeTextureOrDefault(Identifier texture) {
//        return texture != null ? this.extend(texture, "_active") : defaultTexture;
//    }
//
//    private Identifier textureOrDefault(Identifier texture) {
//        return texture != null ? texture : defaultTexture;
//    }
//
//    private void createBlockState(BlockModelBuilder activeBuilder, BlockModelBuilder inactiveBuilder) {
//        VariantBlockStateBuilder builder = this.modelProvider.getVariantBuilder(block);
//        if (this.facing) {
//            for (Direction dir : BlockStateProperties.FACING.getPossibleValues()) {
//                if (this.active) {
//                    builder.partialState().with(PDLBlockStateProperties.ACTIVE, true).with(BlockStateProperties.FACING, dir)
//                            .modelForState()
//                            .modelFile(activeBuilder)
//                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
//                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
//                            .addModel()
//                            .partialState().with(PDLBlockStateProperties.ACTIVE, false).with(BlockStateProperties.FACING, dir)
//                            .modelForState()
//                            .modelFile(inactiveBuilder)
//                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
//                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
//                            .addModel();
//                } else {
//                    builder.partialState().with(BlockStateProperties.FACING, dir)
//                            .modelForState()
//                            .modelFile(inactiveBuilder)
//                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
//                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
//                            .addModel();
//                }
//            }
//        } else if (this.horizontalFacing) {
//            for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
//                if (this.active) {
//                    builder.partialState().with(PDLBlockStateProperties.ACTIVE, true).with(BlockStateProperties.HORIZONTAL_FACING, dir)
//                            .modelForState().modelFile(activeBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel()
//                            .partialState().with(PDLBlockStateProperties.ACTIVE, false).with(BlockStateProperties.HORIZONTAL_FACING, dir)
//                            .modelForState().modelFile(inactiveBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
//                } else {
//                    builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, dir)
//                            .modelForState().modelFile(inactiveBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
//                }
//            }
//        } else if (this.active) {
//            builder.partialState().with(PDLBlockStateProperties.ACTIVE, true)
//                    .modelForState().modelFile(activeBuilder).addModel()
//                    .partialState().with(PDLBlockStateProperties.ACTIVE, false)
//                    .modelForState().modelFile(inactiveBuilder).addModel();
//        } else {
//            builder.partialState().modelForState().modelFile(inactiveBuilder).addModel();
//        }
//    }
//
//    public void setDefaultTextureBlock(Block defaultTextureBlock) {
//        this.defaultTextureBlock = defaultTextureBlock;
//    }
//
//    private Identifier key(Block block) {
//        return BuiltInRegistries.BLOCK.getKey(block);
//    }
//
//    private String name(Block block) {
//        return this.key(block).getPath();
//    }
//
//    private Identifier extend(Identifier rl, String suffix) {
//        String var10000 = rl.getNamespace();
//        String var10001 = rl.getPath();
//        return Identifier.fromNamespaceAndPath(var10000, var10001 + suffix);
//    }
//}