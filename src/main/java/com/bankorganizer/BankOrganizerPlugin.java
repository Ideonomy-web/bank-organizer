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

    // Quest items and their associated quest
    private static final Map<Integer, Quest> QUEST_ITEM_MAP = Map.ofEntries(
            Map.entry(ItemID.ENCHANTED_KEY, Quest.MAKING_HISTORY),
            Map.entry(ItemID.GLARIALS_PEBBLE, Quest.WATERFALL_QUEST),
            Map.entry(ItemID.SEAL_OF_PASSAGE, Quest.THE_FREMENNIK_ISLES),
            Map.entry(ItemID.DRAMEN_BRANCH, Quest.LOST_CITY),
            Map.entry(ItemID.DRAMEN_STAFF, Quest.LOST_CITY),
            Map.entry(ItemID.LUNAR_STAFF, Quest.LUNAR_DIPLOMACY),
            Map.entry(ItemID.MAGIC_WHISTLE, Quest.LEGENDS_QUEST),
            Map.entry(ItemID.GOBLIN_SYMBOL_BOOK, Quest.LAND_OF_THE_GOBLINS)
            // ...complete list & add more quest-specific items here
    );

    /**
     * Special case: an item and a condition under which it should be kept.
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

    private static final List<SpecialCaseRule> SPECIAL_CASES = List.of(
            // E.g. Dramen Staff & Lunar Staff: keep if Lumbridge Elite diary NOT done
            new SpecialCaseRule(ItemID.DRAMEN_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) != 1),
            new SpecialCaseRule(ItemID.LUNAR_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) != 1)
            // Add more special cases
            // --- Example: Diary reward ---
            // Keep Ogre Bellows if WesProv Elite diary is NOT complete
            //      new SpecialCaseRule(ItemID.OGRE_BELLOWS,
            //      c -> c.getVarbitValue(Varbits.DIARY_WESTERN_ELITE) != 1),

            // --- Example: Skill-check ---
            // Keep Infernal Axe if Woodcutting level is below 99
            //      new SpecialCaseRule(ItemID.INFERNAL_AXE,
            //      c -> c.getRealSkillLevel(net.runelite.api.Skill.WOODCUTTING) < 99)
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

            final var def = client.getItemDefinition(item.getId());
            if (def == null)
            {
                continue; // Null safety
            }

            String name = def.getName().toLowerCase();
            List<Color> matchedColors = new ArrayList<>();

            // --- Category highlighting ---
            for (Map.Entry<Integer, List<String>> entry : ItemCategories.CATEGORY_PATTERNS.entrySet())
            {
                int catId = entry.getKey();

                if (!isCategoryActive(catId))
                {
                    continue;
                }

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

            // --- Special Items logic (quests, diaries, skills, etc.) ---
            if (config.specialItemsActive())
            {
                boolean handled = false;

                // 1. Refer to explicit special-case rule first (works for quest & non-quest items)
                for (SpecialCaseRule rule : SPECIAL_CASES)
                {
                    if (rule.itemId == item.getId())
                    {
                        matchedColors.add(rule.keepCondition.test(client)
                                ? config.specialItemsKeepColor()
                                : config.specialItemsDiscardColor());
                        handled = true;
                        break;
                    }
                }

                // 2. If no special-case rule matched, then check quest logic
                if (!handled)
                {
                    Quest quest = QUEST_ITEM_MAP.get(item.getId());
                    if (quest != null)
                    {
                        QuestState state = quest.getState(client);
                        matchedColors.add(state == QuestState.FINISHED
                                ? config.specialItemsDiscardColor()
                                : config.specialItemsKeepColor());
                    }
                }
            }

            if (!matchedColors.isEmpty())
            {
                overlay.markItem(item.getId(), matchedColors);
            }
        }
    }

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

    private boolean isCategoryActive(int catId)
    {
        switch (catId)
        {
            case 1: return config.cat1Active();
            case 2: return config.cat2Active();
            case 3: return config.cat3Active();
            case 4: return config.cat4Active();
            case 5: return config.cat5Active();
            default: return false;
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

