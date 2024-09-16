package ru.nern.notsoshadowextras.test;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.entity.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.test.GameTest;
import net.minecraft.test.PositionedException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.event.GameEvent;
import ru.nern.notsoshadowextras.NSSECommands;

public class NSSETests {
    @GameTest(templateName = "notsoshadowextras:trapdoor_so_suppression")
    public void stackoverflow_trapdoor_test(TestContext context) {
        try {
            context.setBlockState(3, 3, 3, Blocks.REDSTONE_WIRE);
        }catch (CrashException e) {
            context.setBlockState(4, 2, 2, Blocks.AIR);
            context.complete();
        }

    }

    @GameTest(templateName = "notsoshadowextras:cce_suppression")
    public void cce_suppression(TestContext context) throws CommandSyntaxException {
        NSSECommands.swapAtPos(context.getWorld(), Identifier.ofVanilla("lectern"),
                context.getAbsolutePos(new BlockPos(2, 2, 2)));
        try {
            context.setBlockState(4, 2, 2, Blocks.AIR);
        }catch (CrashException e) {
            context.expectBlock(Blocks.STONE_BUTTON, 4, 2, 1);
            context.expectBlock(Blocks.STONE_BUTTON, 4, 3, 2);
            context.expectBlock(Blocks.STONE_BUTTON, 4, 2, 3);
            context.setBlockState(2, 2, 2, Blocks.AIR);
            context.complete();
        }
    }

    @GameTest(templateName = "notsoshadowextras:floating_components")
    public void floating_components(TestContext context) {
        int[][] positions = new int[][]{{1, 3}, {2, 3}, {3, 3}, {3, 2}, {3, 1}, {2, 1}, {1, 1}};

        for(int[] position : positions) {
            context.useBlock(new BlockPos(position[0], 2, position[1]));
        }

        context.runAtTick(2, () -> {
            context.expectBlock(Blocks.COMPARATOR, 1, 3, 3);
            context.expectBlock(Blocks.REPEATER, 2, 3, 3);
            context.expectBlock(Blocks.REDSTONE_WIRE, 3, 3, 3);
            context.expectBlock(Blocks.RAIL, 3, 3, 2);
            context.expectBlock(Blocks.POWERED_RAIL, 3, 3, 1);
            context.expectBlock(Blocks.DETECTOR_RAIL, 2, 3, 1);
            context.expectBlock(Blocks.ACTIVATOR_RAIL, 1, 3, 1);
            context.complete();
        });
    }

    @GameTest(templateName = "notsoshadowextras:furnace_xp_dupe")
    public void furnace_xp_dupe(TestContext context) throws CommandSyntaxException {
        NSSECommands.swapAtPos(context.getWorld(), Identifier.ofVanilla("lectern"),
                context.getAbsolutePos(new BlockPos(3, 2, 2)));
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

        BlockPos furnacePos = new BlockPos(1, 2 ,2);
        try {
            context.getWorld().breakBlock(context.getAbsolutePos(furnacePos), false, player);
        }catch (CrashException ignored) {}

        context.expectEntityAround(EntityType.EXPERIENCE_ORB, furnacePos, 4);
        context.killAllEntities(ExperienceOrbEntity.class);
        context.setBlockState(3, 2, 2, Blocks.AIR);

        context.setBlockState(furnacePos, Blocks.FURNACE);
        context.getWorld().breakBlock(context.getAbsolutePos(furnacePos), false, player);
        context.expectEntityAround(EntityType.EXPERIENCE_ORB, furnacePos, 4);
        context.complete();
    }

    @GameTest(templateName = "notsoshadowextras:blockentity_swap")
    public void block_entity_swap(TestContext context) throws CommandSyntaxException {
        NSSECommands.swapAtPos(context.getWorld(), Identifier.ofVanilla("lectern"),
                context.getAbsolutePos(new BlockPos(3, 2, 2)));
        try {
            context.setBlockState(1, 2, 2, Blocks.AIR);

            context.setBlockState(1, 2, 2, Blocks.CHEST);
        }catch (CrashException ignored) {}

        context.setBlockState(3, 2, 2, Blocks.AIR);
        context.checkBlockEntity(new BlockPos(1, 2, 2), blockEntity -> blockEntity instanceof HopperBlockEntity,
                () -> "Block Entity Swap failed");

        context.complete();
    }

