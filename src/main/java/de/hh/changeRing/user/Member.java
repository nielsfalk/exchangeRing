package de.hh.changeRing.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("member")
@NamedQueries({
        @NamedQuery(name = "allFeeMembers", query = "select m from Member m where m.noFee = false order by m.id"),
		@NamedQuery(name = "newestMembers", query = "select m from Member m order by m.activated desc"),
		@NamedQuery(name = "findOthers", query = "select m from Member m where m <> :me order by m.id"),
		@NamedQuery(name = "allMembers", query = "select m from Member m order by m.id")}
)
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
