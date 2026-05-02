package ru.nern.notsoshadowextras;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

import java.util.Collections;
import java.util.Optional;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class NSSECommands {
    private static final SimpleCommandExceptionType IS_NOT_A_BLOCK = new SimpleCommandExceptionType(new LiteralMessage("Selected item is not a block"));
    private static final SimpleCommandExceptionType BLOCK_ENTITY_UNSUPPORTED = new SimpleCommandExceptionType(new LiteralMessage("Selected block can't have a block entity"));
    private static final SimpleCommandExceptionType UNKNOWN_TYPE = new SimpleCommandExceptionType(new LiteralMessage("Can't create a block entity of this type"));
    private static final SuggestionProvider<CommandSourceStack> BLOCK_ENTITIES = (context, builder) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.BLOCK_ENTITY_TYPE.keySet(), builder);

    protected static void init() {
        if (FabricLoader.getInstance().isModLoaded("fabric-command-api-v2") && NSSE.config().Additions.BlockEntitySwapCommand) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("nsse")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .then(literal("exists")
                            .then(argument("pos", BlockPosArgument.blockPos())
                                    .executes(ctx -> blockEntityExists(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos")))))
                    .then(literal("swap")
                            .then(argument("blockEntity", IdentifierArgument.id()).suggests(BLOCK_ENTITIES)
                                    .executes(ctx -> swap(ctx.getSource(), IdentifierArgument.getId(ctx, "blockEntity")))
                                    .then(argument("pos", BlockPosArgument.blockPos())
                                            .executes(ctx -> swapAtPos(ctx.getSource(), IdentifierArgument.getId(ctx, "blockEntity"), BlockPosArgument.getLoadedBlockPos(ctx, "pos"))))))));
        }
    }

    public static int blockEntityExists(CommandSourceStack source, BlockPos pos) {
        BlockEntity blockEntity = source.getLevel().getChunk(pos).blockEntities.get(pos);

        if(blockEntity == null) {
            source.sendSuccess(() ->
                    Component.literal("Block entity does not exist at this position"), false);
        }else{
            source.sendSuccess(() ->
                    Component.literal(String.format("Block entity does exist. Type: %s", BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType()))), false);
        }

        return 1;
    }

    public static int swapAtPos(CommandSourceStack source, Identifier id, BlockPos pos) throws CommandSyntaxException {
        if (!(source.getLevel().getBlockState(pos).getBlock() instanceof BaseEntityBlock)) {
            throw BLOCK_ENTITY_UNSUPPORTED.create();
        }
        swapAtPos(source.getLevel(), id, pos);
        source.sendSuccess(() ->
                Component.literal(String.format("The block entity at position %s has been changed to %s", pos.toShortString(), id.toString())), false);
        return 1;
    }

    public static void swapAtPos(ServerLevel world, Identifier id, BlockPos pos) throws CommandSyntaxException {
        BlockEntity current = world.getBlockEntity(pos);

        Optional<BlockEntity> blockEntity = BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(id).map(type -> {
            try {
                return !type.validBlocks.isEmpty() ? type.create(pos, type.validBlocks.iterator().next().defaultBlockState()) : null;
            } catch (Throwable throwable) {
                NSSE.LOGGER.error("Error occurred during instantiation of a block entity: ", throwable);
                return null;
            }
        });
        if(blockEntity.isEmpty()) throw UNKNOWN_TYPE.create();
        if(current != null) world.removeBlockEntity(pos);
        world.setBlockEntity(blockEntity.get());
    }

    public static int swap(CommandSourceStack source, Identifier blockEntity) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        ItemStack stack = player.getMainHandItem();
        if(!(stack.getItem() instanceof BlockItem)) throw IS_NOT_A_BLOCK.create();
        if(!(((BlockItem)stack.getItem()).getBlock() instanceof BaseEntityBlock)) throw BLOCK_ENTITY_UNSUPPORTED.create();

        CompoundTag compound = new CompoundTag();
        compound.putString("StoredBlockEntity", blockEntity.toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));

        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        stack.set(DataComponents.LORE, new ItemLore(Collections.singletonList(Component.literal("§f+"+ blockEntity).withStyle(ChatFormatting.WHITE))));

        source.sendSuccess(() ->
                Component.literal(String.format("%s now has %s", BuiltInRegistries.ITEM.getKey(stack.getItem()) , blockEntity)), false);
        return 1;
    }
}
