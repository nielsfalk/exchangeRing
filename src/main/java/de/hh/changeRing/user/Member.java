package de.hh.changeRing.user;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("member")
@NamedQueries({
        @NamedQuery(name = "membersWithFee", query = "select m from Member m where m.fee = :fee")})
public class Member extends User {
    @Override
    public boolean isFeeApplicable() {
        return fee;
    }

    // TODO mhoennig: Boolean mapping to 0/X
    private Boolean fee = true;

    public void setFee(boolean fee) {
        this.fee = fee;
    }
}
