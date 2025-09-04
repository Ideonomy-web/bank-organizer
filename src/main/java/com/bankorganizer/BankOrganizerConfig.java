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

    // ===== Category 1 =====
    @ConfigItem(
            position = 1,
            keyName = "cat1Active",
            name = "Category 1 Active",
            description = "Enable or disable Category 1 highlighting"
    )
    default boolean cat1Active() { return true; }

    @ConfigItem(
            position = 2,
            keyName = "colorCat1",
            name = "Category 1 Color",
            description = "Color for category 1 items"
    )
    default Color colorCat1() { return Color.CYAN; }

    @ConfigItem(
            position = 3,
            keyName = "excludeCat1",
            name = "Exclude items (Cat 1)",
            description = "Comma-separated list of item names to exclude from Category 1"
    )
    default String excludeCat1() { return ""; }

    // ===== Category 2 =====
    @ConfigItem(
            position = 4,
            keyName = "cat2Active",
            name = "Category 2 Active",
            description = "Enable or disable Category 2 highlighting"
    )
    default boolean cat2Active() { return true; }

    @ConfigItem(
            position = 5,
            keyName = "colorCat2",
            name = "Category 2 Color",
            description = "Color for category 2 items"
    )
    default Color colorCat2() { return Color.RED; }

    @ConfigItem(
            position = 6,
            keyName = "excludeCat2",
            name = "Exclude items (Cat 2)",
            description = "Comma-separated list of item names to exclude from Category 2"
    )
    default String excludeCat2() { return ""; }

    // ===== Category 3 =====
    @ConfigItem(
            position = 7,
            keyName = "cat3Active",
            name = "Category 3 Active",
            description = "Enable or disable Category 3 highlighting"
    )
    default boolean cat3Active() { return true; }

    @ConfigItem(
            position = 8,
            keyName = "colorCat3",
            name = "Category 3 Color",
            description = "Color for category 3 items"
    )
    default Color colorCat3() { return Color.GREEN; }

    @ConfigItem(
            position = 9,
            keyName = "excludeCat3",
            name = "Exclude items (Cat 3)",
            description = "Comma-separated list of item names to exclude from Category 3"
    )
    default String excludeCat3() { return ""; }

    // ===== Category 4 =====
    @ConfigItem(
            position = 10,
            keyName = "cat4Active",
            name = "Category 4 Active",
            description = "Enable or disable Category 4 highlighting"
    )
    default boolean cat4Active() { return true; }

    @ConfigItem(
            position = 11,
            keyName = "colorCat4",
            name = "Category 4 Color",
            description = "Color for category 4 items"
    )
    default Color colorCat4() { return Color.MAGENTA; }

    @ConfigItem(
            position = 12,
            keyName = "excludeCat4",
            name = "Exclude items (Cat 4)",
            description = "Comma-separated list of item names to exclude from Category 4"
    )
    default String excludeCat4() { return ""; }

    // ===== Category 5 =====
    @ConfigItem(
            position = 13,
            keyName = "cat5Active",
            name = "Category 5 Active",
            description = "Enable or disable Category 5 highlighting"
    )
    default boolean cat5Active() { return true; }

    @ConfigItem(
            position = 14,
            keyName = "colorCat5",
            name = "Category 5 Color",
            description = "Color for category 5 items"
    )
    default Color colorCat5() { return Color.ORANGE; }

    @ConfigItem(
            position = 15,
            keyName = "excludeCat5",
            name = "Exclude items (Cat 5)",
            description = "Comma-separated list of item names to exclude from Category 5"
    )
    default String excludeCat5() { return ""; }

    // ===== Special Case Items (Header/Section) =====
    @ConfigSection(
            name = "Special Case Items",
            description = "Items with rules based on quests, diaries, skills, or other conditions",
            position = 50,
            closedByDefault = false
    )
    String specialCaseSection = "specialCaseSection";

    @ConfigItem(
            keyName = "specialItemsActive",
            name = "Special Case Items Active",
            description = "Enable or disable highlighting for items with special rules",
            position = 1,
            section = specialCaseSection
    )
    default boolean specialItemsActive() { return false; }

    @ConfigItem(
            keyName = "specialItemsKeepColor",
            name = "Keep Color",
            description = "Color for items when they should be kept",
            position = 2,
            section = specialCaseSection
    )
    default Color specialItemsKeepColor() { return Color.WHITE; }

    @ConfigItem(
            keyName = "specialItemsDiscardColor",
            name = "Discard Color",
            description = "Color for items when they should be discarded",
            position = 3,
            section = specialCaseSection
    )
    default Color specialItemsDiscardColor() { return Color.GRAY; }
}
