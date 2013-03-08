package de.hh.changeRing;

import de.hh.changeRing.transaction.Transaction;
import de.hh.changeRing.user.User;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be
 * excluded.
 * Environmental damage caused by the use must be kept as small as possible.
 */
public class InitTestDataTest {


    protected static final List<User> users = InitTestData.getUsers();
    static final List<Transaction> transactions = InitTestData.getTransactions();
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy.MM.dd");

    @Test
    public void advertisements() {
        assertThat(InitTestData.getAdvertisements().size() > 100, is(true));
    }
}
