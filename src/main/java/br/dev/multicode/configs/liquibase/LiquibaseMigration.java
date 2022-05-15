package br.dev.multicode.configs.liquibase;

import io.quarkus.runtime.StartupEvent;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LiquibaseMigration {

  private final Logger logger = Logger.getLogger(this.getClass());

  @ConfigProperty(name = "quarkus.datasource.reactive.url")
  String datasourceUrl;

  @ConfigProperty(name = "quarkus.datasource.username")
  String datasourceUsername;

  @ConfigProperty(name = "quarkus.datasource.password")
  String datasourcePassword;

  @ConfigProperty(name = "quarkus.liquibase.change-log")
  String changeLogLocation;

  public void onStart(@Observes StartupEvent event) throws DatabaseException
  {
    final String jdbcDatasourceUrl = datasourceUrl.replace("vertx-reactive", "jdbc");
    final ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
    final DatabaseConnection connection = DatabaseFactory.getInstance()
        .openConnection(jdbcDatasourceUrl, datasourceUsername, datasourcePassword, null, resourceAccessor);

    try (Liquibase liquibase = new Liquibase(changeLogLocation, resourceAccessor, connection)) {
      liquibase.update(new Contexts(), new LabelExpression());
    } catch (LiquibaseException ex) {
      logger.errorf("LIQUIBASE: database migrations has failed. ERROR: ", ex.getMessage());
    }
  }
}
