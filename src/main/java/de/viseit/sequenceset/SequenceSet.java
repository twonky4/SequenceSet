package de.viseit.sequenceset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class SequenceSet {
    private List<SequencePart> parts = new ArrayList<SequencePart>();
    private final String splitSign;
    private final String rangeSign;
    private final String wildcardSign;

    public static class SequenceSetBuilder {
        private String splitSign = ",";
        private String rangeSign = ":";
        private String wildcardSign = "*";

        private SequenceSetBuilder() {
        }

        public SequenceSetBuilder splitAs(String splitSign) {
            this.splitSign = splitSign;

            return this;
        }

        public SequenceSetBuilder rangeAs(String rangeSign) {
            this.rangeSign = rangeSign;

            return this;

        }

        public SequenceSetBuilder wildcardAs(String wildcardSign) {
            this.wildcardSign = wildcardSign;

            return this;
        }

        private void checkSign(String splitSign, String rangeSign, String wildcardSign) {
            if (splitSign.equals(rangeSign)) {
                throw new SequenceSetInitException("split sign can not be equals with range sign");
            }

            if (wildcardSign.equals(rangeSign)) {
                throw new SequenceSetInitException(
                        "range sign can not be equals with wildcard sign");
            }

            if (splitSign.equals(wildcardSign)) {
                throw new SequenceSetInitException(
                        "split sign can not be equals with wildcard sign");
            }
        }

        private void checkSize(String sign, String name) {
            if (sign == null || sign.length() == 0) {
                throw new SequenceSetInitException(name + " sign can not be empty");
            }

            for (char c : sign.toCharArray()) {
                if (Character.isDigit(c)) {
                    throw new SequenceSetInitException(name + " sign can not be a number");
                }
            }
        }

        public SequenceSet build() {
            checkSize(splitSign, "split");
            checkSize(rangeSign, "range");
            checkSize(wildcardSign, "wildcard");
            checkSign(splitSign, rangeSign, wildcardSign);

            return new SequenceSet(splitSign, rangeSign, wildcardSign);
        }
    }

    public static SequenceSetBuilder factory() {
        return new SequenceSetBuilder();
    }

    public static SequenceSet defaults() {
        return new SequenceSetBuilder().build();
    }

    private SequenceSet(String splitSign, String rangeSign, String wildcardSign) {
        this.splitSign = splitSign;
        this.rangeSign = rangeSign;
        this.wildcardSign = wildcardSign;
    }

    private void addParts(String idStr) {
        if (idStr == null || idStr.length() == 0) {
            return;
        }
        checkString(idStr);

        String[] prepList;
        if (idStr.contains(splitSign)) {
            prepList = idStr.split(splitSign);
        } else {
            prepList = new String[] { idStr };
        }

        for (String idPart : prepList) {
            if (idPart.contains(rangeSign)) {
                String[] range = idPart.split(rangeSign);
                String fromId = range[0];
                String toId = range[1];
                parts.add(new SequencePart(fromId, toId));
            } else {
                parts.add(new SequencePart(idPart));
            }
        }

        cleanUp();
    }

    private void checkString(String idStr) {
        for (int pos = 0; pos < idStr.toCharArray().length;) {
            char c = idStr.toCharArray()[pos];
            if (!Character.isDigit(c)) {
                if (splitSign.equals(idStr.substring(pos, pos + splitSign.length()))) {
                    pos += splitSign.length();
                } else if (rangeSign.equals(idStr.substring(pos, pos + rangeSign.length()))) {
                    pos += rangeSign.length();
                } else if (wildcardSign.equals(idStr.substring(pos, pos + wildcardSign.length()))) {
                    pos += wildcardSign.length();
                } else {
                    throw new SequenceSetException(pos, c);
                }
            } else {
                pos++;
            }
        }
    }

    private void cleanUp() {
        // simplify
        boolean simplified;
        do {
            simplified = false;

            Iterator<SequencePart> partIterator = parts.iterator();
            while (partIterator.hasNext()) {
                SequencePart part = partIterator.next();
                for (SequencePart checkPart : parts) {
                    if (part.equals(checkPart)) {
                        continue;
                    }

                    if (!checkPart.isRangeSplit(part)) {
                        checkPart.merge(part);
                        partIterator.remove();
                        simplified = true;
                        break;
                    }
                }
            }
        } while (simplified);

        // sort
        List<SequencePart> newList = new ArrayList<SequencePart>();
        SequencePart smallest = null;
        do {
            smallest = null;
            for (SequencePart part : parts) {
                if (smallest == null) {
                    smallest = part;
                } else {
                    if (!wildcardSign.equals(part.getFromId())
                            && !wildcardSign.equals(smallest.getFromId())
                            && part.getFromIdNum() < smallest.getFromIdNum()) {
                        smallest = part;
                    } else if (wildcardSign.equals(part.getFromId())
                            && !wildcardSign.equals(smallest.getFromId())) {
                        smallest = part;
                    }
                }
            }
            if (smallest != null) {
                newList.add(smallest);
                parts.remove(smallest);
            }
        } while (parts.size() > 0 && smallest != null);
        parts = newList;
    }

    public boolean inList(Long searchId) {
        if (searchId == null) {
            return false;
        }

        for (SequencePart part : parts) {
            if (wildcardSign.equals(part.getFromId()) && wildcardSign.equals(part.getToId())) {
                return true;
            }

            if (wildcardSign.equals(part.getFromId()) && part.getToIdNum() != null
                    && searchId <= part.getToIdNum()) {
                return true;
            }

            if (wildcardSign.equals(part.getToId()) && part.getFromIdNum() != null
                    && searchId >= part.getFromIdNum()) {
                return true;
            }

            if (part.getFromIdNum() != null && searchId >= part.getFromIdNum()
                    && part.getToIdNum() != null && searchId <= part.getToIdNum()) {
                return true;
            }
        }

        return false;
    }

    private class SequencePart {
        String fromId;
        String toId;

        public SequencePart(String fromId, String toId) {
            this.fromId = fromId.trim();
            this.toId = toId.trim();
        }

        public SequencePart(String id) {
            this.fromId = id.trim();
            this.toId = id.trim();
        }

        public String getFromId() {
            return fromId;
        }

        public Long getFromIdNum() {
            Long parseLong = null;
            try {
                parseLong = Long.parseLong(fromId);
            } catch (NumberFormatException e) {
            }
            return parseLong;
        }

        public String getToId() {
            return toId;
        }

        public Long getToIdNum() {
            Long parseLong = null;
            try {
                parseLong = Long.parseLong(toId);
            } catch (NumberFormatException e) {
            }
            return parseLong;
        }

        public boolean isFromWildCard() {
            if (wildcardSign.equals(fromId)) {
                return true;
            }
            return false;
        }

        public boolean isToWildCard() {
            if (wildcardSign.equals(toId)) {
                return true;
            }
            return false;
        }

        public boolean isWildCard() {
            return isFromWildCard() || isToWildCard();
        }

        public boolean isRangeSplit(SequencePart part) {
            if (!isToWildCard() && !part.isFromWildCard()
                    && getToIdNum() + 1 < part.getFromIdNum()) {
                return true;
            } else if (!isFromWildCard() && !part.isToWildCard()
                    && getFromIdNum() > part.getToIdNum() + 1) {
                return true;
            }
            return false;
        }

        public void merge(SequencePart part) {
            // smallest
            if (isFromWildCard() || part.isFromWildCard()) {
                fromId = wildcardSign;
            } else if (getFromIdNum() > part.getFromIdNum()) {
                fromId = part.getFromId();
            }
            // biggest
            if (isToWildCard() || part.isToWildCard()) {
                toId = wildcardSign;
            } else if (getToIdNum() < part.getToIdNum()) {
                toId = part.getToId();
            }
        }
    }

    public SequenceSet add(Collection<?> c) {
        for (Object o : c) {
            if (o == null) {
                // ignore
            } else if (o instanceof String) {
                add((String) o);
            } else if (o instanceof Long) {
                add((Long) o);
            } else if (o instanceof Integer) {
                add((Integer) o);
            } else {
                throw new SequenceSetException(0, o.toString().charAt(0));
            }
        }

        return this;
    }

    public SequenceSet add(Long l) {
        if (l != null) {
            add(l.toString());
        }

        return this;
    }

    public SequenceSet add(Integer l) {
        if (l != null) {
            add(l.toString());
        }

        return this;
    }

    public SequenceSet add(String idStr) {
        addParts(idStr);

        return this;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        for (SequencePart part : parts) {
            if (ret.length() > 0) {
                ret.append(splitSign);
            }

            if (part.fromId.equals(part.toId)) {
                ret.append(part.fromId);
            } else if (!part.isWildCard() && (part.getFromIdNum() + 1) == part.getToIdNum()) {
                ret.append(part.fromId);
                ret.append(splitSign);
                ret.append(part.toId);
            } else {
                ret.append(part.fromId);
                ret.append(rangeSign);
                ret.append(part.toId);
            }
        }

        return ret.toString();
    }

    public void clear() {
        parts.clear();
    }
}
