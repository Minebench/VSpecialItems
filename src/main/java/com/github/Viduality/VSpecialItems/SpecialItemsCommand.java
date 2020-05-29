package com.github.Viduality.VSpecialItems;

/*
 * VSpecialItems
 * Copyright (C) 2020, Viduality
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SpecialItemsCommand implements CommandExecutor {

    private final VSpecialItems plugin = VSpecialItems.getInstance();

    private VSpecialItems specialItemsCommand;

    public SpecialItemsCommand(VSpecialItems specialItemsCommand) {
        this.specialItemsCommand = specialItemsCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("vspecialitems")) {
                if (player.hasPermission("VSpecialItems.Command")) {
                    InventoryGui gui = new InventoryGui(plugin, player, ChatColor.RED + "Special Items", guiSetup);
                    gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
                    gui.addElement(new StaticGuiElement('c',
                            new ItemStack(VSpecialItems.specialItems.get("ChangeBiomeItem")),
                            click -> false
                    ));
                    gui.show(player);
                } else {
                    plugin.sendMessage(player, "NoPermission");
                }
            }
        } else {
            plugin.sendMessage(sender, "NoPlayer");
        }
        return true;
    }

    String[] guiSetup = {
            "  c  ",
    };
}
