package de.hh.changeRing;

import de.hh.changeRing.domain.Transaction;
import de.hh.changeRing.domain.User;
import org.junit.BeforeClass;

import java.util.List;


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
public class SuperTest {
    private static InitTestData initTestData;
    static List<Transaction> transactions;
    static List<User> users;

    @BeforeClass
    public static void parse() {
        //initTestData = new InitTestData();
        //InitTestData.init();
        users = InitTestData.getUsers();
        transactions = InitTestData.getTransactions();
    }
}
