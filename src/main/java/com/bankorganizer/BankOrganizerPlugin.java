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
            ),
            new SpecialCaseRule(
                    ItemID.VARMENS_NOTES,
                    c -> Quest.THE_GOLEM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BLACK_DYE,
                    c -> Quest.THE_GOLEM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PHOENIX_FEATHER,
                    c -> Quest.THE_GOLEM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PHOENIX_QUILL_PEN,
                    c -> Quest.THE_GOLEM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DEMONIC_SIGIL,
                    c -> Quest.SHADOW_OF_THE_STORM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DEMONIC_SIGIL_MOULD,
                    c -> Quest.SHADOW_OF_THE_STORM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DEMONIC_TOME,
                    c -> Quest.SHADOW_OF_THE_STORM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BLESSED_AXE,
                    c -> Quest.ANIMAL_MAGNETISM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.UNDEAD_TWIGS,
                    c -> Quest.ANIMAL_MAGNETISM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GLARIALS_URN_EMPTY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.KHAZARD_CELL_KEYS,
                    c -> Quest.FIGHT_ARENA.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.WASHING_BOWL,
                    c -> Quest.NATURE_SPIRIT.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MIRROR,
                    c -> Quest.NATURE_SPIRIT.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.JOURNAL,
                    c -> Quest.NATURE_SPIRIT.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PICTURE,
                    c -> Quest.PLAGUE_CITY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.A_SCRUFFY_NOTE,
                    c -> Quest.PLAGUE_CITY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.INSECT_REPELLENT,
                    c -> Quest.MERLINS_CRYSTAL.getState(c) !=QuestState.FINISHED
                    && Quest.TROLL_ROMANCE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GOBLIN_SYMBOL_BOOK,
                    c -> Quest.THE_LOST_TRIBE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MAGIC_WHISTLE,
                    c -> Quest.HOLY_GRAIL.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HOLY_TABLE_NAPKIN,
                    c -> Quest.HOLY_GRAIL.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GRAIL_BELL,
                    c -> Quest.HOLY_GRAIL.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MAGIC_GOLD_FEATHER,
                    c -> Quest.HOLY_GRAIL.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CRUSHED_GEM,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CONSTRUCTION_GUIDE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ABYSSAL_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CATTLEPROD,
                    c -> Quest.SHEEP_HERDER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SHEEP_FEED,
                    c -> Quest.SHEEP_HERDER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.A_STONE_BOWL,
                    c -> Quest.ELEMENTAL_WORKSHOP_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CRANE_SCHEMATIC,
                    c -> Quest.ELEMENTAL_WORKSHOP_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.LEVER_SCHEMATIC,
                    c -> Quest.ELEMENTAL_WORKSHOP_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SCROLL_9721,
                    c -> Quest.ELEMENTAL_WORKSHOP_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.STONEPLAQUE,
                    c -> Quest.SHILO_VILLAGE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TATTERED_SCROLL,
                    c -> Quest.SHILO_VILLAGE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CRUMPLED_SCROLL,
                    c -> Quest.SHILO_VILLAGE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HISTORY_OF_IBAN,
                    c -> Quest.UNDERGROUND_PASS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OLD_JOURNAL,
                    c -> Quest.UNDERGROUND_PASS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SKAVID_MAP,
                    c -> Quest.WATCHTOWER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PANNING_TRAY,
                    c -> Quest.THE_DIG_SITE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NUGGETS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ARCENIA_ROOT,
                    c -> Quest.THE_DIG_SITE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EXTENDED_BRUSH,
                    c -> Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EXTENDED_BRUSH_4192,
                    c -> Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EXTENDED_BRUSH_4193,
                    c -> Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CONDUCTOR_MOULD,
                    c -> Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.JOURNAL_4203,
                    c -> Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NUFFS_CERTIFICATE,
                    c -> Quest.FAIRYTALE_II__CURE_A_QUEEN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TREASURE_MAP,
                    c -> Quest.GHOSTS_AHOY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.A_STONE_BOWL_2889,
                    c -> Quest.ELEMENTAL_WORKSHOP_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.RED_VINE_WORM,
                    c -> Quest.FISHING_CONTEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.A_USED_SPELL,
                    c -> Quest.NATURE_SPIRIT.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GOLDEN_WOOL,
                    c -> Quest.THE_FREMENNIK_TRIALS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BRANCH,
                    c -> Quest.THE_FREMENNIK_TRIALS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BOOK,
                    c -> Quest.SHIELD_OF_ARRAV.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.PHOENIX_CROSSBOW,
                    c -> Quest.SHIELD_OF_ARRAV.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.ROCK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.PLASTER_FRAGMENT,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DUSTY_SCROLL,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.THE_SLEEPING_SEVEN,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HISTORIES_OF_THE_HALLOWLAND,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MODERN_DAY_MORYTANIA,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BEARHEAD,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CINNAMON,
                    c -> Quest.RECIPE_FOR_DISASTER__SIR_AMIK_VARZE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.RED_BANANA,
                    c -> Quest.RECIPE_FOR_DISASTER__KING_AWOWOGEI.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SLICED_RED_BANANA,
                    c -> Quest.RECIPE_FOR_DISASTER__KING_AWOWOGEI.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TCHIKI_MONKEY_NUTS,
                    c -> Quest.RECIPE_FOR_DISASTER__KING_AWOWOGEI.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TCHIKI_NUT_PASTE,
                    c -> Quest.RECIPE_FOR_DISASTER__KING_AWOWOGEI.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SNAKE_CORPSE,
                    c -> Quest.RECIPE_FOR_DISASTER__KING_AWOWOGEI.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HOLLOW_REED,
                    c -> Quest.LEGENDS_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DRAGON_INN_TANKARD,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TORN_PAGE,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BLACK_PRISM,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OGRE_COFFIN_KEY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_OF_PORTRAITURE,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NECROMANCY_BOOK,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_OF_HAM,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BINDING_BOOK,
                    c -> Quest.LEGENDS_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TINY_NET,
                    c -> c.getVarbitValue(Varbits.DIARY_KANDARIN_EASY) != 1
            ),
            new SpecialCaseRule(
                    ItemID.LUNAR_ORE,
                    c -> Quest.LUNAR_DIPLOMACY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOONCLAN_MANUAL,
                    c -> Quest.LUNAR_DIPLOMACY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EMPTY_VIAL,
                    c -> Quest.LUNAR_DIPLOMACY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAIR_CLIP,
                    c -> Quest.KINGS_RANSOM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SAMPLE_BOTTLE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.A_DEAR_FRIEND,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.A_HANDWRITTEN_BOOK,
                    c -> Quest.THE_EYES_OF_GLOUPHRIE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.A_TASTE_OF_HOPE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ABLENKIANS_ESCAPE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.AIVAS_DIARY,
                    c -> Quest.DRAGON_SLAYER_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.AKILAS_JOURNAL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ANCIENT_DIARY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.APMEKENS_CAPTURE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ARACHNIDS_OF_VAMPYRIUM,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ARCHAEOLOGISTS_DIARY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ARENA_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.ASTRONOMY_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.AUBURN_VALLEY_ECOLOGICAL_REPORTS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BALLAD_OF_THE_BASILISK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BIG_BOOK_OF_BANGS,
                    c -> Quest.REGICIDE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BIRD_BOOK,
                    c -> Quest.EAGLES_PEAK.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BLOODY_DIARY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_OF_SPYOLOGY,
                    c -> Quest.MONKEY_MADNESS_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_ON_CHEMICALS,
                    c -> Quest.THE_DIG_SITE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BREWIN_GUIDE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.BURNT_DIARY,
                    c -> Quest.ROYAL_TROUBLE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CADARN_LINEAGE,
                    c -> Quest.ROVING_ELVES.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CLOCKWORK_BOOK,
                    c -> Quest.COLD_WAR.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.COCKTAIL_GUIDE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CRAZED_SCRIBBLES,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CREATURE_KEEPERS_JOURNAL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CRONDIS_CAPTURE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CRUMBLING_TOME,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.CRYSTAL_SINGING_FOR_BEGINNERS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.DAGONHAI_HISTORY,
                    c -> Quest.WHAT_LIES_BELOW.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DARK_JOURNAL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.DIARY,
                    c -> Quest.WITCHS_HOUSE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DIARY_3395,
                    c -> Quest.SHADES_OF_MORTTON.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DIARY_3846,
                    c -> Quest.HORROR_FROM_THE_DEEP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DWARVEN_LORE,
                    c -> Quest.BETWEEN_A_ROCK.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EASTERN_DISCOVERY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.EASTERN_SETTLEMENT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.EBRILLS_JOURNAL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.EDERNS_JOURNAL,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.EMBALMING_MANUAL,
                    c -> Quest.ICTHLARINS_LITTLE_HELPER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.ENTOMOLOGISTS_DIARY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.EXPLORERS_NOTES,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.FARMING_MANUAL,
                    c -> Quest.MY_ARMS_BIG_ADVENTURE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.FEATHERED_JOURNAL,
                    c -> Quest.EAGLES_PEAK.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GHRIMS_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GOBLIN_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GOLLWYNS_FINAL_STATEMENT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GLASSBLOWING_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.GUIDE_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.HARMONY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.HERMANS_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.HETS_CAPTURE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.HOUNDMASTERS_DIARY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.IMAFORES_BETRAYAL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.IMCANDORIAS_FALL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.INSTRUCTION_MANUAL,
                    c -> Quest.DWARF_CANNON.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.JOURNAL_3845,
                    c -> Quest.HORROR_FROM_THE_DEEP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.JOURNAL_6755,
                    c -> Quest.MAKING_HISTORY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KASONDES_JOURNAL,
                    c -> Quest.THE_GARDEN_OF_DEATH.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MANUAL,
                    c -> Quest.HORROR_FROM_THE_DEEP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOVARIOS_NOTES_VOLUME_1,
                    c -> Quest.WHILE_GUTHIX_SLEEPS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MOVARIOS_NOTES_VOLUME_2,
                    c -> Quest.WHILE_GUTHIX_SLEEPS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NEILANS_JOURNAL,
                    c -> Quest.GETTING_AHEAD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NISTIRIOS_MANIFESTO,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.OLD_DIARY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.OLD_JOURNAL,
                    c -> Quest.UNDERGROUND_PASS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SCABARAS_CAPTURE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.SECURITY_BOOK,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.SERAFINAS_DIARY,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.SHAMANS_TOME,
                    c -> Quest.LEGENDS_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SINKETHS_DIARY,
                    c -> Quest.WHAT_LIES_BELOW.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.STRONGHOLD_NOTES,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.TEKTONS_JOURNAL,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.THE_BUTCHER,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.THE_SHADOW_REALM,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.THE_WARDENS,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.THE_WILD_HUNT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.TOME_OF_THE_MOON,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.TOME_OF_THE_SUN,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.TOME_OF_THE_TEMPLE,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.TRANSDIMENSIONAL_NOTES,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.TRANSLATION,
                    c -> Quest.DESERT_TREASURE_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TRANSLATION_BOOK,
                    c -> Quest.THE_GRAND_TREE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.VANGUARD_JUDGEMENT,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.VARLAMORE_ENVOY,
                    c -> Quest.THE_DEPTHS_OF_DESPAIR.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.VERZIK_VITUR__PATIENT_RECORD,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.WEISS_FIRE_NOTES,
                    c -> Quest.MAKING_FRIENDS_WITH_MY_ARM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.LIFT_MANUAL,
                    c -> Quest.ROYAL_TROUBLE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_OF_HARICANTO,
                    c -> Quest.GHOSTS_AHOY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BOOK_ON_COSTUMES,
                    c -> Quest.THE_GIANT_DWARF.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.HAEMALCHEMY_VOLUME_1,
                    c -> Quest.DARKNESS_OF_HALLOWVALE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NEWSPAPER,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.A_SMALL_KEY,
                    c -> Quest.PLAGUE_CITY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.A_NICE_KEY,
                    c -> Quest.SPIRITS_OF_THE_ELID.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BASE_KEY,
                    c -> Quest.THE_CURSE_OF_ARRAV.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BEDABIN_KEY,
                    c -> Quest.THE_TOURIST_TRAP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BLUISH_KEY,
                    c -> Quest.A_KINGDOM_DIVIDED.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BONE_KEY_4272,
                    c -> Quest.GHOSTS_AHOY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BONE_KEY,
                    c -> Quest.SHILO_VILLAGE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.BRONZE_KEY,
                    c -> Quest.PRINCE_ALI_RESCUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CAVERN_KEY,
                    c -> Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CELL_DOOR_KEY,
                    c -> Quest.THE_TOURIST_TRAP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CELL_KEY,
                    c -> Quest.WHILE_GUTHIX_SLEEPS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CELL_KEY_1,
                    c -> Quest.TROLL_STRONGHOLD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CELL_KEY_2,
                    c -> Quest.TROLL_STRONGHOLD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CHEST_KEY_709,
                    c -> Quest.THE_DIG_SITE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CHEST_KEY_2404,
                    c -> Quest.HAZEEL_CULT.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CHEST_KEY_4273,
                    c -> Quest.GHOSTS_AHOY.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CHEST_KEY_28573,
                    c -> Quest.THE_PATH_OF_GLOUPHRIE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.COLD_KEY,
                    c -> Quest.A_KINGDOM_DIVIDED.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.COMBAT_DAMAGED_KEY,
                    c -> Quest.MONKEY_MADNESS_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.CRYPT_KEY,
                    c -> Quest.A_NIGHT_AT_THE_THEATRE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DAMP_KEY,
                    c -> Quest.A_KINGDOM_DIVIDED.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DISPLAY_CABINET_KEY,
                    c -> Quest.THE_GOLEM.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.GOBLIN_KITCHEN_KEY,
                    c -> Quest.OBSERVATORY_QUEST.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.ICY_KEY,
                    c -> Quest.SECRETS_OF_THE_NORTH.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KEY_423,
                    c -> Quest.BIOHAZARD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KEY_9722,
                    c -> Quest.ELEMENTAL_WORKSHOP_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KEY,
                    c -> Quest.ERNEST_THE_CHICKEN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KEY_293,
                    c -> Quest.TREE_GNOME_VILLAGE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KEY_5010,
                    c -> Quest.THE_LOST_TRIBE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KEYS,
                    c -> Quest.THE_FEUD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.MAZE_KEY,
                    c -> Quest.DRAGON_SLAYER_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.METAL_KEY,
                    c -> Quest.THE_TOURIST_TRAP.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NEW_KEY,
                    c -> Quest.MOURNINGS_END_PART_II.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OGRE_GATE_KEY,
                    c -> Quest.ZOGRE_FLESH_EATERS.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SHED_KEY,
                    c -> Quest.CREATURE_OF_FENKENSTRAIN.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.SHINY_KEY,
                    c -> Quest.TEMPLE_OF_IKOV.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.STOREROOM_KEY,
                    c -> Quest.EADGARS_RUSE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.STOREROOM_KEY_29906,
                    c -> Quest.ETHICALLY_ACQUIRED_ANTIQUITIES.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.STRONGROOM_KEY,
                    c -> Quest.THE_PATH_OF_GLOUPHRIE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TARNISHED_KEY,
                    c -> Quest.MOURNINGS_END_PART_I.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TEMPLE_LIBRARY_KEY,
                    c -> Quest.IN_AID_OF_THE_MYREQUE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.TOBANS_KEY,
                    c -> Quest.WATCHTOWER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.WEAPON_STORE_KEY,
                    c -> Quest.SHIELD_OF_ARRAV.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.ZEALOTS_KEY,
                    c -> Quest.HAUNTED_MINE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.DIRTY_NOTE,
                    c -> Quest.THE_GARDEN_OF_DEATH.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.FLAYGIANS_NOTES,
                    c -> Quest.A_TASTE_OF_HOPE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.KNIGHTS_NOTES,
                    c -> false
            ),
            new SpecialCaseRule(
                    ItemID.MEETING_NOTES,
                    c -> Quest.THE_GIANT_DWARF.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NOTE,
                    c -> Quest.THE_FEUD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NOTE_4598,
                    c -> Quest.THE_FEUD.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.NULODIONS_NOTES,
                    c -> Quest.DWARF_CANNON.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OLD_NOTES_22410,
                    c -> Quest.A_TASTE_OF_HOPE.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OLD_NOTES_22774,
                    c -> Quest.THE_FORSAKEN_TOWER.getState(c) !=QuestState.FINISHED
            ),
            new SpecialCaseRule(
                    ItemID.OLD_NOTE,
                    c -> Quest.SINS_OF_THE_FATHER.getState(c) !=QuestState.FINISHED
            )

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
                continue;
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
                boolean excluded = false;
                for (String ex : exclusions)
                {
                    if (!ex.isEmpty() && name.contains(ex))
                    {
                        excluded = true;
                        break;
                    }
                }
                if (excluded)
                {
                    continue;
                }

                for (String pattern : entry.getValue())
                {
                    boolean match;
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
        List<String> list = new ArrayList<>();

        // Hhidden defaults from ItemCategories
        List<String> base = ItemCategories.CATEGORY_DEFAULT_EXCLUSIONS
                .getOrDefault(catId, Collections.emptyList());
        for (String token : base)
        {
            if (token != null && !token.trim().isEmpty())
            {
                list.add(token.trim().toLowerCase());
            }
        }

        // User config
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
            // case 11: raw = config.excludeCat11(); break;
            default: raw = null; break;
        }

        if (raw != null && !raw.trim().isEmpty())
        {
            for (String token : raw.split(","))
            {
                if (token != null && !token.trim().isEmpty())
                {
                    list.add(token.trim().toLowerCase());
                }
            }
        }

        return list;
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
            //case 11: return config.colorCat11();
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
            //case 11: return config.cat11Active();
            default: return false;
        }
    }

    @Provides
    BankOrganizerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(BankOrganizerConfig.class);
    }
}

