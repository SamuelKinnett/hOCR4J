/* Copyright (c) 2014 Karol Stasiak
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*/

package io.github.samuelkinnett.hocr4j.dom;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An HOCR DOM tag element.
 */
public class HocrTag extends HocrElement {

    private final Map<String, String> attributes;
    private final String tagClass;
    private final List<HocrElement> elements;
    private final String id;
    private final String name;
    private final String title;

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public String getTagClass() {
        return tagClass;
    }

    public List<HocrElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Creates a new tag with the given elements
     * and with name and attributes based on the given contents of the opening tag.
     * @param openingTagString contents of the opening tag
     * @param contents elements in the body of the tag
     */
    public HocrTag(String openingTagString, List<HocrElement> contents) {
        final String x = openingTagString;
        int i = 1;
        while (x.charAt(i) == ' ') i++;
        int nameStart = i;
        while (x.charAt(i) != ' ' && x.charAt(i) != '>' && x.charAt(i) != '/') {
            i++;
        }
        name = x.substring(nameStart, i).toLowerCase(Locale.US);
        while (x.charAt(i) == ' ') i++;
        attributes = new HashMap();
        while (x.charAt(i) != '/' && x.charAt(i) != '>') {
            int attrNameStart = i;
            ing_bad:
            while (true) {
                switch (x.charAt(i)) {
                    case '=':
                    case '/':
                    case ' ':
                    case '>':
                        break ing_bad;
                    default:
                        i++;
                }
            }
            String attrName = x.substring(attrNameStart, i);
            while (x.charAt(i) == ' ') i++;
            String attrValue = attrName;
            if (x.charAt(i) == '=') {
                i++;
                while (x.charAt(i) == ' ') i++;
                int attrValueStart = i;
                switch (x.charAt(i)) {
                    case '\'':
                        attrValueStart++;
                        i++;
                        while (x.charAt(i) != '\'') i++;
                        attrValue = x.substring(attrValueStart, i);
                        i++;
                        break;
                    case '\"':
                        attrValueStart++;
                        i++;
                        while (x.charAt(i) != '\"') i++;
                        attrValue = x.substring(attrValueStart, i);
                        i++;
                        break;
                    default:
                        while (x.charAt(i) != ' ' && x.charAt(i) != '/' && x.charAt(i) != '>') i++;
                        attrValue = x.substring(attrValueStart, i);
                        break;
                }
            }
            while (x.charAt(i) == ' ') i++;
            attributes.put(
                    StringEscapeUtils.unescapeHtml4(attrName),
                    StringEscapeUtils.unescapeHtml4(attrValue));
        }
        this.id = attributes.get("id");
        this.tagClass = attributes.get("class");
        this.title = attributes.get("title");
        this.elements = new ArrayList(contents);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final HocrTag other = (HocrTag) obj;
        return Objects.equals(this.getElements(), other.getElements())
                && Objects.equals(this.getName(), other.getName())
                && Objects.equals(this.getTagClass(), other.getTagClass())
                && Objects.equals(this.getTitle(), other.getTitle())
                && Objects.equals(this.getID(), other.getID())
                && Objects.equals(this.getAttributes(), other.getAttributes());
    }

    @Nullable
    @Override
    public HocrTag findTag(@Nonnull String tagName) {
        if (name.equals(tagName)) {
            return this;
        } else {
            for (HocrElement e : elements) {
                HocrTag found = e.findTag(tagName);
                if (found != null) return found;
            }
        }
        return null;
    }//tested

    @Nonnull
    @Override
    public String getRawText() {
        StringBuilder sb = new StringBuilder();
        for (HocrElement e : elements) {
            sb.append(e.getRawText());
        }
        return sb.toString();
    }//tested

    @Override
    public int hashCode() {
        return Objects.hash(elements, name, tagClass, title, id, attributes);
    }

    @Override
    public boolean isBlank() {
        return false;
    }//tested

    @Nonnull
    public String mkString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(name);
        sb.append(" ");
        sb.append(id);
        sb.append(" ");
        sb.append(tagClass);
        sb.append(">");
        for (HocrElement e : elements) {
            sb.append(e.mkString());
        }
        sb.append("</");
        sb.append(name);
        sb.append(">");
        return sb.toString();
    }

    public String toString() {
        String result = " <" + name + ">";
        if (!attributes.isEmpty()) {
            result += attributes;
        }
        if (!elements.isEmpty()) {
            result += elements;
        }
        return result + " ";

    }
}
