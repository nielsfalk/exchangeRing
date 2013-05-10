package de.hh.changeRing.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

@Entity
@DiscriminatorValue("system")
@NamedQueries(
        {@NamedQuery(name = "getSystem",query = "select s from SystemAccount s")}
)
public class SystemAccount extends User{
    public static SystemAccount getSystem(EntityManager entityManager) {
        List<SystemAccount> resultList = entityManager.createNamedQuery("getSystem", SystemAccount.class).getResultList();
        if (resultList.size()!= 1) {
            throw new IllegalStateException("this is strange");
        }
        return resultList.get(0);
    }

	@Override
	public boolean isNotSystem() {
		return false;
	}
}
