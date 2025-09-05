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
        name = "Bank Organizer",
        description = "Allows for easy bank organization into categories with custom colors and exclusions"
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
            new SpecialCaseRule(
                    ItemID.DRAMEN_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) != 1
            ),
            new SpecialCaseRule(
                    ItemID.LUNAR_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) != 1
            ),
            new SpecialCaseRule(
                    ItemID.SPIKED_BOOTS,
                    c -> Quest.DESERT_TREASURE_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OGRE_BELLOWS,
                    c -> Quest.RECIPE_FOR_DISASTER.getState(c) !=QuestState.FINISHED
                    && Quest.MOURNINGS_END_PART_I.getState(c) !=QuestState.FINISHED
                    && c.getVarpValue(Varbits.DIARY_WESTERN_ELITE) !=1
            ),
            new SpecialCaseRule(
                    ItemID.TROWEL,
                    c -> Quest.ANOTHER_SLICE_OF_HAM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BATTERED_BOOK,
                    c -> Quest.ELEMENTAL_WORKSHOP_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BATTERED_KEY,
                    c -> Quest.ELEMENTAL_WORKSHOP_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.FAKE_BEARD,
                    c -> Quest.WANTED.getState(c) !=QuestState.FINISHED
                    && Quest.FORGETTABLE_TALE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.RED_HOT_SAUCE,
                    c -> Quest.MY_ARMS_BIG_ADVENTURE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.RED_VINE_WORM,
                    c -> Quest.FAIRYTALE_I__GROWING_PAINS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DRAMEN_BRANCH,
                    c -> Quest.RECIPE_FOR_DISASTER__SIR_AMIK_VARZE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BLACK_MUSHROOM,
                    c -> Quest.SHADOW_OF_THE_STORM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.STRANGE_IMPLEMENT,
                    c -> Quest.SHADOW_OF_THE_STORM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BLACK_DYE,
                    c -> Quest.SHADOW_OF_THE_STORM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KARAMJAN_RUM,
                    c -> Quest.TAI_BWO_WANNAI_TRIO.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GHOSTSPEAK_AMULET,
                    c -> Quest.ANIMAL_MAGNETISM.getState(c) !=QuestState.FINISHED
                    && Quest.NATURE_SPIRIT.getState(c) !=QuestState.FINISHED
                    && Quest.GHOSTS_AHOY.getState(c) !=QuestState.FINISHED
                    && Quest.MAKING_HISTORY.getState(c) !=QuestState.FINISHED
                    && Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
                    && Quest.FAIRYTALE_I__GROWING_PAINS.getState(c) !=QuestState.FINISHED
                    && Quest.CABIN_FEVER.getState(c) != QuestState.FINISHED
                    && Quest.DRAGON_SLAYER_II.getState(c) != QuestState.FINISHED
                    && Quest.A_NIGHT_AT_THE_THEATRE.getState(c) != QuestState.FINISHED
                    && Quest.CURSE_OF_THE_EMPTY_LORD.getState(c) != QuestState.FINISHED
                    && Quest.THE_GENERALS_SHADOW.getState(c) != QuestState.FINISHED
                    && Quest.HOPESPEARS_WILL.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BROKEN_GLASS,
                    c -> Quest.FORGETTABLE_TALE.getState(c) != QuestState.FINISHED
                    && Quest.SEA_SLUG.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.ROGUES_PURSE,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) != QuestState.FINISHED
                    && Quest.JUNGLE_POTION.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GLARIALS_PEBBLE,
                    c -> Quest.ROVING_ELVES.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KEY_298,
                    c -> Quest.SONG_OF_THE_ELVES.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DOOR_KEY,
                    c -> Quest.GRIM_TALES.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAM_BOOTS,
                    c -> Quest.DEATH_TO_THE_DORGESHUUN.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAM_CLOAK,
                    c -> Quest.DEATH_TO_THE_DORGESHUUN.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAM_GLOVES,
                    c -> Quest.DEATH_TO_THE_DORGESHUUN.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAM_HOOD,
                    c -> Quest.DEATH_TO_THE_DORGESHUUN.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAM_LOGO,
                    c -> Quest.DEATH_TO_THE_DORGESHUUN.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAM_ROBE,
                    c -> Quest.DEATH_TO_THE_DORGESHUUN.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAM_SHIRT,
                    c -> Quest.DEATH_TO_THE_DORGESHUUN.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.COMMORB,
                    c -> Quest.WANTED.getState(c) != QuestState.FINISHED
                    && Quest.THE_SLUG_MENACE.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GOUTWEED,
                    c -> Quest.EADGARS_RUSE.getState(c) != QuestState.FINISHED
                    && Quest.DREAM_MENTOR.getState(c) != QuestState.FINISHED
                    && Quest.DRAGON_SLAYER_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SEAL_OF_PASSAGE,
                    c -> Quest.DREAM_MENTOR.getState(c) != QuestState.FINISHED
                    && c.getVarbitValue(Varbits.DIARY_FREMENNIK_ELITE) !=1
            ),
            new SpecialCaseRule(
                    ItemID.ANIMATE_ROCK_SCROLL,
                    c -> Quest.KINGS_RANSOM.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MSPEAK_AMULET,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MSPEAK_AMULET_4022,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BARREL_OF_NAPHTHA,
                    c -> Quest.MOURNINGS_END_PART_I.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BARREL_OF_COAL_TAR,
                    c -> Quest.MOURNINGS_END_PART_I.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KARAMJAN_MONKEY_GREEGREE,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KRUK_MONKEY_GREEGREE,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GORILLA_GREEGREE,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NINJA_MONKEY_GREEGREE,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.ZOMBIE_MONKEY_GREEGREE,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) != QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OGRE_BOW,
                    c -> Quest.RECIPE_FOR_DISASTER.getState(c) !=QuestState.FINISHED
                    && Quest.MOURNINGS_END_PART_I.getState(c) !=QuestState.FINISHED
                    && c.getVarpValue(Varbits.DIARY_WESTERN_ELITE) !=1
            ),
            new SpecialCaseRule(
                    ItemID.COMP_OGRE_BOW,
                    c -> Quest.RECIPE_FOR_DISASTER.getState(c) !=QuestState.FINISHED
                    && Quest.MOURNINGS_END_PART_I.getState(c) !=QuestState.FINISHED
                    && c.getVarpValue(Varbits.DIARY_WESTERN_ELITE) !=1
            ),
            new SpecialCaseRule(
                    ItemID.CATSPEAK_AMULET,
                    c -> Quest.RATCATCHERS.getState(c) !=QuestState.FINISHED
                    && Quest.A_TAIL_OF_TWO_CATS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SILVER_SICKLE_B,
                    c -> Quest.FAIRYTALE_I__GROWING_PAINS.getState(c) !=QuestState.FINISHED
                    && Quest.A_TASTE_OF_HOPE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOURNER_BOOTS,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOURNER_CLOAK,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOURNER_GLOVES,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOURNER_TOP,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOURNER_TROUSERS,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EXCALIBUR,
                    c -> Quest.HOLY_GRAIL.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DRAMEN_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) !=1
            ),
            new SpecialCaseRule(
                    ItemID.LUNAR_STAFF,
                    c -> c.getVarbitValue(Varbits.DIARY_LUMBRIDGE_ELITE) !=1
            ),
            new SpecialCaseRule(
                    ItemID.SNAKE_CHARM,
                    c -> Quest.RATCATCHERS.getState(c) !=QuestState.FINISHED
                    && Quest.THE_FEUD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.UGTHANKI_DUNG,
                    c -> Quest.FORGETTABLE_TALE.getState(c) !=QuestState.FINISHED
                    && Quest.MY_ARMS_BIG_ADVENTURE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GNOME_AMULET,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.KLANKS_GAUNTLETS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.RING_OF_CHAROS,
                    c -> Quest.GARDEN_OF_TRANQUILLITY.getState(c) !=QuestState.FINISHED
                    && Quest.THE_GREAT_BRAIN_ROBBERY.getState(c) !=QuestState.FINISHED
            ),
            new  SpecialCaseRule(
                    ItemID.BLURITE_ORE,
                    c -> c.getVarbitValue(Varbits.DIARY_FALADOR_EASY) !=1
            ),
            new SpecialCaseRule(
                    ItemID.CRYSTALMINE_KEY,
                    c -> Quest.HAUNTED_MINE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BEATEN_BOOK,
                    c -> c.getVarbitValue(Varbits.DIARY_KANDARIN_MEDIUM) !=1
            ),
            new SpecialCaseRule(
                    ItemID.BROOCH,
                    c -> Quest.THE_LOST_TRIBE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EAGLE_CAPE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.FAKE_BEAK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CAMEL_MASK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BOMBER_CAP,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BOMBER_JACKET,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ORIGAMI_BALLOON,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.PERFECT_RING,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.PERFECT_NECKLACE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.SHOES,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.KHAZARD_ARMOUR,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.PET_ROCK,
                    c -> c.getVarbitValue(Varbits.DIARY_FREMENNIK_MEDIUM) !=1
            ),
            new SpecialCaseRule(
                    ItemID.HAZEELS_MARK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CARNILLEAN_ARMOUR,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BLURITE_SWORD,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.PLAGUE_JACKET,
                    c -> Quest.SHEEP_HERDER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PLAGUE_TROUSERS,
                    c -> Quest.SHEEP_HERDER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SLAVE_BOOTS,
                    c -> Quest.THE_TOURIST_TRAP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SLAVE_ROBE,
                    c -> Quest.THE_TOURIST_TRAP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SLAVE_SHIRT,
                    c -> Quest.THE_TOURIST_TRAP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HARD_HAT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BUILDERS_BOOTS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BUILDERS_SHIRT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BUILDERS_TROUSERS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GOLD_HELMET,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GADDERHAMMER,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BEADS_OF_THE_DEAD,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.DARK_DAGGER,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GLOWING_DAGGER,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CRYSTAL_PENDANT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.FIXED_DEVICE,
                    c -> Quest.MOURNINGS_END_PART_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.RAT_POLE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.DOCTORS_HAT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.NURSE_HAT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BEDSHEET,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CRYSTAL_TRINKET,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.FISHING_PASS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BLESSED_GOLD_BOWL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.MOUSE_TOY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.PRAYER_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.WROUGHT_IRON_KEY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BEACON_RING,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.LOCATING_CRYSTAL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BERVIRIUS_NOTES,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BLACK_PRISM,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.STEEL_KEY_RING,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ARMADYL_PENDANT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.POLISHED_BUTTONS,
                    c -> Quest.ANIMAL_MAGNETISM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SHEEP_FEED,
                    c -> Quest.SHEEP_HERDER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MEDICAL_GOWN,
                    c -> Quest.BIOHAZARD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PIGEON_CAGE,
                    c -> Quest.BIOHAZARD.getState(c) !=QuestState.FINISHED
                    && Quest.ONE_SMALL_FAVOUR.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PIGEON_CAGE_425,
                    c -> Quest.BIOHAZARD.getState(c) !=QuestState.FINISHED
                    && Quest.ONE_SMALL_FAVOUR.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.RING_OF_CHAROSA,
                    c -> Quest.GARDEN_OF_TRANQUILLITY.getState(c) !=QuestState.FINISHED
                    && Quest.THE_GREAT_BRAIN_ROBBERY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.WOLFBANE,
                    c -> c.getVarbitValue(Varbits.DIARY_MORYTANIA_EASY) !=1
            ),
            new SpecialCaseRule(
                    ItemID.MITHRIL_SEEDS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.JUG_OF_VINEGAR,
                    c ->Quest.RAG_AND_BONE_MAN_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SILVER_NECKLACE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_OF_PORTRAITURE,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CATSPEAK_AMULETE,
                    c -> Quest.RATCATCHERS.getState(c) !=QuestState.FINISHED
                    && Quest.A_TAIL_OF_TWO_CATS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.METAL_FEATHER,
                    c -> Quest.EAGLES_PEAK.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TROLLWEISS,
                    c -> Quest.TROLL_ROMANCE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.FREMENNIK_SHIELD,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.FREMENNIK_HELM,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.FREMENNIK_BLADE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.LYRE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ROD_MOULD,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SILVTHRILL_ROD,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KHAZARD_HELMET,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GLOUGHS_JOURNAL,
                    c -> Quest.THE_GRAND_TREE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GLOUGHS_KEY,
                    c -> Quest.THE_GRAND_TREE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_ON_BAXTORIAN,
                    c -> Quest.WATERFALL_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.LEVEL_1_CERTIFICATE,
                    c -> Quest.RECIPE_FOR_DISASTER__ANOTHER_COOKS_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.LEVEL_2_CERTIFICATE,
                    c -> Quest.RECIPE_FOR_DISASTER__ANOTHER_COOKS_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.LEVEL_3_CERTIFICATE,
                    c -> Quest.RECIPE_FOR_DISASTER__ANOTHER_COOKS_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GNOME_ROYAL_SEAL,
                    c -> Quest.MONKEY_MADNESS_I.getState(c) !=QuestState.FINISHED
            ),
            new  SpecialCaseRule(
                    ItemID._10TH_SQUAD_SIGIL,
                    c -> Quest.MONKEY_MADNESS_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MAMULET_MOULD,
                    c -> Quest.MONKEY_MADNESS_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.FOSSIL_ISLAND_NOTE_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.FISHBOWL_AND_NET,
                    c -> Quest.RUM_DEAL.getState(c) !=QuestState.FINISHED
            )
    );
        // Add more special cases
    //.getState(c) !=QuestState.FINISHED
    //c -> false
    //c -> c.getVarbitValue(Varbits.
            // --- Example: Diary reward ---
            // Keep Ogre Bellows if WesProv Elite diary is NOT complete
            //      new SpecialCaseRule(ItemID.OGRE_BELLOWS,
            //      c -> c.getVarbitValue(Varbits.DIARY_WESTERN_ELITE) != 1),

            // --- Example: Skill-check ---
            // Keep Infernal Axe if Woodcutting level is below 99
            //      new SpecialCaseRule(ItemID.INFERNAL_AXE,
            //      c -> c.getRealSkillLevel(net.runelite.api.Skill.WOODCUTTING) < 99)


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
                    boolean match;
                    //Special logic only for cat7(tools)
                    if (catId == 7)
                    {
                        match = name.matches(".*\\b" + pattern + "s?\\b.*");
                    }
                    else
                    {
                        match = name.contains(pattern);
                    }

                    if (match)
                    {
                        matchedColors.add(getColorForCategory(catId));
                        break;
                    }
                }
            }

            // --- Special Items logic (quests, diaries, skills, etc.) ---
            if (config.specialItemsActive())
            {
                SPECIAL_CASES.stream()
                        .filter(rule -> rule.itemId == item.getId())
                        .findFirst()
                        .ifPresent(rule -> matchedColors.add(
                                rule.keepCondition.test(client)
                                        ? config.specialItemsKeepColor()
                                        : config.specialItemsDiscardColor()
                        ));
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
            case 6: raw = config.excludeCat6(); break;
            case 7: raw = config.excludeCat7(); break;
            case 8: raw = config.excludeCat8(); break;
            case 9: raw = config.excludeCat9(); break;
            case 10: raw = config.excludeCat10(); break;
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
            case 6: return config.colorCat6();
            case 7: return config.colorCat7();
            case 8: return config.colorCat8();
            case 9: return config.colorCat9();
            case 10: return config.colorCat10();
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
            case 6: return config.cat6Active();
            case 7: return config.cat7Active();
            case 8: return config.cat8Active();
            case 9: return config.cat9Active();
            case 10: return config.cat10Active();
            default: return false;
        }
    }

    @Provides
    BankOrganizerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BankOrganizerConfig.class);
    }
}

