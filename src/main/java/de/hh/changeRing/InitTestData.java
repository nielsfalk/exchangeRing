package de.hh.changeRing;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.hh.changeRing.domain.User;


/**
 * User: nielsfalk Date: 08.11.12 17:23
 */
public class InitTestData {
	private InitialData data;

	public void init() {
		try {
			JAXBContext context = JAXBContext.newInstance(InitialData.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = InitialData.class.getResourceAsStream("/initialData.xml");

			data = (InitialData) unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public List<User> getUsers() {
		return data.users;
	}

	@XmlRootElement(name = "exchangeRingInitial")
	@XmlAccessorType(XmlAccessType.PROPERTY)
	public static class InitialData {
		@XmlElement(name = "user")
		List<User> users;

	}


}
