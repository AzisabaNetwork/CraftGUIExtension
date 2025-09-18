package net.azisaba.life.utils;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RequiredOrResultItem {

    private final boolean isMythicItem;
    private final String mmid;
    private final Material type;
    private final String displayName;
    private final int amount;

    public RequiredOrResultItem(boolean isMythicItem, String mmid, Material type, String displayName, int amount) {
        this.isMythicItem = isMythicItem;
        this.mmid = mmid;
        this.type = type;
        this.displayName = displayName;
        this.amount = amount;
    }

    public boolean isMythicItem() { return isMythicItem; }
    public String getMmid() { return mmid; }
    public Material getType() { return type; }
    public String getDisplayName() { return displayName; }
    public int getAmount() { return amount; }

    public String resolveDisplayName() {
        if (this.getDisplayName() != null && !this.getDisplayName().isEmpty()) {
            return this.getDisplayName();
        }

        if (isMythicItem()) {
            MythicItem item = MythicMobs.inst().getItemManager().getItem(this.mmid).orElse(null);
            if (item != null) {
                ItemStack stack = BukkitAdapter.adapt(item.generateItemStack(1));
                if (stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
                    return stack.getItemMeta().getDisplayName();
                }
            }
            return this.mmid;
        } else {
            if (this.type != null) {
                ItemStack stack = new ItemStack(this.type);
                if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
                    return stack.getItemMeta().getDisplayName();
                }
                return WordUtils.capitalizeFully(this.type.name().replace("_", " ").toLowerCase());
            }
        }
        return "Unknown Item";
    }
}
