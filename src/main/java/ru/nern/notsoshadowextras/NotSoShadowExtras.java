package ru.nern.notsoshadowextras;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class NotSoShadowExtras implements ModInitializer {
	public static ConfigurationManager.Config config = new ConfigurationManager.Config();
	public static final Logger LOGGER = LoggerFactory.getLogger("notsoshadowextras");
	private static final SimpleCommandExceptionType IS_NOT_A_BLOCK = new SimpleCommandExceptionType(new LiteralMessage("Selected item is not a block"));
	private static final SimpleCommandExceptionType NO_BLOCK_ENTITY = new SimpleCommandExceptionType(new LiteralMessage("Selected block doesn't have a block entity"));

	private static final SuggestionProvider<ServerCommandSource> BLOCK_ENTITIES = (context, builder) -> CommandSource.suggestIdentifiers(Registries.BLOCK_ENTITY_TYPE.getIds(), builder);

	@Override
	public void onInitialize()
	{
		ConfigurationManager.onInit();

		if(FabricLoader.getInstance().isModLoaded("fabric-command-api-v2")) {
			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("nse")
					.requires(source -> source.hasPermissionLevel(3)).then(literal("swap")
							.then(CommandManager.argument("blockEntity", IdentifierArgumentType.identifier()).suggests(BLOCK_ENTITIES).executes(ctx -> swap(ctx.getSource(), IdentifierArgumentType.getIdentifier(ctx, "blockEntity")))))));
		}
	}

	public static int swap(ServerCommandSource source, Identifier blockEntity) throws CommandSyntaxException {
		ServerPlayerEntity player = source.getPlayerOrThrow();

		ItemStack stack = player.getMainHandStack();
		if(!(stack.getItem() instanceof BlockItem)) throw IS_NOT_A_BLOCK.create();
		if(!(((BlockItem)stack.getItem()).getBlock() instanceof BlockWithEntity)) throw NO_BLOCK_ENTITY.create();

		stack.setSubNbt("StoredBlockEntity", NbtString.of(blockEntity.toString()));

		NbtList list = new NbtList();
		list.add(new NbtCompound());
		stack.setSubNbt("Enchantments", list);

		NbtList lore = new NbtList();
		lore.add(NbtString.of(Text.Serialization.toJsonString(Text.literal("§f+"+blockEntity).formatted(Formatting.WHITE))));

		NbtCompound display = stack.getOrCreateSubNbt("display");
		display.put("Lore", lore);
		stack.setSubNbt("display", display);

		source.sendFeedback(() -> stack.getItem().getName().copy().append(Text.literal(" now has " +blockEntity)), false);
		return 1;
	}
}
