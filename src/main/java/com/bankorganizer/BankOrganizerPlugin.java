package com.bankorganizer;

import com.google.inject.Provides;
import javax.inject.Inject;
import java.awt.Color;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Varbits;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
        name = "Bank Organizer"
)
public class BankOrganizerPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private BankOrganizerConfig config;

    @Inject
    private BankOrganizerOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    // Quest items → associated quest
    private static final Map<Integer, Quest> QUEST_ITEM_MAP = Map.ofEntries(
            Map.entry(ItemID.ENCHANTED_KEY, Quest.MAKING_HISTORY),
            Map.entry(ItemID.GLARIALS_PEBBLE, Quest.WATERFALL_QUEST),
            Map.entry(ItemID.SEAL_OF_PASSAGE, Quest.THE_FREMENNIK_ISLES),
            Map.entry(ItemID.DRAMEN_BRANCH, Quest.LOST_CITY),
            Map.entry(ItemID.DRAMEN_STAFF, Quest.LOST_CITY),
            Map.entry(ItemID.LUNAR_STAFF, Quest.LUNAR_DIPLOMACY),
            Map.entry(ItemID.MAGIC_WHISTLE, Quest.LEGENDS_QUEST),
            Map.entry(ItemID.GOBLIN_SYMBOL_BOOK, Quest.LAND_OF_THE_GOBLINS)
            // ...add more quest-specific items here
    );

    /**
     * Special case rule: defines an item and a condition under which it should be kept.
     * If the condition returns false, the item is considered safe to discard.
     */
    private static class SpecialCaseRule
    {
        final int itemId;
        final Predicate<Client> keepCondition;

        SpecialCaseRule(int itemId, Predicate<Client> keepCondition)
        {
            this.itemId = itemId;
            this.keepCondition = keepCondition;
        }
    }

            // All special-case rules in one place
            private static final List<SpecialCaseRule> SPECIAL_CASES = List.of(
            // Dramen Staff & Lunar Staff: keep if Lumbridge Elite diary NOT done
            new SpecialCaseRule(ItemID.DRAMEN_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) != 1),
            new SpecialCaseRule(ItemID.LUNAR_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) != 1),
            new SpecialCaseRule(ItemID.SEAL_OF_PASSAGE,
                    c -> c.getVarbitValue(Varbits.DIARY_FREMENNIK_ELITE) != 1)

                    // Diary Check new SpecialCaseRule(ItemID.SOME_ITEM, c -> c.getVarbitValue(Varbits.SOME_DIARY_VARBIT) != 1)
                    // Quest Check new SpecialCaseRule(ItemID.ENCHANTED_KEY, c -> c.getQuest(Quest.MAKING_HISTORY).getState(c) != QuestState.FINISHED)  ||  (One or multiple quests)
                    // Skill Check new SpecialCaseRule(ItemID.SOME_ITEM, c -> c.getRealSkillLevel(Skill.AGILITY) < 70)
    );

    @Override
    protected void startUp()
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        overlay.clearMarks();
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (event.getContainerId() != InventoryID.BANK.getId())
        {
            return;
        }

        overlay.clearMarks();

        for (Item item : event.getItemContainer().getItems())
        {
            if (item.getId() <= 0)
            {
                continue;
            }

            String name = client.getItemDefinition(item.getId()).getName().toLowerCase();
            List<Color> matchedColors = new ArrayList<>();

            // --- Original 5 categories ---
            for (Map.Entry<Integer, List<String>> entry : ItemCategories.CATEGORY_PATTERNS.entrySet())
            {
                int catId = entry.getKey();
                List<String> exclusions = getExclusionsForCategory(catId);
                if (exclusions.contains(name))
                {
                    continue;
                }

                for (String pattern : entry.getValue())
                {
                    if (name.contains(pattern))
                    {
                        matchedColors.add(getColorForCategory(catId));
                        break;
                    }
                }
            }

            // --- Quest Items category ---
            Quest quest = QUEST_ITEM_MAP.get(item.getId());
            if (quest != null)
            {
                // Check if this item has a special-case rule
                SpecialCaseRule caseRule = SPECIAL_CASES.stream()
                        .filter(rule -> rule.itemId == item.getId())
                        .findFirst()
                        .orElse(null);

                if (caseRule != null)
                {
                    if (caseRule.keepCondition.test(client))
                    {
                        matchedColors.add(config.questItemsKeepColor());
                    }
                    else
                    {
                        matchedColors.add(config.questItemsDiscardColor());
                    }
                }
                else
                {
                    // Normal quest item logic
                    QuestState state = quest.getState(client);
                    if (state == QuestState.FINISHED)
                    {
                        matchedColors.add(config.questItemsDiscardColor());
                    }
                    else
                    {
                        matchedColors.add(config.questItemsKeepColor());
                    }
                }
            }

            if (!matchedColors.isEmpty())
            {
                overlay.markItem(item.getId(), matchedColors);
            }
        }
    }

    /**
     * Reads the exclusion string for a category from config and returns a lowercase list.
     */
    private List<String> getExclusionsForCategory(int catId)
    {
        String raw;
        switch (catId)
        {
            case 1: raw = config.excludeCat1(); break;
            case 2: raw = config.excludeCat2(); break;
            case 3: raw = config.excludeCat3(); break;
            case 4: raw = config.excludeCat4(); break;
            case 5: raw = config.excludeCat5(); break;
            default: return Collections.emptyList();
        }

        if (raw == null || raw.trim().isEmpty())
        {
            return Collections.emptyList();
        }

        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private Color getColorForCategory(int catId)
    {
        switch (catId)
        {
            case 1: return config.colorCat1();
            case 2: return config.colorCat2();
            case 3: return config.colorCat3();
            case 4: return config.colorCat4();
            case 5: return config.colorCat5();
            default: return Color.WHITE;
        }
    }

    @Provides
    BankOrganizerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BankOrganizerConfig.class);
    }
}





    /*
    Exclusions:
    (A).
    Only run exclusion check for Category 2
    if (catId == 2)
    {
        List<String> exclusions = getExclusionsForCategory(catId);
        if (exclusions.contains(name))
        {
            continue; // skip this category for this item
        }
    }

    for (String pattern : entry.getValue())
    {
        if (name.contains(pattern))
        {
            matchedColors.add(getColorForCategory(catId));
            break;
        }
    }
    */
                /*
                (B).
                Pattern-based exclusion
                boolean skip = false;
                for (String exclusionPattern : exclusions)
                {if (!exclusionPattern.isEmpty() && name.contains(exclusionPattern))
                    {
                        skip = true;
                        break;
                    }
                }
                if (skip)
                {
                    continue; // skip this category for this item
                }
                */

