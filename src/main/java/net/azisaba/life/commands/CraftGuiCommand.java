package net.azisaba.life.commands;

import net.azisaba.life.CraftGUIExtension;
import net.azisaba.life.editor.RecipeEditorManager;
import net.azisaba.life.gui.GuiManager;
import net.azisaba.life.utils.MapUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftGuiCommand implements CommandExecutor, TabCompleter {

    private final CraftGUIExtension plugin;
    private final MapUtil mapUtil;
    private final GuiManager guiManager;
    private final RecipeEditorManager editorManager;

    public CraftGuiCommand(CraftGUIExtension plugin, MapUtil mapUtil, GuiManager guiManager, RecipeEditorManager editorManager) {
        this.plugin = plugin;
        this.mapUtil = mapUtil;
        this.guiManager = guiManager;
        this.editorManager = editorManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String CommandLabel, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                guiManager.openCraftGUI(player, 1);
                mapUtil.setPlayerPage(player.getUniqueId(), 1);
                player.sendMessage(ChatColor.DARK_GREEN + "CraftGUI Extensionを開きました");
            } else {
                sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
            }
            return true;
        }

        if (args.length == 1) {
            try {
                int page = Integer.parseInt(args[0]);
                if (sender instanceof Player) {
                    Player player = (Player) sender;

                    if (page <= 0) {
                        player.sendMessage(ChatColor.RED + "ページ番号は1以上で指定してください");
                        return true;
                    }

                    guiManager.openCraftGUI(player, page);
                    mapUtil.setPlayerPage(player.getUniqueId(), page);
                    player.sendMessage(ChatColor.DARK_GREEN + "CraftGUI Extensionの" + page + "ページ目を開きました");
                } else {
                    sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
                }
                return true;
            } catch (NumberFormatException e) {
            }
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("register")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("craftguiextension.command.admin")) {
                    editorManager.start(player);
                } else {
                    player.sendMessage(ChatColor.RED + "権限がありません");
                }
            } else {
                sender.sendMessage("このコマンドはプレイヤーのみ実行できます。");
            }
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("config")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("reload")) {
                if (args.length == 2) {
                    if (!sender.hasPermission("craftguiextension.command.admin")) {
                        sender.sendMessage(ChatColor.RED + "権限がありません");
                        return true;
                    }
                    plugin.reloadPluginConfig();
                    sender.sendMessage(ChatColor.GREEN + "CraftGUI ExtensionのConfigを再読み込みしました");
                    return true;
                }
                else if (args.length == 3 && args[2].equalsIgnoreCase("--external")) {
                    if (!sender.hasPermission("craftguiextension.command.admin")) {
                        sender.sendMessage(ChatColor.RED + "権限がありません");
                        return true;
                    }
                    String url = plugin.getConfig().getString("ConfigURL");
                    if (url == null || url.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "エラー：config.ymlにConfigURLが設定されていません！");
                        return true;
                    }
                    plugin.reloadPluginConfigFromUrl(url);
                    sender.sendMessage(ChatColor.GREEN + "CraftGUI ExtensionのConfigを外部URLから再読み込みしました");
                    return true;
                }
            }
            if (args.length > 1 && args[1].equalsIgnoreCase("set")) {
                if (args.length == 2) {
                    if (!sender.hasPermission("craftguiextension.command.admin")) {
                        sender.sendMessage(ChatColor.RED + "権限がありません");
                    } else {
                        sender.sendMessage("/craftgui config set <URL>");
                    }
                    return true;
                } else if (args.length == 3 && args[2].equalsIgnoreCase(args[2])) {
                    if (!sender.hasPermission("craftguiextension.command.admin")) {
                        sender.sendMessage(ChatColor.RED + "権限がありません");
                    } else {
                        plugin.getConfig().set("ConfigURL", args[2]);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "CraftGUIの外部参照リロードのURLを設定しました");
                    }
                    return true;
                }
            }
        }
        sender.sendMessage(ChatColor.RED + args[0] + "は不明なコマンドまたはページ番号です");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("craftguiextension.command.admin")) {
                completions.add("config");
            }
            if (sender.hasPermission("craftguiextension.command.admin")) {
                completions.add("register");
            }
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("config")) {
            if (sender.hasPermission("craftguiextension.command.admin")) {
                completions.add("reload");
            }
            return StringUtil.copyPartialMatches(args[1], completions, new ArrayList<>());
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("craftguiextension.command.admin")) {
                completions.add("--external");
            }
            return StringUtil.copyPartialMatches(args[2], completions, new ArrayList<>());
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("set")) {
            if (sender.hasPermission("craftguiextension.command.admin")) {
                completions.add("<URL>");
            }
            return StringUtil.copyPartialMatches(args[2], completions, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
