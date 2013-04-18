package de.hh.changeRing.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("member")
@NamedQueries({
        @NamedQuery(name = "allMembers", query = "select m from Member m where m.noFee = false")})
public class Member extends User {
    @Override
    public boolean isFeeApplicable() {
        return !noFee;
    }

    // TODO mhoennig: Boolean mapping to 0/X
    private Boolean noFee = Boolean.FALSE;


    public void setNoFee(Boolean fee) {
        this.noFee = fee;
    }
}
