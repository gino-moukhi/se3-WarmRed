package be.kdg.se3.warmred.picker.domain;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter class for converting a Character with a {@link javax.xml.bind.Marshaller}
 * otherwise the {@link LocationInfo storageroom} will be an integer after marshalling instead of a character
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
class CharacterAdapter extends XmlAdapter<String, Character> {
    @Override
    public Character unmarshal(String v) {
        return v.charAt(0);
    }

    @Override
    public String marshal(Character v) {
        return v.toString();
    }
}
