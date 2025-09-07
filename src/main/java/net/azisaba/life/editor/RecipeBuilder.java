package net.azisaba.life.editor;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeBuilder {

    private ItemStack mainItem;
    private int modelData = 0;

    private List<ItemStack> requiredItems = new ArrayList<>(); // [変更] finalを削除
    private List<ItemStack> resultItems = new ArrayList<>();

    public ItemStack getMainItem() {
        return mainItem;
    }

    public void setMainItem(ItemStack mainItem) {
        this.mainItem = mainItem;
    }

    public int getModelData() {
        return modelData;
    }

    public void setModelData(int modelData) {
        this.modelData = modelData;
    }

    public List<ItemStack> getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(List<ItemStack> requiredItems) {
        this.requiredItems = requiredItems;
    }

    public List<ItemStack> getResultItems() {
        return resultItems;
    }

    public void addResultItem(ItemStack resultItem) {
        this.resultItems.add(resultItem);
    }

    public void clearResultItems() {
        this.resultItems.clear();
    }
}
