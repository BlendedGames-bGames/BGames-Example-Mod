package net.gsimken.bgamesmod.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class PickupBoostPotionRecipe extends BrewingRecipe {
    public PickupBoostPotionRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
        super(input, ingredient, output);
    }


    @Override
    public boolean isInput(ItemStack input) {
        return (PotionUtil.getPotion(input) == Potions.AWKWARD);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return (ingredient.getItem().asItem() == Items.ENDER_PEARL);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        return isInput(input) && isIngredient(ingredient) ? getOutput().copy() : ItemStack.EMPTY;

    }

    public ItemStack getOutput() {
        return PotionUtil.setPotion(new ItemStack(Items.POTION), ModPotions.PICKUP_BOOST_POTION.get());

    }
}