    @GameTest(templateName = "notsoshadowextras:sound_suppression")
    public void sound_suppression(TestContext context) throws CommandSyntaxException {
        NSSECommands.swapAtPos(context.getWorld(), Identifier.ofVanilla("calibrated_sculk_sensor"),
                context.getAbsolutePos(new BlockPos(1, 2, 2)));

        BlockPos pos = new BlockPos(1, 2, 1);
        BlockPos absolute = context.getAbsolutePos(pos);
        try {
            context.getWorld().emitGameEvent(GameEvent.EXPLODE, absolute, GameEvent.Emitter.of(context.getBlockState(pos)));
        }catch (IllegalArgumentException e) {
            context.complete();
        }
    }

    @GameTest(templateName = "notsoshadowextras:invulnerable_armor_stands")
    public void armor_stands_invulnerable_to_wither_damage(TestContext context) {
        ArmorStandEntity stand = context.getEntities(EntityType.ARMOR_STAND).getFirst();
        WitherEntity wither = new WitherEntity(EntityType.WITHER, context.getWorld());

        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(context.getWorld(), wither, new Vec3d(0, -10, 0));
        witherSkullEntity.setOwner(wither);

        witherSkullEntity.setPos(stand.getX(), stand.getY() + 6, stand.getZ());
        context.getWorld().spawnEntity(witherSkullEntity);

        context.runAtTick(100, () -> {
            context.expectEntity(EntityType.ARMOR_STAND);
            context.complete();
        });
    }

    @GameTest(templateName = "notsoshadowextras:shadow_items_in_mob_inventory")
    public void shadow_items_in_mob_inventory(TestContext context) {
        ItemStack armor = context.spawnItem(Items.DIAMOND_CHESTPLATE, 1, 2, 1).getStack();
        ItemStack handItem = context.spawnItem(Items.DIAMOND_SWORD, 1, 2, 1).getStack();

        ZombieEntity zombie = context.getEntitiesAround(EntityType.ZOMBIE, new BlockPos(1, 2, 1), 5).getFirst();

        context.runAtTick(40, () -> {
            context.testEntity(zombie, zombieEntity -> zombie.getEquippedStack(EquipmentSlot.MAINHAND) == handItem && zombie.getEquippedStack(EquipmentSlot.CHEST) == armor, "has_shadow_stacks");
            context.complete();
        });
    }

    @GameTest(templateName = "notsoshadowextras:1gt_blocks")
    public void blocks_1gt(TestContext context) {
        BlockPos bulbPos = new BlockPos(3, 2 ,2);
        context.setBlockState(bulbPos.up(), Blocks.REDSTONE_BLOCK);
        context.checkBlockState(bulbPos, blockState -> !blockState.get(Properties.LIT), () -> "Expected the copper bulb to not be lit yet");

        BlockPos crafterPos = new BlockPos(1, 2,2);
        context.setBlockState(crafterPos.up(), Blocks.REDSTONE_BLOCK);

        context.runAtTick(1, () -> {
            context.expectEntityAround(EntityType.ITEM, crafterPos, 4);
            context.complete();
        });
    }

    @GameTest(templateName = "notsoshadowextras:sound_suppression_generic")
    public void infinite_cake_fix(TestContext context) throws CommandSyntaxException {
        NSSECommands.swapAtPos(context.getWorld(), Identifier.ofVanilla("calibrated_sculk_sensor"),
                context.getAbsolutePos(new BlockPos(2, 2, 2)));

        BlockPos cakePos = new BlockPos(0, 2, 2);
        try {
            PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
            player.getHungerManager().setFoodLevel(0);
            context.useBlock(cakePos, player);
        }catch (IllegalArgumentException ignored) {}

        context.checkBlockState(cakePos, blockState -> blockState.get(CakeBlock.BITES) != 0, () -> "Cake is a lie :)");
        context.setBlockState(2, 2, 2, Blocks.AIR);
        context.complete();

    }

