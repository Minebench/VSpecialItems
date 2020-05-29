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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecialItem {

    private String name;
    private String lore;
    private ItemStack item;
    private String tag;
    private String nameColor = "§f";
    private String loreColor = "§9";

    /**
     * Sets the name of the item.
     * @param name  The item name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the item.
     * @return  The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the lore of the item.
     * @param lore  Lore of the item.
     */
    public void setLore(String lore) {
        this.lore = lore;
    }

    /**
     * Returns the lore of the item.
     * @return  The lore of the item.
     */
    public String getLore() {
        return lore;
    }

    /**
     * Sets the base item (ItemStack).
      * @param item  ItemStack of the special item.
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Returns the base ItemStack of the special item.
     * @return  The base ItemStack of the special item.
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Sets the tag for the item to identify the special item.
     * @param tag  String tag.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Returns the tag of the item.
     * @return  String tag of the item.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the color code for the item name.
     * Default: §f (White)
     * @param nameColor  color code of the item name
     */
    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    /**
     * Returns the color code of the item name.
     * @return  String color code.
     */
    public String getNameColor() {
        return nameColor;
    }

    /**
     * Sets the color code for the item lore.
     * Default: §9 (Blue)
     * @param loreColor  color code of the item lore
     */
    public void setLoreColor(String loreColor) {
        this.loreColor = loreColor;
    }

    /**
     * Returns the color code of the item lore.
     * @return  String color code.
     */
    public String getLoreColor() {
        return loreColor;
    }

    /**
     * Creates the special item.
     * Validates the item and sets default values if a param is not valid. Creates the special item.
     * @return  ItemStack of the special item.
     */
    public ItemStack buildItem() {
        validate();

        ItemStack item = getItem();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(getNameColor() + getName());

        List<String> lore = new ArrayList<>(splitString(getLore(), getLoreColor()));
        lore.add("");
        lore.add(ChatColor.BLUE + "SpecialItems");
        itemMeta.setLore(lore);

        itemMeta.addEnchant(Enchantment.WATER_WORKER, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(VSpecialItems.KEY, PersistentDataType.STRING, getTag());

        item.setItemMeta(itemMeta);
        return item;
    }

    private void validate() {
        if (getName() == null) {
            setName("Invalid Name");
        }
        if (getLore() == null) {
            setLore(" ");
        }
        if (getTag() == null) {
            setTag("SpecialItems Invalid Tag");
        }
        if (getItem() == null) {
            setItem(new ItemStack(Material.BARRIER, 1));
        }
    }

    private List<String> splitString(String string, String colorCode) {
        List<String> wordbyword = new ArrayList<>();
        if (string.length() < 30) {
            wordbyword.add(string);
        } else {
            wordbyword = Arrays.asList(string.split(" "));
        }

        List<String> splittedString = new ArrayList<>();
        int i = 0;
        String currentLine = null;
        for (String word : wordbyword) {
            i = i + word.length();
            if (i > 30) {
                if (word.length() >= 30) {
                    splittedString.add(currentLine);
                    splittedString.add(word);
                    i = 0;
                    currentLine = null;
                } else {
                    splittedString.add(colorCode + currentLine);
                    currentLine = word;
                    i = word.length();
                }
            } else {
                if (currentLine == null) {
                    currentLine = word;
                } else {
                    currentLine = currentLine + " " + word;
                }
            }
            if (wordbyword.get(wordbyword.size() - 1).equals(word)) {
                splittedString.add(colorCode + currentLine);
            }
        }
        return splittedString;
    }
}
