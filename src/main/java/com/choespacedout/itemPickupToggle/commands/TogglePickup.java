package com.choespacedout.itemPickupToggle.commands;

import com.choespacedout.itemPickupToggle.PlayerConfig;
import com.choespacedout.itemPickupToggle.PlayerConfigsCache;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TogglePickup {

    private static void togglePickup(Player player, boolean isToggled, File playerConfigs, PlayerConfigsCache playerConfigsCache) {
        UUID playerID = player.getUniqueId();

        PlayerConfig playerConfig = playerConfigsCache.getPlayerConfig(player.getUniqueId());
        playerConfig.setItemPickupToggle(isToggled);
        playerConfigsCache.setPlayerConfig(playerID, playerConfig);

        YamlConfiguration modifyPlayerConfigs = YamlConfiguration.loadConfiguration(playerConfigs);

        modifyPlayerConfigs.set(playerID + ".itemPickupToggle", isToggled);

        try {
            modifyPlayerConfigs.save(playerConfigs);
        } catch (IOException e) {
            player.sendRichMessage("<red>Error saving to player configs file! Please contact a staff member and report this error!");
            return;
        }
        if (isToggled) {
            player.sendRichMessage("<gray>Item pickups are now enabled");
        } else {
            player.sendRichMessage("<gray>Item pickups are now disabled");
        }
    }

    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName, File playerConfigs, PlayerConfigsCache playerConfigsCache) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player && sender.getSender().hasPermission("togglePickup.use"))
                .executes(ctx -> {
                    final Player player = (Player) ctx.getSource().getSender();
                    final UUID playerID = player.getUniqueId();

                    final boolean isToggled = !playerConfigsCache.getPlayerConfig(playerID).getItemPickupToggle();

                    togglePickup(player, isToggled, playerConfigs, playerConfigsCache);

                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("toggle", BoolArgumentType.bool())
                        .executes(ctx -> {
                            final Player player = (Player) ctx.getSource().getSender();
                            final UUID playerID = player.getUniqueId();

                            final boolean isToggled = ctx.getArgument("toggle", boolean.class);

                            togglePickup(player, isToggled, playerConfigs, playerConfigsCache);

                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }
}
