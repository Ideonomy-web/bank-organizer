package com.bankorganizer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import java.awt.Color;

@ConfigGroup("bankorganizer")
public interface BankOrganizerConfig extends Config
{
    @ConfigItem(
            keyName = "showBoxes",
            name = "Show Boxes",
            description = "Draw boxes over matching bank items"
    )
    default boolean showBoxes()
    {
        return true;
    }

    @ConfigItem(
            position = 1,
            keyName = "cat1Active",
            name = "Melee Categorization",
            description = "Enable or disable melee categorization"
    )
    default boolean cat1Active() { return true; }

    @ConfigItem(
            position = 2,
            keyName = "colorCat1",
            name = "Color",
            description = "Box Color for 'melee' items"
    )
    default Color colorCat1() { return Color.RED; }

    @ConfigItem(
            position = 3,
            keyName = "excludeCat1",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat1() { return "granite dust, granite cannonball, hunter spear tips, wilderness sword, oathplate shards, raw swordfish, swordfish, blue moon spear, godsword shard, godsword blade, defender ornament kit, godsword ornament kit"; }
    default String excludeCat1() { return ""; }


    @ConfigItem(
            position = 4,
            keyName = "cat2Active",
            name = "Magic Categorization",
            description = "Enable or disable 'magic' categorization"
    )
    default boolean cat2Active() { return true; }

    @ConfigItem(
            position = 5,
            keyName = "colorCat2",
            name = "Color",
            description = "Box Color for 'magic' items"
    )
    default Color colorCat2() { return Color.BLUE; }

    @ConfigItem(
            position = 6,
            keyName = "excludeCat2",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat2() { return "noose wand, hard hat, doctor's hat, enchanted symbol, banana hat, hat of the eye, elemental staff crown, runescroll of, mystic cards, battletsaff"; }
    default String excludeCat2() { return ""; }

    @ConfigItem(
            position = 7,
            keyName = "cat3Active",
            name = "Range Categorization",
            description = "Enable or disable 'range' categorization"
    )
    default boolean cat3Active() { return true; }

    @ConfigItem(
            position = 8,
            keyName = "colorCat3",
            name = "Color",
            description = "Box Color for 'range' items"
    )
        default Color colorCat3()
            {
            return new Color(19, 255, 0);
            }

    @ConfigItem(
            position = 9,
            keyName = "excludeCat3",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat3() { return "bowl, bowl wig, (unf), mixed hide base, crossbow string, fishbowl and net, blessed gold bowl, venator shard, snakeskin, (u), mixed hide cape, bronze arrowtips, iron arrowtips, steel arrowtips, mithril arrowtips, broad arrowheads, adamant arrowtips, rune arrowtips, amethyst arrowtips, dragon arrowtips, fishbowl helmet, amethyst javelin heads, rune javelin heads, dragon javelin heads, barrows teleport, (u), ballista limbs, ballista spring"; }
    default String excludeCat3() { return ""; }

    @ConfigItem(
            position = 10,
            keyName = "cat4Active",
            name = "Skilling Categorization",
            description = "Enable or disable 'skilling' categorization"
    )
    default boolean cat4Active() { return true; }

    @ConfigItem(
            position = 11,
            keyName = "colorCat4",
            name = "Color",
            description = "Box Color for 'skilling' items"
    )
    default Color colorCat4()
    {
        return new Color(0, 255, 255);
    }

    @ConfigItem(
            position = 12,
            keyName = "excludeCat4",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat4() { return "blighted super restore(1), blighted super restore(2), blighted super restore(3), blighted super restore(4), sapphire lantern, emerald lantern, diamond dragon bolts, dragonstone dragon bolts, emerald dragon bolts, jade dragon bolts, onyx dragon bolts, opal dragon bolts, pearl dragon bolts, ruby dragon bolts, sapphire dragon bolts, topaz dragon bolts, sapphite bolts, emerald bolts, ruby bolts, diamond bolts, dragonstone bolts, onyx bolts,  blighted super restore, ruby harvest, sapphire glacialis, zenyte shard, barrelchest anchor, bolts, barrows gloves, toadflax seed, super restore, (unf), toadflax, metal feather, black tourmaline core, lava battlestaff, iban's staff (u), mud battlestaff, steam battlestaff, smoke battlestaff, mist battlestaff, dust battlestaff, air battlestaff, water battlestaff, earth battlestaff, fire battlestaff, amethyst arrows, amethyst darts, amethyst javelins, runescroll of bloodbark, runescroll of swampbark, cannon barrels, cannon barrels (or), fish barrel, splitbark gauntlets, splitbark legs, splitbark boots, splitbark helm, splitbark body, barrows teleport, barrel of demonic tallow, machete"; }
    default String excludeCat4() { return ""; }

    @ConfigItem(
            position = 13,
            keyName = "cat5Active",
            name = "Potions Categorization",
            description = "Enable or disable 'potions' categorization"
    )
    default boolean cat5Active() { return true; }

    @ConfigItem(
            position = 14,
            keyName = "colorCat5",
            name = "Color",
            description = "Box Color for 'potion' items"
    )
    default Color colorCat5()
        {
        return new Color(147, 112, 219);
        }

    @ConfigItem(
            position = 15,
            keyName = "excludeCat5",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat5() { return "snape grass seed, guam seed, marrentill seed, tarromin seed, harralander seed, ranarr seed, toadflax seed, irit seed, avantoe seed, kwuarm seed, snapdragon seed, huasca seed, cadantine seed, lantadyme seed, dwarf weed seed, torstol seed, superior dragon bones, spirit flakes, guam seed, marrentill seed, tarromin seed, harralander seed, ranarr seed, toadflax seed, irit seed, avantoe seed, kwuarm seed, huasca seed, snapdragon seed, cadantine seed, lantadyme seed, dwarf weed seed, torstol seed"; }
    default String excludeCat5() { return ""; }

    @ConfigItem(
            position = 16,
            keyName = "cat6Active",
            name = "Slayer Loot Categorization",
            description = "Enable or disable 'slayer' categorization"
    )
    default boolean cat6Active() { return false; }

    @ConfigItem(
            position = 17,
            keyName = "colorCat6",
            name = "Color",
            description = "Box Color for 'slayer' items"
    )
    default Color colorCat6()
    {
        return new Color(207, 166, 11);
    }

    @ConfigItem(
            position = 18,
            keyName = "excludeCat6",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat6() { return "10th squad sigil, bonecrusher, hallowed crystal shard, bones to peaches, bone dagger"; }
    default String excludeCat6() { return ""; }

    @ConfigItem(
            position = 19,
            keyName = "cat7Active",
            name = "Tools & Misc Categorization",
            description = "Enable or disable 'tools & misc' categorization"
    )
    default boolean cat7Active() { return false; }

    @ConfigItem(
            position = 20,
            keyName = "colorCat7",
            name = "Color of Toolbox",
            description = "Box Color for 'tools' items"
    )
    default Color colorCat7()
    {
        return new Color(255, 123, 123);
    }

    @ConfigItem(
            position = 21,
            keyName = "excludeCat7",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat7() { return "bowl wig, bronze knife, iron knife, steel knife, black knife, mithril knife, adamant knife, rune knife, monkey talisman, xeric's talisman, reagent pouch, abyssal red dye, abyssal blue dye, abyssal green dye, dark dye, rune pouch, divine rune pouch, infernal cape, rune knife, nail beast nails, potato seed, battleaxe, wahammer, thrownaxe,potato cactus, potion, dragon warhammer, bucket of sand, metal feather, tuna potato, baked potato, dragon knife, thread of elidinis, torag's hammers, granite hammer"; }
    default String excludeCat7() { return ""; }

    @ConfigItem(
            position = 25,
            keyName = "cat8Active",
            name = "Food categorization",
            description = "Enable or disable 'food' categorization"
    )
    default boolean cat8Active() { return false; }

    @ConfigItem(
            position = 26,
            keyName = "colorCat8",
            name = "Color",
            description = "Box color for 'food' items"
    )
    default Color colorCat8() { return new Color(128, 0, 128); }

    @ConfigItem(
            position = 27,
            keyName = "excludeCat8",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //efault String excludeCat8() { return "curry leaf, lobster pot, curry tree seed, strawberry seed, crawling hand, strawberries, strawberry, seed, leaf, spiked boots, shark lure, wine of zamorak, half full wine jug, big shark, big bass, big swordfish, big harpoonfish"; }
    default String excludeCat8() { return ""; }

    @ConfigItem(
            position = 31,
            keyName = "cat9Active",
            name = "Farming Categorization",
            description = "Enable or disable 'farming' categorization"
    )
    default boolean cat9Active() { return true; }

    @ConfigItem(
            position = 32,
            keyName = "colorCat9",
            name = "Color",
            description = "Box color for 'farming' items"
    )
    default Color colorCat9() { return new Color(20, 100, 17); }

    @ConfigItem(
            position = 33,
            keyName = "excludeCat9",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat9() { return "banana hat, orange egg sac, diving apparatus, mithril grapple, mithril seeds, orange dye, apple pie, orange feather, cooked sweetcorn, baked potato, tuna potato, black mushroom, bowl, mushroom potato, mushroom & onion, sliced mushroom, fried mushroom, fossilised mushroom, uncooked mushroom pie, half a mushroom pie, mushroom pie, crystal tool seed, crystal armour seed, crystal weapon seed, enhanced crystal weapon seed, egg potato, seed pod, pineapple pizza, mith grapple, cursed banana, hallowed grapple"; }
    default String excludeCat9() { return ""; }

    @ConfigItem(
            position = 34,
            keyName = "cat10Active",
            name = "Skilling Outfit Categorization",
            description = "Enable or disable Category 10 highlighting"
    )
    default boolean cat10Active() { return false; }

    @ConfigItem(
            position = 35,
            keyName = "colorCat10",
            name = "Color",
            description = "Color for 'skilling outfit' items"
    )
    default Color colorCat10() { return new Color(150, 75, 0); }

    @ConfigItem(
            position = 36,
            keyName = "excludeCat10",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    //default String excludeCat10() { return "anglerfish, raw anglerfish"; }
    default String excludeCat10() { return ""; }

    /*
    @ConfigItem(
            position = 37,
            keyName = "cat11Active",
            name = "Sailing Categorization",
            description = "Enable or disable categorization for Sailing"
    )
    default boolean cat11Active() { return false; }

    @ConfigItem(
            position = 38,
            keyName = "colorCat11",
            name = "Color",
            description = "Box color for 'sailing' items"
    )
    default Color colorCat11() { return new Color(0, 100, 100); }

    @ConfigItem(
            position = 39,
            keyName = "excludeCat11",
            name = "Excluded items",
            description = "Comma-separated list of item names to exclude from category"
    )
    default String excludeCat11() { return "achey logs, willow logs, maple logs, arctic pine logs, yew logs, magic logs"; }
     */

    @ConfigSection(
            name = "Special Case Items",
            description = "Items with rules based on quests, diaries, skills, or other conditions (Recommended with high QP/Cape)",
            position = 50,
            closedByDefault = false
    )
    String specialCaseSection = "specialCaseSection";

    @ConfigItem(
            keyName = "stringConfig",
            name = "",
            description = "",
            position = 10,
            section = specialCaseSection
        )
    default String stringConfig() { return "En exhaustive bank clean-up for quest-item and diverse misc items; not niche achievement diary-specific & clue-doers inclusive - beware. "; }

    @ConfigItem(
            keyName = "specialItemsActive",
            name = "Bank Cleanup",
            description = "Enable or disable highlighting for items with special rules",
            position = 11,
            section = specialCaseSection
    )
    default boolean specialItemsActive() { return false; }

    @ConfigItem(
            keyName = "specialItemsKeepColor",
            name = "Keep (still used in quest)",
            description = "Color for items when they should be kept, still has a use in quests, etc",
            position = 12,
            section = specialCaseSection
    )
    default Color specialItemsKeepColor()
    {
        return new Color(255, 255, 255);
    }

    @ConfigItem(
            keyName = "specialItemsDiscardColor",
            name = "Discard Color",
            description = "Color for items when they are free to be discarded",
            position = 13,
            section = specialCaseSection
    )
    default Color specialItemsDiscardColor()
    {
        return new Color(0 , 0, 0);
    }
}
