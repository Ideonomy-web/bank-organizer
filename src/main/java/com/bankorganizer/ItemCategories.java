package com.bankorganizer;

import java.util.*;

public class    ItemCategories
{
    public static final Map<Integer, List<String>> CATEGORY_PATTERNS = new HashMap<>();
    public static final Map<Integer, List<String>> CATEGORY_DEFAULT_EXCLUSIONS = new HashMap<>();

    static
    {
        //Melee
        CATEGORY_PATTERNS.put(1, Arrays.asList("defender", "full helm", "kiteshield", "plateskirt", "platelegs", "chainbody", "platebody", "initiate", "proselyte", "inquisitor", "rock-shell",
                "sunfire fanatic", "blood moon", "obsidian", "3rd age longsword", "bandos chestplate", "bandos tassets", "bandos boots", "justiciar", "dragonfire shield",
                "dinh's bulwark", "oathplate", "torva", "warrior helm", "berserker", "neitiznot", "dharok's", "torag's", "verac's", "guthan's", "serpentine helm", "tanzanite helm",
                "magma helm", "dwarven helmet", "fighter hat", "fighter torso", "runner hat", "granite helm", "granite body", "granite legs", "granite shield", "granite gloves", "granite boots",
                "granite maul", "granite longsword", "granite hammer", "granite ring", "dragonstone boots", "dragonstone gauntlets", "climbing boots", "rune boots", "guardian boots", "echo boots",
                "spiked manacles", "dragon boots", "primordial boots", "mace", "tzhaar-ket-em", "warhammer", "tzhaar-ket-om", "elder maul", "abyssal bludgeon", "barrelchest anchro",
                "dual macuahuitl", "glacial temotli", "keris", "sarachnis cudgel", "chainmace", "blade of saeldor", "scimitar", "longsword", "battleaxe", "zombie axe", "claws",
                "halberd", "2h", "godsword", "colossal blade", "saradomin sword", "abyssal tentacle", "abyssal whip", "scythe of vitur", "soulreaper axe", "sulphur blades", "toktz-xil-ek",
                "voidwaker", "silverlight", "darklight", "arclight", "emberlight", "arkan blade", "dagger", "sword", "toktz-xil-ak", "ghrazi rapier", "osmumten's fang", "spear", "hasta",
                "lance", "swift blade", "earthbound tecpatl", "ferocious gloves", "amulet of rancour", "infernal cape", "fire cape", "amulet of blood fury", "ivandis flail", "warrior ring", "ultor ring",
                "bellator ring", "ham joint", "crystal shield", "toktz-ket-xil", "book of war", "dragon sq shield", "dragon kiteshield", "mythical cape", "barrelchest anchor", "dragon med helm", "rune sq shield", "rune med helm", "soulflame horn", "soul reaper axe"));

        // Melee Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(1, Arrays.asList("granite dust", "granite cannonball", "hunter spear tips", "wilderness sword", "oathplate shards", "raw swordfish", "swordfish", "blue moon spear", "godsword shard",
                "godsword blade", "defender ornament kit", "godsword ornament kit"));

        //Magic
        CATEGORY_PATTERNS.put(2, Arrays.asList("air rune", "water rune", "earth rune", "fire rune", "sunfire rune", "mind rune", "body rune", "cosmic rune", "chaos rune",
                "astral rune", "nature rune", "law rune", "death rune", "blood rune", "soul rune", "wrath rune", "mist rune", "dust rune", "mud rune", "smoke rune",
                "steam rune", "lava rune", "aether rune", "staff", "wand", "sceptre", "toktz-mej-tal", "void knight mace","blue moon spear", "trident", "eye of ayak", "tumeken's shadow",
                "wizard hat", "wizard robe", "skeletal", "ancestral", "enchanted hat", "ghostly", "mystic", "xerician", "moonclan", "lunar", "splitbark", "swampbark", "bloodbark", "enchanted", "of darkness",
                "elder chaos", "dagon'hai", "infinity", "farseer helm", "blue moon", "ahrim's", "3rd age mage hat", "3rd age robe top", "3rd age amulet", "3rd age robe", "virtus",
                "zamorak monk top", "zamorak monk bottom", "tormented bracelet", "confliction gauntlets", "eternal boots", "wizard boots", "zamorak cape", "saradomin cape", "guthix cape",
                "amulet of magic", "occult necklace", "seers ring", "brimstone ring", "magus ring", "elidinis' ward", "book of the dead", "tome of", "book of darkness", "malediction ward",
                "mage's book", "ancient wyvern shield", "arcane spirit shield", "talisman", "rune pouch", "small pouch", "medium pouch", "large pouch", "giant pouch", "colossal pouch", "teleport",
                "quetzal histle", "calcified moth", "ectophial", "grand seed pod", "hallowed crystal shard", "icy basalt", "stony basalt", "plain of mud sphere", "goblin village sphere", "dorgesh-kaan sphere",
                "escape crystal", "chronicle", "kharedst's memoirs", "enchanted lyre", "xeric's talisman", "pendant of ates", "drakan's medallion", "burning amulet", "amulet of the eye", "giantsoul amulet", "amulet of eternal glory",
                "digsite pendant", "games necklace", "necklace of passage", "skills necklace", "slayer ring", "ring of returning", "ring of dueling", "healer hat", "moonclan hat",
                "mystic hat", "fremennik hat", "pink hat", "cream hat", "green hat", "blue hat", "turquoise hat", "grey hat", "red hat", "yellow hat", "teal hat", "purple hat", "ether catalyst", "blood essence", "master scroll book", "catalytic tiara", "ring of shadows"));

        //Magic Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(2, Arrays.asList("noose wand", "hard hat", "doctor's hat", "enchanted symbol", "banana hat", "hat of the eye", "elemental staff crown", "runescroll of",
                "mystic cards", "battlestaff", "colour kit"));

        //Range
        CATEGORY_PATTERNS.put(3, Arrays.asList("leather cowl", "snakeskin bandana", "snakeskin body", "snakeskin boots", "snakeskin chaps", "snakeskin shield", "snakeskin vambraces",
                "coif", "shayzien", "spined", "ranger hat", "archer helm", "hueycoatl hide body", "hueycoatl hide chaps", "karil's", "vambraces", "eclipse moon", "eclipse atlatl", "robin hood hat",
                "3rd age range", "3rd age bow", "crystal helm", "crystal body", "crystal legs", "armadyl helmet", "armadyl chestplate", "armadyl chainskirt", "masori", "hardleather body",
                "studded body", "studded chap", "frog-leather", "rangers' tunic", "d'hide", "mixed hide", "penance skirt", "rangers' tights", "ranger boots", "pegasian boots", "ranging cape",
                "ava's","dizana's", "necklace of anguish", "archers ring", "venator", "hard leather shield", "book of law", "odium ward", "dragonfire ward", "twisted buckler", "chaps",
                "shortbow", "longbow", "composite bow", "rain bow", "seercull", "dark bow", "craw's bow", "webweaver bow", "crystal bow", "scorching bow", "bow of faerdhinen", "venator bow",
                "twisted bow", "bow", "crossbow", "ballista", "dart", "bronze knife", "iron knife", "steel knife", "mithril knife", "adamant knife", "rune knife", "dragon knife",
                "thrownaxe", "chinchompa", "toktz-xil-ul", "hunter's spear", "blowpipe", "tonalztics of ralos", "arrow", "bolts", " brutal", "amethyst javelin", "rune javelin", "dragon javelin", "bracers", "bolt rack"));

        //Range Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(3, Arrays.asList("bowl", "bowl wig"," (unf)", "mixed hide base", "crossbow string", "fishbowl and net", "blessed gold bowl", "venator shard", "snakeskin"," (u)",
                "mixed hide cape", "bronze arrowtips", "iron arrowtips", "steel arrowtips", "mithril arrowtips", "broad arrowheads",
                "adamant arrowtips", "rune arrowtips", "amethyst arrowtips", "dragon arrowtips", "fishbowl helmet", "amethyst javelin heads",
                "rune javelin heads", "dragon javelin heads", "barrows teleport", "(u)", "ballista limbs", "ballista spring", "atlatl dart tips", "bow string", "shayzien hood", "dragon dart tip"));

        //Skilling
        CATEGORY_PATTERNS.put(4, Arrays.asList("bar","ore", "coal", "uncut", "opal", "jade", "topaz", "sapphire", "emerald", "ruby", "diamond", "dragonstone", "onyx", "zenyte", "giant seaweed",
                "soda ash", "bucket of sand", "molten glass", "clay", "plank", "logs", "battlestaff", "unpowered orb", "air orb", "water orb", "earth orb", "fire orb", "flax",
                "bow string", "(u)", "amethyst", "(unf)", "arrowtips", "javelin heads", "headless arrow", "bolt tips", "dart tip", "feather", "golden nugget", "stardust", "arrow shafts", "abyssal pearl", "dynamite", "hunter spear tips", "unidentified minerals", "termites"));
                //"jatoba logs", "camphor logs", "ironwood logs", "rosewood logs", "lead ore", "rubium ore", "rubium dust", "rubium splinters", "rubium geodes", "nickel ore",
                //"lead bar", "cupronickel bar"
                //"flax", "hemp", "cotton boll", "linen yarn", "hemp yarn", "cotton yarn", "bolt of linen", "bolt of canvas", "bolt of cotton"

        //Skilling Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(4, Arrays.asList("blighted super restore(1)", "blighted super restore(2)", "blighted super restore(3)", "blighted super restore(4)", "sapphire lantern",
                "emerald lantern", "diamond dragon bolts", "dragonstone dragon bolts", "emerald dragon bolts", "jade dragon bolts", "onyx dragon bolts",
                "opal dragon bolts", "pearl dragon bolts", "ruby dragon bolts", "sapphire dragon bolts", "topaz dragon bolts", "sapphite bolts", "emerald bolts",
                "ruby bolts", "diamond bolts", "dragonstone bolts", "onyx bolts", "blighted super restore", "ruby harvest", "sapphire glacialis",
                "zenyte shard", "barrelchest anchor", "bolts", "barrows gloves", "toadflax seed", "super restore"," (unf)", "toadflax",
                "metal feather", "black tourmaline core", "lava battlestaff", "iban's staff (u)", "mud battlestaff", "steam battlestaff", "smoke battlestaff",
                "mist battlestaff", "dust battlestaff", "air battlestaff", "water battlestaff", "earth battlestaff", "fire battlestaff", "amethyst arrows",
                "amethyst dart", "amethyst javelin", "runescroll of bloodbark", "runescroll of swampbark", "cannon barrels", "cannon barrels (or)",
                "fish barrel", "splitbark gauntlets", "splitbark legs", "splitbark boots", "splitbark helm", "splitbark body", "barrows teleport",
                "barrel of demonic tallow", "machete", "explorer's ring", "barronite handle", "barronite guard", "barronite head", "barbarian rod"));

        //Herblore/Potions
        CATEGORY_PATTERNS.put(5, Arrays.asList("potion", "grimy", "guam", "marrentill", "tarromin", "harralander", "ranarr", "toadflax", "irit", "avantoe", "kwuarm", "huasca", "snapdragon", "cadantine",
                "lantadyme", "dwarf weed", "torstol", "super", "eye of newt", "limpwurt root", "red spiders' eggs", "white berries", "snape grass", "mort myre fungus", "aldarium",
                "star flower", "nail beast nails", "yew roots", "lily of the sands", "wine of zamorak", "cactus spine", "potato cactus", "amylase crystal", "jangerberries", "magic roots",
                "cave nightshade", "nightshade", "poison ivy berries", "zulrah's scales", "ancient essence", "araxyte venom sack", "demonic tallow", "unicorn horn", "chocolate bar", "chocolate dust",
                "goat horn dust", "desert goat horn", "kebbit teeth", "gorak claws", "blue dragon scale", "dragon scale dust", "crystal dust", "crushed nest", "lava scale shard", "crushed superior dragon bones",
                "ashes", "volcanic ash", "nihil dust", "saradomin brew", "antidote", "anti-venom", "menaphite remedy","sanfew serum", "antifire potion", "extended antifire", "vial of water", "pestle and mortar",
                "ruby harvest", "sapphire glacialis", "black warlock", "moonlight moth", "sunlight moth", "reagent pouch", "alchemist's amulet", "prescription goggles", "bird nest", "coconut milk", "toad's legs",
                "chugging barrel", "alchemist labcoat", "alchemist pants", "alchemist gloves", "aga paste", "lye paste", "mox paste", "guam tar", "marrentill tar", "tarromin tar", "harralander tar", "irit tar",
                "swamp tar", "vial of blood"));
                //"anti-odour salt", "elkhorn coral", "crab paste", "anti-bleed tonic", "squid paste", "super fishing potion", "pillar coral", "haddock eye", "extreme energy potion", "yellow tuna fin"
                //"super hunter potion", "extended stamina potion", "marlin scales", "armadyl brew", "umbral coral", "rainbow crab paste"

        //Herblore Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(5, Arrays.asList("snape grass seed", "guam seed", "marrentill seed", "tarromin seed", "harralander seed", "ranarr seed",
                "toadflax seed", "irit seed", "avantoe seed", "kwuarm seed", "snapdragon seed", "huasca seed", "cadantine seed", "lantadyme seed",
                "dwarf weed seed", "torstol seed", "superior dragon bones", "spirit flakes", "vile ashes", "fiendish ashes", "malicious ashes", "abyssal ashes", "infernal ashes"));

        //Loot
        CATEGORY_PATTERNS.put(6, Arrays.asList("Head", "bone", "loop half of key", "tooth half of key", "crystal key", "bones", "bone shards", "ensouled", "zulrah's scale", "long bone",
                "rune full helm", "rune platebody", "rune platelegs", "rune plateskirt", "rune chainbody", "rune sq shield", "rune kiteshield", "rune med helm", "dragon halberd", "rune halberd",
                "rune spear", "rune warhammer", "rune battleaxe", "rune 2h sword", "rune longsword", "rune scimitar", "dragon dagger", "dragon longsword", "dragon scimitar", "dragon spear",
                "granite hammer", "granite maul", "abyssal whip", "dragon javelin heads", "amulet of the damned", "zenyte shard", "ballista spring", "frame", "ballista limbs", "unstrung heavy ballista",
                "dragon bolts (unf)", "ancient shard", "dark totem", "rune javelin heads", "dragon dart tip", "crystal shard", "crystal armour seed", "enhanced crystal weapon seed", "firelighter",
                "rune arrowtips", "dragonhide", "dragon arrowtips", "dragon plateskirt", "dragon platelegs", "dragon chainbody", "dragon med helm", "drake's tooth", "magic fang", "tanzanite fang",
                "serpentine visage", "hilt", "godsword shard", "thread of elidinis", "ancient essence", "dark bow", "hydra's", "hydra leather", "venator shard", "steam battlestaff", "hydra tail",
                "dragon sword", "dragon knife", "dragon thrownaxe", "shield left half", "awakener's orb", "granite gloves", "sunfire splinters", "jar of", "black tourmaline core", "kraken tentacle",
                "champion scroll", "coagulated venom", "egg sac", "charged ice", "rune boots", "dragonstone bolt tips", "skull of vet'ion", "prayer scroll", "eye of the duke", "leviathan's lure",
                "chromium ingot", "quartz", "zamorakian spear", "unsired", "araxyte fang", "noxious", "bryophyta", "tyrannical ring", "claws of callisto", "voicwaker hilt", "eternal crystal",
                "pegasian crystal", "primordial crystal", "smouldering stone", "dragon 2h", "odium shard", "malediction shard", "saradomin's light", "sigil", "holy elixir", "spirit shield", "mokhaiotl cloth",
                "vestige", "echo crystal", "crystal weapon seed", "hueycoatl hide", "draconic visage", "nihil horn", "volatile orb", "harmonised orb", "eldritch orb", "parasitic egg", "hill giant club",
                "element staff crown", "dark claw", "executioner's axe head", "treasonous ring", "fangs of venenatis", "voidwaker gem", "ring of the gods", "voidwaker blade", "skeletal visage",
                "dragonbone necklace", "siren's staff", "contract of", "crystal tool seed", "zolcano shard", "mutagen", "avernic defender hilt", "sanguine dust", "holy ornament kit", "sanguine ornament kit",
                "torn prayer scroll", "metamorphic dust", "twisted ancestral colour kit", "kodai insignia", "breach of the scarab", "eye of the corruptor", "jewel of the sun", "jewel of amascut",
                "eternal gem", "basilisk jaw", "tormented synapse", "oathplate shards", "onyx bolt", " impling jar", "mossy key", "giant key", "scroll box", "crawling hand", "cockatrice head", "basilisk head",
                "kurask head", "abyssal demon head", "kbd heads", "kq head", "vorkath's head", "alchemical hydra heads", "araxyte head", "brine sabre", "granite ring", "granite shield", "scurrius spine", "ornament kit", "(h1)", "(h2)", "(h3)", "(h4)",
                "(h5)"));

        //Loot Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(6, Arrays.asList("10th squad sigil", "bonecrusher", "hallowed crystal shard", "bones to peaches", "bone dagger", "crushed superior dragon bones", "bone bolts", "ghommal's hilt"));

        //Tools
        CATEGORY_PATTERNS.put(7, Arrays.asList("axe", "pickaxe", "butterfly net", "knife", "chisel", "hammer", "saw", "fishing rod", "fishing bait", "sandworms", "diabolic worms",
                "feather", "bucket", "pot", "fishing net", "rope", "lockpick", "tinderbox", "mould", "costume needle", "harpoon", "nails", "bowl", "karambwan vessel", "fish barrel",
                "farming rake", "seed dibber", "spade", "gricoller's can", "gardening trowel", "compost", "plant cure", "secatures", "infernal", "rogue kit", "trowel", "specimen", "talisman", "pouch", "dye",
                "candle", "rock pick", "jug of water", "box trap", "bird snare", "teasing stick", "noose wand", "butterfly jar", "ball of wool", "lantern","rabbit snare", "huntsman's kit", "goldsmith gauntlets",
                "ice gloves", "swamp tar", "swamp paste", "machete", "torch", "blackjack", "bonecrusher", "ash sanctifier", "ectoplasmator", "soul bearer", "spice", "shark lure", "holy symbol", "holy wrench", "tarn's diary",
                "vampire dust", "needle", "thread", "tackle box", "herb sack", "brown apron", "flamtaer", "glassblowing pipe", "gem bag", "fishing explosives", "olive oil", "sacred oil", "jar generator", "circlet of water", "impling jar",
                "butterfly jar", "crystal chime", "quetzal whistle", "blackstone fragment", "prayer book", "ghostspeak amulet", "smithing catalyst", "barbarian rod", "silver sickle", "ring of charos",
                "sled", "spirit flakes"));
                //"squid paste", "fish offcuts", "squid beak", "crab paste",

        //Tools Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(7, Arrays.asList("bowl wig", "bronze knife", "iron knife", "steel knife", "black knife", "mithril knife", "adamant knife", "rune knife",
                "monkey talisman", "xeric's talisman", "reagent pouch", "abyssal red dye", "abyssal blue dye", "abyssal green dye", "dark dye",
                "rune pouch", "divine rune pouch", "infernal cape", "rune knife", "nail beast nails", "potato seed",
                "battleaxe", "wahammer", "thrownaxe,potato cactus", "potion", "dragon warhammer", "bucket of sand", "metal feather", "tuna potato",
                "baked potato", "dragon knife", "thread of elidinis", "torag's hammers", "granite hammer", "executioner's axe head", "hunting knife", "baby impling jar",
                "young impling jar", "gourmet impling jar", "earth impling jar", "essence impling jar", "eclectic impling jar", "nature impling jar", "magpie impling jar", "ninja impling jar",
                "crystal impling jar", "dragon impling jar", "lucky impling jar"));

        //Food
        CATEGORY_PATTERNS.put(8, Arrays.asList("raw", "pie", "anchovies", "anglerfish", "baked potato", "bass", "blighted anglerfish", "blighted karambwan", "blighted manta ray",
                "wine", "bread", "cake", "chilli potato", "cod", "cooked", "curry", "dark crab", "egg potato", "giant carp", "herring", "kebab", "lobster", "mackeral", "manta ray",
                "pizza", "monkfish", "mushroom potato", "peach", "pike", "potato with cheese", "premade", "purple sweets", "roast", "salmon", "sardine", "sea turtle", "shark", "shrimp", "spicy",
                "sandwich", "stew", "strange fruit", "swordfish", "trout", "tuna", "cooking gauntlets"));
                //"red crab", "blue crab", "swordtip squid", "giant krill", "jumbo squid", "haddock", "rainbow crab", "yellowfin tuna", "halibut", "bluefin tuna", "marlin", "?crab meat"

        //Food Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(8, Arrays.asList("curry leaf", "lobster pot", "curry tree seed", "strawberry seed", "crawling hand", "strawberries", "strawberry", "seed", "leaf", "spiked boots",
                "shark lure", "wine of zamorak", "half full wine jug", "big shark", "big bass", "big swordfish", "big harpoonfish", "bones to peaches"));

        //Farming
        CATEGORY_PATTERNS.put(9, Arrays.asList("seeds", "seaweed spore", "seed", "plant pot", "watering can", "diving apparatus", "fishbowl helmet", "magic secatures", "empty sack", "basket", "tomatoes",
                "strawberries", "bananas", "apples", "curry leaf", "cabbage", "sweetcorn", "onion", "pineapple", "poison ivy berries", "watermelon", "strawberry", "papaya fruit",
                "coconut", "dragonfruit", "calquat fruit", "mushroom", "jangerberries", "grapes", "saltpetre", "nightshade", "cactus spine", "hops", "potato", "snape grass", "barley",
                "hammerstone", "asgarnian", "jute fibre", "yanillian", "krandorian", "wildblood", "redberries", "cadava berries", "dwellberries", "jangerberries", "white berries",
                "poison ivy berries", "roots", "apple", "banana", "orange", "zamorak's grapes", "potatoes(10)", "onions(10)", "cabbages(10)", "celastrus bark", "acorn", "seedling",
                "sapling", "farming rake", "seed dibber", "spade", "gricoller's can", "gardening trowel", "plant cure", "bottomless bucket", "compost", "mushroom spore"));
                //"elkhorn frags", "pillar frags", "umbral frags", "flax seed", "hemp seed", "cotton seed", "camphor seed", "ironwood seed", "rosewood seed"

        //Farming Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(9, Arrays.asList("banana hat", "orange egg sac", "mithril grapple", "mithril seeds", "orange dye",
                "apple pie", "orange feather", "cooked sweetcorn", "baked potato", "tuna potato", "black mushroom", "bowl", "mushroom potato", "mushroom & onion",
                "sliced mushroom", "fried mushroom", "fossilised mushroom", "uncooked mushroom pie", "half a mushroom pie", "mushroom pie", "crystal tool seed",
                "crystal armour seed", "crystal weapon seed", "enhanced crystal weapon seed", "egg potato", "seed pod", "pineapple pizza", "mith grapple",
                "cursed banana", "hallowed grapple", "coconut milk"));

        //Skilling outfits
        CATEGORY_PATTERNS.put(10, Arrays.asList("graceful", "carpenter", "farmer", "pyromancer", "angler", "spirit angler", "guild hunter", "prospector", "zealot", "raiments of the eye",
                "smiths", "rogue", "lumberjack", "forestry", "ardougne cloak", "desert amulet", "falador shield", "fremennik sea boots", "kandarin headgear", "karamja gloves", "rada's blessing", "explorer's ring", "morytania legs",
                "varrock armour", "western banner", "wilderness sword"));

        //Skilling outfits Exclusions
        CATEGORY_DEFAULT_EXCLUSIONS.put(10, Arrays.asList("anglerfish", "raw anglerfish"));

        //Sailing
        //CATEGORY_PATTERNS.put(11, Arrays.asList("tinderbox", "charcoal", "logs", "oak logs", "teak logs", "mahogany logs", "camphor log", "ironwood log", "rosewood log", "oak plank", "teak plank", "mahogany plank", "camphor plank", "ironwood plank", "rosewood plank",
        // "wooden hull part", "oak hull part", "teak hull part", "mahogany hull part", "camphor hull part", "ironwood hull part", "rosewood hull part", "bronze bar", "iron bar", "steel bar", "mithril bar", "adamant bar", "rune bar", "dragon sheet",
        // "bronze nails", "iron nails", "steel nails", "mithril nails", "adamantite nails", "rune nails", "dragon nails", "bronze keel part", "iron keel part", "steel keel part", "mithril keel part", "adamant keel part", "rune keel part",
        // "dragon keel part", "lead bar", "cupronickel bar", "bolt of linen", "bolt of canvas", "bolt of cotton", "swamp tar", "rope", "swamp paste", "linen yarn", "hemp yarn", "cotton yarn"));
        // "large keel part", "large hull part", "bronze cannonball", "iron cannonball", "steel cannonball", "mithril cannonball", "adamant cannonball", "rune cannonball", "dragon cannonball"
        // "repair kit", "oak repair kit", "teak repair kit", "mahogany repair kit", "camphor repair kit", "ironwood repair kit", "rosewood repair kit"

        //Tertiary Sailing related:
        //"jatoba logs", "camphor logs", "ironwood logs", "rosewood logs", "lead ore", "rubium ore", "rubium dust", "rubium splinters", "rubium geodes", "nickel ore", "lead bar", "cupronickel bar"
        //"flax", "hemp", "cotton boll", "linen yarn", "hemp yarn", "cotton yarn", "bolt of linen", "bolt of canvas", "bolt of cotton"
        //"anti-odour salt", "elkhorn coral", "crab paste", "anti-bleed tonic", "squid paste", "super fishing potion", "pillar coral", "haddock eye", "extreme energy potion", "yellow tuna fin"
        //"super hunter potion", "extended stamina potion", "marlin scales", "armadyl brew", "umbral coral", "rainbow crab paste"
        //"squid paste", "fish offcuts", "squid beak", "crab paste",
        //"red crab", "blue crab", "swordtip squid", "giant krill", "jumbo squid", "haddock", "rainbow crab", "yellowfin tuna", "halibut", "bluefin tuna", "marlin", "?crab meat"
        //"elkhorn frags", "pillar frags", "umbral frags", "flax seed", "hemp seed", "cotton seed", "camphor seed", "ironwood seed", "rosewood seed"

        //Sailing Exclusions
        //CATEGORY_DEFAULT_EXCLUSIONS.put(11, Arrays.asList("sailing example exclusion", "another exclusion"));
    }
}

//multi-target items - consider: slayer helmets, aranea boots, void, barrows gloves, anti-dragon shield