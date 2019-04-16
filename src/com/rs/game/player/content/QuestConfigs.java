package com.rs.game.player.content;

import com.rs.game.player.Player;

;

public class QuestConfigs {

            public static void SendQuestList(Player player) {
                //* F2P QUESTS *\\
				
                player.getPackets().sendConfig(281, 1000); // Learning The Ropes
                player.getPackets().sendConfig(130, 4); // Black Knight's Fortress
                player.getPackets().sendConfig(29, 2); // Cook's Assistant
                player.getPackets().sendConfig(222, 3); // Demon Slayer
                player.getPackets().sendConfig(31, 100); // Doric's Quest
                player.getPackets().sendConfig(176, 10); // Dragon Slayer
                player.getPackets().sendConfig(32, 3); // Ernest The Chicken
                player.getPackets().sendConfig(62, 6); // Goblin Diplomancy
                player.getPackets().sendConfig(160, 2); // Imp Catcher
                player.getPackets().sendConfig(122, 7); // The Knight's Sword
                player.getPackets().sendConfig(71, 4); // Pirate's Treasure
                player.getPackets().sendConfig(273, 110); // Prince Ali Rescue
                player.getPackets().sendConfig(107, 5); // The Restless Ghost
                player.getPackets().sendConfig(144, 100); // Romeo & Juliet
                player.getPackets().sendConfig(63, 6); // Rune Mysteries
                player.getPackets().sendConfig(179, 21); // Sheep Shearer
                player.getPackets().sendConfig(145, 7); // Shield of Arrav
                player.getPackets().sendConfig(178, 3); // Vampire Slayer
                player.getPackets().sendConfig(67, 3); // Witch's Potion
                //* MEMBER QUESTS *\\
                player.getPackets().sendConfig(939, 1000); // Animal Magnetism
                player.getPackets().sendConfig(433, 154); // Between a Rock
                player.getPackets().sendConfig(293, 87); // Big Chompy Bird Hunting
                player.getPackets().sendConfig(68, 10000); // Biohazard
                player.getPackets().sendConfig(655, 145); // Cabin Fever
                player.getPackets().sendConfig(10, 100); // Clock Tower
                player.getPackets().sendConfig(964, 3000); // Contact!
                player.getPackets().sendConfig(399, 85); // Creature of Fenkenstrain
                player.getPackets().sendConfig(869, 1000); // Darkness of Hallowvale
                player.getPackets().sendConfig(314, 99); // Death Plateau
                player.getPackets().sendConfig(794, 153); // Death to the Dorgeshuun
                player.getPackets().sendConfig(440, 86); // Desert Treasure
                player.getPackets().sendConfig(622, 632); // Devious Minds
                player.getPackets().sendConfig(131, 132); // The Dig Site
                player.getPackets().sendConfig(80, 10); // Druidic Ritual
                player.getPackets().sendConfig(0, 11); // Dwarf Cannon
                player.getPackets().sendConfig(335, 188); // Eadgar's Ruse
                player.getPackets().sendConfig(934, 97000); // Eagleas' Peak
                player.getPackets().sendConfig(299, 124); // Elemental Workshop I
                player.getPackets().sendConfig(896, 124); // Elemental Workshop II
                player.getPackets().sendConfig(641, 365); // Enakhra's Lament
                player.getPackets().sendConfig(912, 98000); // Enlightened Journey
                player.getPackets().sendConfig(844, 37500); // The Eyes of Glouphrie
                player.getPackets().sendConfig(671, 120); // Fairytale I - Growing Pains
                player.getPackets().sendConfig(810, 120); // Fairytale II - Cure a Queen
                player.getPackets().sendConfig(148, 99); // Family Crest
                player.getPackets().sendConfig(435, 435); // The Feud
                player.getPackets().sendConfig(17, 159); // Fight Arena
                player.getPackets().sendConfig(11, 111); // Fishing Contest
                player.getPackets().sendConfig(521, 99500); // Forgettable Tale...
                player.getPackets().sendConfig(347, 102); // The Fremmenik Trials
                player.getPackets().sendConfig(553, 37500); // Garden of Tranquillity
                player.getPackets().sendConfig(180, 6); // Gertrude's Cat
                player.getPackets().sendConfig(482, 109); // The Giant Dwarf
                player.getPackets().sendConfig(437, 400); // The Golem
                player.getPackets().sendConfig(150, 361); // The Grand Tree
                player.getPackets().sendConfig(382, 36); // Haunted Mine
                player.getPackets().sendConfig(223, 148); // Hazeel Cult
                player.getPackets().sendConfig(188, 97); // Heroes Quest
                player.getPackets().sendConfig(5, 83); // Holy Grail
                player.getPackets().sendConfig(351, 153); // Horror from the Deep
                player.getPackets().sendConfig(445, 8765); // Icthlarin's Little Helper
                player.getPackets().sendConfig(705, 36800); // In Aid of the Myreque
                player.getPackets().sendConfig(387, 244); // In Search of the Myreque
                player.getPackets().sendConfig(175, 75); // Jungle Potion
                player.getPackets().sendConfig(139, 134); // Legends Quest
                player.getPackets().sendConfig(147, 80); // Lost City
                player.getPackets().sendConfig(465, 1); // The Lost Tribe
                player.getPackets().sendConfig(823, 1440); // Lunar Diplomancy
                player.getPackets().sendConfig(604, 350); // Making History
                player.getPackets().sendConfig(14, 183); // Merlin's Crystal
                player.getPackets().sendConfig(30, 98); // Monks Friend
                player.getPackets().sendConfig(365, 32); // Monkey Madness
                player.getPackets().sendConfig(423, 3240); // Mountain Daughter
                player.getPackets().sendConfig(517, 300); // Mourning ends part 1
                player.getPackets().sendConfig(574, 354); // Mourning ends part 2
                player.getPackets().sendConfig(192, 158); // Murder Mystery
                player.getPackets().sendConfig(905, 1470); // My Arm's Big Adventure
                player.getPackets().sendConfig(307, 333); // Nature Spirit
                player.getPackets().sendConfig(112, 111); // Obervatory Quest
                player.getPackets().sendConfig(416, 640); // One Small Favour
                player.getPackets().sendConfig(165, 123); // Plague City
                player.getPackets().sendConfig(302, 84); // Priest in Peril
                player.getPackets().sendConfig(714, 980); // Rag and Bone Man
                player.getPackets().sendConfig(607, 137); // Ratcatchers
                player.getPackets().sendConfig(678, 369); // Recipe for Disaster
                player.getPackets().sendConfig(496, 684); // Recruitment Drive
                player.getPackets().sendConfig(328, 356); // Regicide
                player.getPackets().sendConfig(402, 213); // Roving Elves
                player.getPackets().sendConfig(730, 100); // Royal Trouble
                player.getPackets().sendConfig(600, 450); // Rum Deal
                player.getPackets().sendConfig(76, 78); // Scorpion Catcher
                player.getPackets().sendConfig(159, 166); // Sea Slug
                player.getPackets().sendConfig(339, 888); // Shades of Mor'ton
                player.getPackets().sendConfig(602, 93000); // Shadow of the Storm
                player.getPackets().sendConfig(60, 35); // Sheep Herder
                player.getPackets().sendConfig(116, 178); // Shilo Village
                player.getPackets().sendConfig(874, 500); // The Slug Menace
                player.getPackets().sendConfig(709, 698); // A Soul's Bane
                player.getPackets().sendConfig(616, 614); // Spirits of the Elid
                player.getPackets().sendConfig(723, 311); // Swan Song
                player.getPackets().sendConfig(568, 200); // A Tail of Two Cats
                player.getPackets().sendConfig(449, 350); // Tears of Guthix
                player.getPackets().sendConfig(26, 168); // Temple of Ikov
                player.getPackets().sendConfig(359, 177); // Throne of Miscellania
                player.getPackets().sendConfig(197, 38); // The Tourist Trap
                player.getPackets().sendConfig(111, 9); // Tree Gnome Village
                player.getPackets().sendConfig(200, 5); // Tribal Totem
                player.getPackets().sendConfig(385, 300); // Troll Romance
                player.getPackets().sendConfig(980, 500); // The Great Brain Robbery
                player.getPackets().sendConfig(635, 5360); // The Hand in the Sand
                player.getPackets().sendConfig(317, 99); // Troll Stronghold
                player.getPackets().sendConfig(161, 10); // Underground Pass
                player.getPackets().sendConfig(571, 654); // Wanted!
                player.getPackets().sendConfig(212, 13); // Watchtower
                player.getPackets().sendConfig(65, 10); // Waterfall Quest
                player.getPackets().sendConfig(992, 992); // What Lies Below
                player.getPackets().sendConfig(226, 7); // Witch's House
        }
}