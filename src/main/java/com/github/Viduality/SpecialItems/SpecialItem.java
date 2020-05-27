package com.github.Viduality.SpecialItems;

/*
 * SpecialItems
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getLore() {
        return lore;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setLoreColor(String loreColor) {
        this.loreColor = loreColor;
    }

    public String getLoreColor() {
        return loreColor;
    }

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
        dataContainer.set(SpecialItems.KEY, PersistentDataType.STRING, getTag());

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
