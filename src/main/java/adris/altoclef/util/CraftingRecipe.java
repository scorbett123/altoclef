package adris.altoclef.util;

import adris.altoclef.Debug;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CraftingRecipe {

    private ItemTarget[] _slots;

    private int _width, _height;

    private boolean _shapeless;

    private String _shortName;

    private int _outputCount;

    private Recipe _baseRecipe;

    // Every item in this list MUST match.
    // Used for beds where the wood can be anything
    // but the wool MUST be the same color.
    //private final Set<Integer> _mustMatch = new HashSet<>();

    private CraftingRecipe(Recipe _baseRecipe) {
    }

    public static CraftingRecipe createCraftingRecipe(Recipe baseRecipe) {
        if (baseRecipe instanceof ShapedRecipe) {
            return newShapedRecipe(baseRecipe);
        }
        return null;
    }

    public static CraftingRecipe newShapedRecipe(Recipe baseRecipe) {

        CraftingRecipe result = new CraftingRecipe(baseRecipe);
        result._shortName = baseRecipe.getOutput().getName().getString();
        result._outputCount = baseRecipe.getOutput().getCount();

        result._shapeless = false;

        ShapedRecipe recipe = (ShapedRecipe) baseRecipe;
        result._width = recipe.getWidth();
        result._height = recipe.getHeight();

        result._slots = new ItemTarget[recipe.getIngredients().size()];
        int i = 0;
        for (Ingredient ingredient : recipe.getIngredients()) {
            result._slots[i] = new ItemTarget( (Item[]) Arrays.stream(ingredient.getMatchingStacks()).map(ItemStack::getItem).toArray(), 1);
            i++;
        }

        return result;
    }

    private static ItemTarget[] createSlots(ItemTarget[] slots) {
        ItemTarget[] result = new ItemTarget[slots.length];
        System.arraycopy(slots, 0, result, 0, slots.length);
        return result;
    }

    private static ItemTarget[] createSlots(Item[][] slots) {
        ItemTarget[] result = new ItemTarget[slots.length];
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] == null) {
                result[i] = ItemTarget.EMPTY;
            } else {
                result[i] = new ItemTarget(slots[i]);
            }
        }
        return result;
    }

    public ItemTarget getSlot(int index) {

        return _slots[index];
    }

    public int getSlotCount() {
        return _slots.length;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public boolean isShapeless() {
        return _shapeless;
    }

    public boolean isBig() {
        return _slots.length > 4;
    }

    public int outputCount() {
        return _outputCount;
    }

    public Recipe getBaseRecipe() {
        return _baseRecipe;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CraftingRecipe) {
            CraftingRecipe other = (CraftingRecipe) o;
            if (other._shapeless != _shapeless) return false;
            if (other._outputCount != _outputCount) return false;
            if (other._height != _height) return false;
            if (other._width != _width) return false;
            //if (other._mustMatch.size() != _mustMatch.size()) return false;
            if (other._slots.length != _slots.length) return false;
            for (int i = 0; i < _slots.length; ++i) {
                if ((other._slots[i] == null) != (_slots[i] == null)) return false;
                if (other._slots[i] != null && !other._slots[i].equals(_slots[i])) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String name = "CraftingRecipe{";
        if (_shortName != null) {
            name += "craft " + _shortName;
        } else {
            name += "_slots=" + Arrays.toString(_slots) +
                    ", _width=" + _width +
                    ", _height=" + _height +
                    ", _shapeless=" + _shapeless;
        }
        name += "}";
        return name;
    }
}
