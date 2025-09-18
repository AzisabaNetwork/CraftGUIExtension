package net.azisaba.life.utils;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

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
        if (item == null) {
            return mmid;
        }

        ItemStack stack = BukkitAdapter.adapt(item.generateItemStack(1));
        if (stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
            return stack.getItemMeta().getDisplayName();
        }
        return mmid;
    }

}