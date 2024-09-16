package ru.nern.notsoshadowextras;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.Optional;

import static net.minecraft.server.command.CommandManager.literal;

public class NSSECommands {
    private static final SimpleCommandExceptionType IS_NOT_A_BLOCK = new SimpleCommandExceptionType(new LiteralMessage("Selected item is not a block"));
    private static final SimpleCommandExceptionType BLOCK_ENTITY_UNSUPPORTED = new SimpleCommandExceptionType(new LiteralMessage("Selected block can't have a block entity"));
    private static final SimpleCommandExceptionType UNKNOWN_TYPE = new SimpleCommandExceptionType(new LiteralMessage("Can't create a block entity of this type"));
    private static final SuggestionProvider<ServerCommandSource> BLOCK_ENTITIES = (context, builder) -> CommandSource.suggestIdentifiers(Registries.BLOCK_ENTITY_TYPE.getIds(), builder);

    protected static void init() {
        if(FabricLoader.getInstance().isModLoaded("fabric-command-api-v2") && NSSE.config().Additions.BlockEntitySwapCommand) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("nsse")
                    .requires(source -> source.hasPermissionLevel(3))
                    .then(literal("exists")
                            .then(CommandManager.argument( "pos", BlockPosArgumentType.blockPos())
                            .executes(ctx -> blockEntityExists(ctx.getSource(), BlockPosArgumentType.getLoadedBlockPos(ctx, "pos")))))
                    .then(literal("swap")
                            .then(CommandManager.literal("block")
                                    .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                            .then(literal("set")
                                                    .then(CommandManager.argument("blockEntity", IdentifierArgumentType.identifier()).suggests(BLOCK_ENTITIES)
                                                            .executes(ctx -> swapAtPos(ctx.getSource(), IdentifierArgumentType.getIdentifier(ctx, "blockEntity"), BlockPosArgumentType.getLoadedBlockPos(ctx, "pos")))))))
                            .then(CommandManager.argument("blockEntity", IdentifierArgumentType.identifier()).suggests(BLOCK_ENTITIES)
                                    .executes(ctx -> swap(ctx.getSource(), IdentifierArgumentType.getIdentifier(ctx, "blockEntity")))))));
        }
    }

    public static int blockEntityExists(ServerCommandSource source, BlockPos pos) {
        BlockEntity blockEntity = source.getWorld().getChunk(pos).blockEntities.get(pos);
        ;
        if(blockEntity == null) {
            source.sendFeedback(() ->
                    Text.literal("Block entity does not exist at this position"), false);
        }else{
            source.sendFeedback(() ->
                    Text.literal(String.format("Block entity does exist. Type: %s", blockEntity.getType().getRegistryEntry().getIdAsString())), false);
        }

        return 1;
    }

    public static int swapAtPos(ServerCommandSource source, Identifier id, BlockPos pos) throws CommandSyntaxException {
        swapAtPos(source.getWorld(), id, pos);
        source.sendFeedback(() ->
                Text.literal(String.format("The block entity at position %s has been changed to %s", pos.toShortString(), id.toString())), false);
        return 1;
    }

    public static void swapAtPos(ServerWorld world, Identifier id, BlockPos pos) throws CommandSyntaxException {
        BlockEntity current = world.getBlockEntity(pos);

        Optional<BlockEntity> blockEntity = Registries.BLOCK_ENTITY_TYPE.getOrEmpty(id).map(type -> {
            try {
                return type.instantiate(pos, world.getBlockState(pos));
            } catch (Throwable throwable) {
                return null;
            }
        });
        if(blockEntity.isEmpty()) throw UNKNOWN_TYPE.create();
        if(current != null) world.removeBlockEntity(pos);
        world.addBlockEntity(blockEntity.get());
    }

    public static int swap(ServerCommandSource source, Identifier blockEntity) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();

        ItemStack stack = player.getMainHandStack();
        if(!(stack.getItem() instanceof BlockItem)) throw IS_NOT_A_BLOCK.create();
        if(!(((BlockItem)stack.getItem()).getBlock() instanceof BlockWithEntity)) throw BLOCK_ENTITY_UNSUPPORTED.create();

        NbtCompound compound = new NbtCompound();
        compound.putString("StoredBlockEntity", blockEntity.toString());
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound));

        stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        stack.set(DataComponentTypes.LORE, new LoreComponent(Collections.singletonList(Text.literal("§f+"+ blockEntity).formatted(Formatting.WHITE))));

        source.sendFeedback(() ->
                Text.literal(String.format("%s now has %s", stack.getItem().getName(), blockEntity)), false);
        return 1;
    }
}
