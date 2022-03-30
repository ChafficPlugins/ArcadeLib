package de.chafficplugins.arcadelib.localization;

import io.github.chafficui.CrucialAPI.Utils.localization.Localized;
import io.github.chafficui.CrucialAPI.Utils.localization.Localizer;

public class Localization extends Localized {

    public static String getLocalizedString(Key key, String... values) {
        return Localizer.getLocalizedString("arcadelib_" + key.name(), values);
    }

    public Localization(String identifier) {
        super(identifier);
    }

    /**
     * Returns the localized string for the given key.
     * @param key The key of the string.
     * @return The localized string.
     */
    public String getLocalizedString(String key) {
        switch (Key.valueOf(key)) {
            case COMMAND_NOT_A_PLAYER_ERROR -> {
                return "&cDieser Befehl kann nur von Spielern ausgeführt werden!";
            }
            case PARTY_CURRENT_MEMBERS -> {
                return "&7Aktuelle Mitglieder:";
            }
            case PARTY_NOT_IN_A_PARTY -> {
                return "&7Du bist in keiner Party!";
            }
            case PLAYER_NOT_FOUND -> {
                return "&cSpieler nicht gefunden!";
            }
            case PARTY_NOT_THE_LEADER -> {
                return "&cDu bist nicht der Party-Leader!";
            }
            case PARTY_PLAYER_IS_ALREADY_IN_A_PARTY -> {
                return "&cDer Spieler ist bereits in einer Party!";
            }
            case PARTY_INVITATION_SENT -> {
                return "&7Du hast eine Party-Einladung an &6{0} &7gesendet!";
            }
            case PARTY_INVITATION_RECEIVED -> {
                return "&7Du hast von &6{0} &7eine Party-Einladung erhalten!";
            }
            case PARTY_CREATED -> {
                return "&7Du hast eine Party erstellt!";
            }
            case PARTY_INVITATION_NOT_FOUND -> {
                return "&cDie Party-Einladung von &6{0} konnte nicht gefunden werden!";
            }
            case PARTY_INVITATION_ACCEPTED_RECEIVER -> {
                return "&7Du hast die Party-Einladung von &6{0} &7angenommen!";
            }
            case PARTY_INVITATION_DECLINED_RECEIVER -> {
                return "&7Du hast die Party-Einladung von &6{0} &7abgelehnt!";
            }
            case PARTY_INVITATION_DECLINED_SENDER -> {
                return "&7Deine Party-Einladung an &6{0} &7 wurde abgelehnt!";
            }
            default -> {
                return "&cUnknown localization key!";
            }
        }
    }


    public enum Key {
        COMMAND_NOT_A_PLAYER_ERROR, //message send if command is only for players
        PARTY_CURRENT_MEMBERS, //current players:
        PARTY_NOT_IN_A_PARTY, //you are not in a party
        PLAYER_NOT_FOUND, //player not found
        PARTY_NOT_THE_LEADER, //you are not the leader
        PARTY_PLAYER_IS_ALREADY_IN_A_PARTY, //player is already in a party
        PARTY_INVITATION_SENT, //invitation sent to {0}
        PARTY_INVITATION_RECEIVED, //invitation received from {0}
        PARTY_INVITATION_NOT_FOUND, //invitation not found from {0}
        PARTY_INVITATION_ACCEPTED_RECEIVER, //invitation accepted from {0}
        PARTY_INVITATION_DECLINED_RECEIVER, //invitation declined from {0}
        PARTY_INVITATION_DECLINED_SENDER, //invitation declined to {0}
        PARTY_CREATED,
    }
}
