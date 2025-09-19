package net.azisaba.life.utils;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MythicItemUtil {

    private static final BukkitAPIHelper mmHelper = new BukkitAPIHelper();

    public static String getMythicType(ItemStack stack) {
        if (stack == null || stack.getType().isAir()) return null;
        NBTTagCompound tag = CraftItemStack.asNMSCopy(stack).getTag();
        if (tag == null) return null;
        String type = tag.getString("MYTHIC_TYPE");
        if (type == null || type.isEmpty()) return null;
        return type;
    }

    public static String getDisplayNameFromMMID(String mmid) {
        MythicItem item = MythicMobs.inst().getItemManager().getItem(mmid).orElse(null);
        if (item == null) return mmid;
        String display = item.getDisplayName();
        if (display != null && !display.trim().isEmpty()) {
            return ChatColor.translateAlternateColorCodes('&', display);
        }
        return mmid;
    }

    public static String resolveDisplayName(RequiredOrResultItem item) {
        if (item.getDisplayName() != null && !item.getDisplayName().isEmpty()) {
            return ChatColor.translateAlternateColorCodes('&', item.getDisplayName());
        }

        if (item.isMythicItem()) {
            return getDisplayNameFromMMID(item.getMmid());
        } else if (item.getType() != null) {
            return capitalize(item.getType().name());
        }
        return "不明なアイテム";
    }

    private static String capitalize(String s) {
        return s.toLowerCase().replace("_", " ").substring(0, 1).toUpperCase() + s.toLowerCase().replace("_", " ").substring(1);
    }

    public static boolean matchesMythic(ItemStack stack, String targetMMID, String displayNameFromConfig) {
        String mmid = getMythicType(stack);
        ItemMeta meta = stack.getItemMeta();
        String displayName = (meta != null && meta.hasDisplayName()) ? meta.getDisplayName() : null;

        if (targetMMID != null && !targetMMID.isEmpty() && targetMMID.equalsIgnoreCase(mmid)) {
            return true;
        }
        if (displayNameFromConfig != null && !displayNameFromConfig.isEmpty() && displayName != null) {
            String configName = ChatColor.translateAlternateColorCodes('&', displayNameFromConfig);
            if (configName.equals(displayName)) {
                return true;
            }
        }
        return false;
    }
}