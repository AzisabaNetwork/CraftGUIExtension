package net.azisaba.life.utils;

import net.azisaba.life.CraftGUIExtension;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LangUtil {

    private final CraftGUIExtension plugin;
    private final Map<String, FileConfiguration> langMap = new HashMap<>();
    private final String defaultLang = "ja";

    public LangUtil(CraftGUIExtension plugin) {
        this.plugin = plugin;
    }

    public void loadLanguages() {
        langMap.clear();
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        String[] defaultLangFiles = {"lang_en.yml", "lang_ja.yml"};
        for (String fileName : defaultLangFiles) {
            File langFile = new File(langFolder, fileName);
            if (!langFile.exists()) {
                plugin.saveResource("lang/" + fileName, false);
            }
        }

        File[] langFiles = langFolder.listFiles((dir, name) -> name.startsWith("lang_") && name.endsWith(".yml"));
        if (langFiles == null) {
            plugin.getLogger().warning("言語フォルダの読み込みに失敗しました。");
            return;
        }

        for (File langFile : langFiles) {
            String fileName = langFile.getName();
            String langCode = fileName.substring(5, fileName.length() - 4);
            FileConfiguration config = YamlConfiguration.loadConfiguration(langFile);

            try (InputStream defaultConfigStream = plugin.getResource("lang/" + fileName)) {
                if (defaultConfigStream != null) {
                    YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8));
                    config.setDefaults(defaultConfig);
                    config.options().copyDefaults(true);
                    config.save(langFile);
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, fileName + " のデフォルト設定の読み込みまたは保存に失敗しました。", e);
            }

            langMap.put(langCode, config);
            plugin.getLogger().info(fileName + " を読み込みました。");
        }
    }

    public String getLangCode(Player player) {
        String locale = player.getLocale().toLowerCase();
        if (locale.length() >= 2) {
            return locale.substring(0, 2);
        }
        return defaultLang;
    }

    public String getMessage(String langCode, String key, String... replacements) {
        FileConfiguration config = langMap.get(langCode);
        if (config == null || !config.isSet(key)) {
            config = langMap.get(defaultLang);
            if (config == null || !config.isSet(key)) {
                return "§c[Missing Lang Key: " + key + "]";
            }
        }

        String message = config.getString(key, "§c[Invalid Lang Key: " + key + "]");
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (replacements.length > 0) {
            for (int i = 0; i < replacements.length; i += 2) {
                if (i + 1 < replacements.length) {
                    message = message.replace(replacements[i], replacements[i + 1]);
                }
            }
        }
        return message;
    }

    public String getMessage(Player player, String key, String... replacements) {
        String langCode = getLangCode(player);
        return getMessage(langCode, key, replacements);
    }

    public void sendMessage(Player player, String key, String... replacements) {
        String prefix = getMessage(player, "prefix");
        String message = getMessage(player, key, replacements);
        if (!prefix.isEmpty()) {
            player.sendMessage(prefix + " " + message);
        } else {
            player.sendMessage(message);
        }
    }
}