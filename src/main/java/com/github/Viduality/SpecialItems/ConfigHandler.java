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

import de.themoep.minedown.MineDown;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigHandler {

    private static final SpecialItems plugin = SpecialItems.getInstance();

    private static StoredConfiguration specialItemsConfig;

    private static StoredConfiguration messagesConfig;

    public static void messagefromString(CommandSender sender, String string) {
        if (sender == null) {
            return;
        }

        String prefix = messagesConfig.getString("Prefix");
        String message = prefix + " " + messagesConfig.getString(string);

        sender.sendMessage(new MineDown(message).toComponent());
    }

    public static void reloadAllConfigs() {
        loadConfig();
        specialItemsConfig = loadConfig("SpecialItems", false);
        messagesConfig = loadConfig("Languages", "", true);
    }

    private static StoredConfiguration loadConfig(String name, boolean languages) {
        return loadConfig(name, name, languages);
    }

    private static void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }

    private static StoredConfiguration loadConfig(String folderName, String configName, boolean languages) {
        File folder = new File(plugin.getDataFolder(), folderName);
        File file;
        if (languages) {
            file = new File(folder, (configName.isEmpty() ? getLanguage() : configName + WordUtils.capitalize(getLanguage())) + ".yml");
        } else {
            file = new File(folder, (configName + ".yml"));
        }

        FileConfiguration defaultConfig = new YamlConfiguration();

        try (InputStream stream = plugin.getResource(file.getName())) {
            if (stream != null) {
                defaultConfig.load(new InputStreamReader(stream));
                if (!file.exists()) {
                    defaultConfig.save(file);
                }
            } else {
                System.out.println("Default config " + file.getName() + " does not exist in the plugin");
            }
        } catch (IOException | InvalidConfigurationException e) {
            System.out.println("Error while saving default config " + file.getName());
            e.printStackTrace();
        }

        if (!file.exists()) {
            file = new File(folder, (configName.isEmpty() ? "eng" : configName + "Eng.yml"));
        }

        StoredConfiguration config = new StoredConfiguration(file);
        if (file.exists()) {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                System.out.println("Encountered an error while loading " + folderName + " config from " + file.getPath());
                e.printStackTrace();
            }
        } else {
            System.out.println("No possible file for the " + folderName + " config exists? Checked for " + getLanguage() + " and eng versions.");
        }
        config.setDefaults(defaultConfig);
        return config;
    }

    private static String getLanguage() {
        return plugin.getConfig().getString("Language", "eng").toLowerCase();
    }

    public static FileConfiguration getSpecialItemsConfig1() {
        return specialItemsConfig;
    }

    public static FileConfiguration getNotesConfig() {
        return messagesConfig;
    }
}