    @GameTest(templateName = "notsoshadowextras:sound_suppression_generic")
    public void item_interact_sound_dupe(TestContext context) throws CommandSyntaxException {
        NSSECommands.swapAtPos(context.getWorld(), Identifier.ofVanilla("calibrated_sculk_sensor"),
                context.getAbsolutePos(new BlockPos(2, 2, 2)));

        BlockPos interactionPos = new BlockPos(3, 1, 3);

        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

        //ArmorStandItemMixin test
        {
            player.setStackInHand(Hand.MAIN_HAND, Items.ARMOR_STAND.getDefaultStack());
            try {
                context.useBlock(interactionPos, player);
            } catch (IllegalArgumentException ignored) {}

            context.expectEntityAround(EntityType.ARMOR_STAND, interactionPos, 2);

            if (player.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
                throw new PositionedException("Armor Stand got duped", context.getAbsolutePos(interactionPos), interactionPos, 0);
            }
            context.killAllEntities();
        }

        ((LecternBlockEntity)context.getBlockEntity(new BlockPos(2, 2, 0))).setCurrentPage(13);


        //EndCrystalItemMixin test
        {
            player.setStackInHand(Hand.MAIN_HAND, Items.END_CRYSTAL.getDefaultStack());
            try {
                context.useBlock(interactionPos, player);
            } catch (IllegalArgumentException ignored) {}

            context.expectEntityAround(EntityType.END_CRYSTAL, interactionPos, 2);

            System.out.println("PLAYER ITEM: " + player.getStackInHand(Hand.MAIN_HAND).toString());
            if (player.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
                throw new PositionedException("End Crystal got duped", context.getAbsolutePos(interactionPos), interactionPos, 0);
            }
            context.killAllEntities();
        }

        //MinecartItemMixin test
        {
            player.setStackInHand(Hand.MAIN_HAND, Items.MINECART.getDefaultStack());
            context.setBlockState(interactionPos.up(), Blocks.RAIL);
            try {
                context.useBlock(interactionPos.up(), player);
            } catch (IllegalArgumentException ignored) {}

            context.expectEntityAround(EntityType.MINECART, interactionPos, 2);

            if (player.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
                throw new PositionedException("Minecart got duped", context.getAbsolutePos(interactionPos), interactionPos, 0);
            }
            context.setBlockState(interactionPos.up(), Blocks.AIR);
            context.killAllEntities();

            context.setBlockState(2, 2, 2, Blocks.AIR);
            context.complete();
        }


        //BlockItemMixin test
        {
            player.setStackInHand(Hand.MAIN_HAND, Items.DIRT.getDefaultStack());
            try {
                context.useBlock(interactionPos, player);
            } catch (IllegalArgumentException ignored) {}

            context.expectBlock(Blocks.DIRT, interactionPos.up());

            if (player.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
                throw new PositionedException("Generic Block Item got duped", context.getAbsolutePos(interactionPos), interactionPos, 0);
            }
            context.setBlockState(interactionPos.up(), Blocks.AIR);
        }

        //FireChargeItemMixin test
        {
            player.setStackInHand(Hand.MAIN_HAND, Items.FIRE_CHARGE.getDefaultStack());
            try {
                context.useBlock(interactionPos, player);
            } catch (IllegalArgumentException ignored) {}

            context.expectBlock(Blocks.FIRE, interactionPos.up());

            if (player.getStackInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
                throw new PositionedException("Fire Charge got duped", context.getAbsolutePos(interactionPos), interactionPos, 0);
            }
            context.setBlockState(interactionPos.up(), Blocks.AIR);
        }


    }

}
