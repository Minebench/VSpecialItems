package com.github.Viduality.VSpecialItems.Items;

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

import com.github.Viduality.VSpecialItems.ConfigHandler;
import com.github.Viduality.VSpecialItems.SpecialItem;
import com.github.Viduality.VSpecialItems.VSpecialItems;
import de.themoep.inventorygui.*;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChangeBiomeItem implements Listener {

    private final VSpecialItems plugin = VSpecialItems.getInstance();

    private final String invName = ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.inventoryName");

    private final List<Biome> biomes = getPossibleBiomes();

    public void createItem() {
        SpecialItem changeBiomeItem = new SpecialItem();
        changeBiomeItem.setTag("ChangeBiomeItem");
        changeBiomeItem.setName(ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.itemName"));
        changeBiomeItem.setNameColor(ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.itemNameColor"));
        changeBiomeItem.setLore(ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.itemLore"));
        changeBiomeItem.setLoreColor(ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.itemLoreColor"));
        changeBiomeItem.setItem(getItem());
        VSpecialItems.specialItems.put("ChangeBiomeItem", changeBiomeItem.buildItem());
    }

    private ItemStack getItem() {
        if (plugin.getConfig().getString("changeBiomeItem.item") != null) {
            String mat = plugin.getConfig().getString("changeBiomeItem.item").toUpperCase();
            if (Material.matchMaterial(mat) != null) {
                return new ItemStack(Material.getMaterial(mat), 1);
            }
        }
        return new ItemStack(Material.BARRIER, 1);
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemUse(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getHand() == EquipmentSlot.HAND) {
                    if (event.hasItem()) {
                        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                                .has(VSpecialItems.KEY, PersistentDataType.STRING)) {
                            if ((Objects.equals(VSpecialItems.specialItems.get("ChangeBiomeItem").getItemMeta().getPersistentDataContainer().get(VSpecialItems.KEY, PersistentDataType.STRING), player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                                    .get(VSpecialItems.KEY, PersistentDataType.STRING)))) {
                                if (!player.isSneaking()) {
                                    if (player.hasPermission("VSpecialItems.ChangeBiomeItemUse")) {
                                        InventoryGui gui = new InventoryGui(plugin, player, ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.inventoryName"), guiSetup);
                                        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
                                        gui.addElements(getBiomes(event.getClickedBlock()));
                                        gui.addElement(new GuiPageElement('f', new ItemStack(Material.ARROW), GuiPageElement.PageAction.FIRST, ConfigHandler.getNotesConfig().getString("gui.firstPage")));
                                        gui.addElement(new GuiPageElement('p', new ItemStack(Material.OAK_SIGN), GuiPageElement.PageAction.PREVIOUS, ConfigHandler.getNotesConfig().getString("gui.prevPage")));
                                        gui.addElement(new GuiPageElement('n', new ItemStack(Material.OAK_SIGN), GuiPageElement.PageAction.NEXT, ConfigHandler.getNotesConfig().getString("gui.nextPage")));
                                        gui.addElement(new GuiPageElement('l', new ItemStack(Material.ARROW), GuiPageElement.PageAction.LAST, ConfigHandler.getNotesConfig().getString("gui.lastPage")));
                                        gui.show(player);
                                    } else {
                                        plugin.sendMessage(player, "NoPermissionMechanic");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            if (player.getInventory().getItemInMainHand().hasItemMeta()) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                        .has(VSpecialItems.KEY, PersistentDataType.STRING)) {
                    if ((VSpecialItems.specialItems.get("ChangeBiomeItem").getItemMeta().getPersistentDataContainer().get(VSpecialItems.KEY, PersistentDataType.STRING).equals(
                            player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                                    .get(VSpecialItems.KEY, PersistentDataType.STRING)))) {
                        if (!player.isSneaking()) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(invName)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(invName)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventory(InventoryInteractEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(invName)) {
            event.setCancelled(true);
        }
    }

    private List<Biome> getPossibleBiomes() {
        List<Biome> biomes = new ArrayList<>();
        List<String> biomesStrings = plugin.getConfig().getStringList("changeBiomeItem.biomes");
        if (!biomesStrings.isEmpty()) {
            for (String current : biomesStrings) {
                int i = 0;
                for (Biome biome : Biome.values()) {
                    i++;
                    if (biome.name().equals(current)) {
                        biomes.add(biome);
                        break;
                    }
                    if (Biome.values().length == i) {
                        System.out.println("Could not find Biome for given String " + current + "! Check your config!");
                    }
                }
            }
        }
        return biomes;
    }

    private GuiElementGroup getBiomes(Block block) {
        List<GuiElement> elements = new ArrayList<>();
        for (Biome biome : biomes) {
            ItemStack item = new ItemStack(Material.GRASS_BLOCK, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.biomesColor") + ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.biomes." + biome.name()));
            itemMeta.setLore(Collections.singletonList(ChatColor.BLUE + biome.name()));
            item.setItemMeta(itemMeta);
            elements.add(new StaticGuiElement('b',
                    item,
                    click -> {
                        Chunk c = block.getChunk();
                        Player player = (Player) click.getEvent().getWhoClicked();
                        for (int x = 0; x <= 15; x++) {
                            for (int z = 0; z <= 15; z++) {
                                for (int y = 0; y <= 255; y++) {
                                    c.getBlock(x, y, z).setBiome(biome);
                                }
                            }
                        }
                        player.sendChunkChange(c);
                        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                        player.closeInventory();
                        plugin.sendMessage(player, "ChangedBiome", "%biome%", ConfigHandler.getNotesConfig().getString("items.changeBiomeItem.biomes." + biome.name()));
                        return true;
                    }));
        }
        GuiElement[] guiElements = new GuiElement[elements.size()];
        guiElements = elements.toArray(guiElements);
        return new GuiElementGroup('e', guiElements);
    }

    private final String[] guiSetup = {
            "eeeeeeeee",
            "  fpdnl  "
    };
}
