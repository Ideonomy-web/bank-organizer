package com.bankorganizer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
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
            keyName = "colorCat1",
            name = "Category 1 Color",
            description = "Color for category 1 items"
    )
    default Color colorCat1() { return Color.CYAN; }

    @ConfigItem(
            position = 2,
            keyName = "excludeCat1",
            name = "Exclude items (Cat 1)",
            description = "Comma-separated list of item names to exclude from Category 1"
    )
    default String excludeCat1() { return ""; }

    @ConfigItem(
            position = 3,
            keyName = "colorCat2",
            name = "Category 2 Color",
            description = "Color for category 2 items"
    )
    default Color colorCat2() { return Color.RED; }


    @ConfigItem(
            position = 4,
            keyName = "excludeCat2",
            name = "Exclude items (Cat 2)",
            description = "Comma-separated list of item names to exclude from Category 2"
    )
    default String excludeCat2() { return ""; }

    @ConfigItem(
            position = 5,
            keyName = "colorCat3",
            name = "Category 3 Color",
            description = "Color for category 3 items"
    )
    default Color colorCat3() { return Color.GREEN; }


    @ConfigItem(
            position = 6,
            keyName = "excludeCat3",
            name = "Exclude items (Cat 3)",
            description = "Comma-separated list of item names to exclude from Category 3"
    )
    default String excludeCat3() { return ""; }

    @ConfigItem(
            position = 7,
            keyName = "colorCat4",
            name = "Category 4 Color",
            description = "Color for category 4 items"
    )
    default Color colorCat4() { return Color.MAGENTA; }


    @ConfigItem(
            position = 8,
            keyName = "excludeCat4",
            name = "Exclude items (Cat 4)",
            description = "Comma-separated list of item names to exclude from Category 4"
    )
    default String excludeCat4() { return ""; }

    @ConfigItem(
            position = 9,
            keyName = "colorCat5",
            name = "Category 5 Color",
            description = "Color for category 5 items"
    )
    default Color colorCat5() { return Color.ORANGE; }


    @ConfigItem(
            position = 10,
            keyName = "excludeCat5",
            name = "Exclude items (Cat 5)",
            description = "Comma-separated list of item names to exclude from Category 5"
    )
    default String excludeCat5() { return ""; }

    // Section header for Quest Items
    @ConfigItem(
            position = 50,
            keyName = "questSectionHeader",
            name = "=== Quest Items ===",
            description = ""
    )
    default String questSectionHeader() { return null; }

    @ConfigItem(
            position = 51,
            keyName = "questItemsKeepColor",
            name = "Quest Items (Keep) Color",
            description = "Color for quest items when quest is NOT completed"
    )
    default Color questItemsKeepColor() { return Color.WHITE; }

    @ConfigItem(
            position = 52,
            keyName = "questItemsDiscardColor",
            name = "Quest Items (Discard) Color",
            description = "Color for quest items when quest IS completed"
    )
    default Color questItemsDiscardColor() { return Color.GRAY; }
}