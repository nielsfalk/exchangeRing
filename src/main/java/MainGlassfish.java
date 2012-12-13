import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.glassfish.embeddable.*;
import org.glassfish.embeddable.archive.ScatteredArchive;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * copied from https://github.com/jsimone/embeddedGlassfishSample
 */
@SuppressWarnings("ALL")
public class MainGlassfish {


	public static final String POOL_NAME = "ringPool";

	public static void main(String[] args) throws GlassFishException, IOException {


        String webappDirLocation = "src/main/webapp/";

        // The port that we should run on can be set into an environment
        // variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        /** Create and start GlassFish which listens at 8080 http port */
        GlassFishProperties gfProps = new GlassFishProperties();
        gfProps.setPort("http-listener", Integer.valueOf(webPort)); // refer
        // JavaDocs
        // for the
        // details
        // of this
        // API.
        gfProps.setProperty("domain-dir", "glassfishDomain");

        GlassFish glassfish = GlassFishRuntime.bootstrap()
                .newGlassFish(gfProps);
        glassfish.start();

		setupDb(args, glassfish.getCommandRunner());

        Deployer deployer = glassfish.getDeployer();

        // Create a scattered web application.
        ScatteredArchive archive = new ScatteredArchive("myApp",
                ScatteredArchive.Type.WAR, new File(webappDirLocation));
        // target/classes directory contains my complied servlets
        archive.addClassPath(new File("target", "classes"));

        deployer.deploy(archive.toURI());
    }

	private static void setupDb(String[] args, CommandRunner runner) {
		setUpPool(args, runner, POOL_NAME);

		log("output of create jdbc: ", runner.run("create-jdbc-resource", "--connectionpoolid", POOL_NAME,
				"app/jdbc/ring").getOutput());

		log("output of set log level: ",
				runner.run("set-log-level",
						"javax.enterprise.system.container.web=INFO:javax.enterprise.system.container.ejb=FINEST")
						.getOutput());
	}

	private static void setUpPool(String[] args, CommandRunner runner, String poolName) {

		for (String dbUrl : args) {
			if(dbUrl.startsWith("jdbc:")) {
				log("db url", dbUrl);

				String escapedDbUrl = replace(dbUrl, ':', "\\:");
				escapedDbUrl = replace(escapedDbUrl, '=', "\\=");

				String properties = "LoginTimeout=0:URL=" + escapedDbUrl;
				log("properties: ", properties);

				log("output of create conn pool: ", runner.run("create-jdbc-connection-pool",
						"--datasourceclassname", "org.h2.jdbcx.JdbcDataSource",
						"--restype", "javax.sql.DataSource",
						"--property", properties,
						poolName).getOutput());
				return;
			}
		}
		String dbUrl = System.getenv("DATABASE_URL");
		if (dbUrl== null) {
			throw new RuntimeException("set DATABASE_URL or use JDCB parameter like" +
					"MainGlassfish jdbc:h2:file:~/h2DatabaseFile;MVCC=TRUE;MODE=PostgreSQL");
		}
		log("db url", dbUrl);
		Matcher matcher = Pattern.compile("postgres://(.*):(.*)@(.*)/(.*)").matcher(dbUrl);
		matcher.find();

		String properties = "user=" + matcher.group(1) +
				":password=" + matcher.group(2) +
				":databasename=" + matcher.group(4) +
				":loglevel=4:servername=" + matcher.group(3);


		log("properties: ", properties);

		log("output of create conn pool: ",
				runner.run("create-jdbc-connection-pool", "--datasourceclassname", "org.postgresql.ds.PGSimpleDataSource", "--restype", "javax.sql.DataSource",
				"--property", properties,
				poolName).getOutput());
	}

	private static void log(final String message, String dbUrl) {
		System.out.println("-------" + message + ": " + dbUrl);
	}

	private static String replace(String dbUrl, char separator, String joiner) {
		Iterable<String> split = Splitter.on(separator).split(dbUrl);
		return Joiner.on(joiner).join(split);
	}
}
