package de.hh.changeRing.user;

/**
* Created with IntelliJ IDEA.
* User: niles
* Date: 20.12.12
* Time: 21:33
* To change this template use File | Settings | File Templates.
*/
public enum DepotItemType {
    out("ausgegeben"), in("eingenommen");
    private final String string;

    DepotItemType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
