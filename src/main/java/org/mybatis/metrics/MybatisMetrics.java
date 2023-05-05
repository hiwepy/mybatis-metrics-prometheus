package org.mybatis.metrics;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.prometheus.client.Summary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

/**
 * druid Metrics
 *
 * @author L.cm
 */
public class MybatisMetrics implements MeterBinder {

	private static final String[] labelNames = new String[] { "class", "command", "status" };

	public static final Counter QUERY_COUNT = Counter.builder()
			.name("mybatis_requests_total").help("total sql command.")
			.labelNames(labelNames).register();

	public static final Gauge QUERY_MAX = Gauge.builder()
			.name("mybatis_requests_max")
			.help("run sql command latency in seconds.")
			.labelNames(labelNames).register();

	public static final Summary QUERY_SUMMARY = Summary.build()
			.name("mybatis_latency_seconds").help("Request latency in seconds.").labelNames(labelNames)
			.register();

	/**
	 * Prefix used for all Druid metric names.
	 */
	public static final String DRUID_METRIC_NAME_PREFIX = "druid";
	private static final String METRIC_CATEGORY = "pool";

	/**
	 * DataSource
	 */
	private static final String METRIC_NAME_INITIAL_SIZE = DRUID_METRIC_NAME_PREFIX + ".initial.size";
	private static final String METRIC_NAME_MIN_IDLE = DRUID_METRIC_NAME_PREFIX + ".min.idle";
	private static final String METRIC_NAME_MAX_ACTIVE = DRUID_METRIC_NAME_PREFIX + ".max.active";
	private static final String METRIC_NAME_MAX_WAIT = DRUID_METRIC_NAME_PREFIX + ".max.wait";
	private static final String METRIC_NAME_MAX_WAIT_THREAD_COUNT = DRUID_METRIC_NAME_PREFIX + ".max.wait.thread.count";
	private static final String METRIC_NAME_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE = DRUID_METRIC_NAME_PREFIX + ".max.pool.prepared.statement.per.connection.size";
	private static final String METRIC_NAME_MAX_OPEN_PREPARED_STATEMENTS = DRUID_METRIC_NAME_PREFIX + ".max.open.prepared.statements";
	private static final String METRIC_NAME_LOGIN_TIMEOUT = DRUID_METRIC_NAME_PREFIX + ".login.timeout";
	private static final String METRIC_NAME_QUERY_TIMEOUT = DRUID_METRIC_NAME_PREFIX + ".query.timeout";
	private static final String METRIC_NAME_TRANSACTION_QUERY_TIMEOUT = DRUID_METRIC_NAME_PREFIX + ".transaction.query.timeout";
	private static final String METRIC_NAME_TRANSACTION_THRESHOLD_MILLIS = DRUID_METRIC_NAME_PREFIX + ".transaction.threshold.millis";
	private static final String METRIC_NAME_VALIDATION_QUERY_TIMEOUT = DRUID_METRIC_NAME_PREFIX + ".validation.query.timeout";
	private static final String METRIC_NAME_ACTIVE_COUNT = DRUID_METRIC_NAME_PREFIX + ".active.count";
	private static final String METRIC_NAME_ACTIVE_PEAK = DRUID_METRIC_NAME_PREFIX + ".active.peak";
	private static final String METRIC_NAME_POOLING_COUNT = DRUID_METRIC_NAME_PREFIX + ".pooling.count";
	private static final String METRIC_NAME_POOLING_PEAK = DRUID_METRIC_NAME_PREFIX + ".pooling.peak";
	private static final String METRIC_NAME_WAIT_THREAD_COUNT = DRUID_METRIC_NAME_PREFIX + ".wait.thread.count";
	private static final String METRIC_NAME_NOT_EMPTY_WAIT_COUNT = DRUID_METRIC_NAME_PREFIX + ".not.empty.wait.count";
	private static final String METRIC_NAME_NOT_EMPTY_WAIT_MILLIS = DRUID_METRIC_NAME_PREFIX + ".not.empty.wait.millis";
	private static final String METRIC_NAME_NOT_EMPTY_THREAD_COUNT = DRUID_METRIC_NAME_PREFIX + ".not.empty.thread.count";
	private static final String METRIC_NAME_LOGIC_CONNECT_COUNT = DRUID_METRIC_NAME_PREFIX + ".logic.connect.count";
	private static final String METRIC_NAME_LOGIC_CLOSE_COUNT = DRUID_METRIC_NAME_PREFIX + ".logic.close.count";
	private static final String METRIC_NAME_LOGIC_CONNECT_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".logic.connect.error.count";
	private static final String METRIC_NAME_PHYSICAL_CONNECT_COUNT = DRUID_METRIC_NAME_PREFIX + ".physical.connect.count";
	private static final String METRIC_NAME_PHYSICAL_CLOSE_COUNT = DRUID_METRIC_NAME_PREFIX + ".physical.close.count";
	private static final String METRIC_NAME_PHYSICAL_CONNECT_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".physical.connect.error.count";
	private static final String METRIC_NAME_EXECUTE_COUNT = DRUID_METRIC_NAME_PREFIX + ".execute.count";
	private static final String METRIC_NAME_EXECUTE_QUERY_COUNT = DRUID_METRIC_NAME_PREFIX + ".execute.query.count";
	private static final String METRIC_NAME_EXECUTE_UPDATE_COUNT = DRUID_METRIC_NAME_PREFIX + ".execute.update.count";
	private static final String METRIC_NAME_EXECUTE_BATCH_COUNT = DRUID_METRIC_NAME_PREFIX + ".execute.batch.count";
	private static final String METRIC_NAME_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".error.count";
	private static final String METRIC_NAME_COMMIT_COUNT = DRUID_METRIC_NAME_PREFIX + ".commit.count";
	private static final String METRIC_NAME_ROLLBACK_COUNT = DRUID_METRIC_NAME_PREFIX + ".rollback.count";
	private static final String METRIC_NAME_PSCACHE_ACCESS_COUNT = DRUID_METRIC_NAME_PREFIX + ".ps.cache.access.count";
	private static final String METRIC_NAME_PSCACHE_HIT_COUNT = DRUID_METRIC_NAME_PREFIX + ".ps.cache.hit.count";
	private static final String METRIC_NAME_PSCACHE_MISS_COUNT = DRUID_METRIC_NAME_PREFIX + ".ps.cache.miss.count";
	private static final String METRIC_NAME_PREPARED_STATEMENT_OPEN_COUNT = DRUID_METRIC_NAME_PREFIX + ".prepared.statement.open.count";
	private static final String METRIC_NAME_PREPARED_STATEMENT_CLOSED_COUNT = DRUID_METRIC_NAME_PREFIX + ".prepared.statement.closed.count";
	private static final String METRIC_NAME_RESULTSET_OPEN_COUNT = DRUID_METRIC_NAME_PREFIX + ".resultset.open.count";
	private static final String METRIC_NAME_RESULTSET_OPENING_COUNT = DRUID_METRIC_NAME_PREFIX + ".resultset.opening.count";
	private static final String METRIC_NAME_RESULTSET_OPENING_MAX = DRUID_METRIC_NAME_PREFIX + ".resultset.opening.max";
	private static final String METRIC_NAME_RESULTSET_CLOSE_COUNT = DRUID_METRIC_NAME_PREFIX + ".resultset.close.count";
	private static final String METRIC_NAME_RESULTSET_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".resultset.error.count";
	private static final String METRIC_NAME_RESULTSET_FETCH_ROW_COUNT = DRUID_METRIC_NAME_PREFIX + ".resultset.fetch.row.count";
	private static final String METRIC_NAME_START_TRANSACTION_COUNT = DRUID_METRIC_NAME_PREFIX + ".start.transaction.count";
	private static final String METRIC_NAME_TRANSACTION_COUNT = DRUID_METRIC_NAME_PREFIX + ".transaction.count";
	private static final String METRIC_NAME_CONNECTION_HOLD_TIME_MILLIS = DRUID_METRIC_NAME_PREFIX + ".connection.hold.time.millis";
	private static final String METRIC_NAME_CONNECTION_HOLD_TIME_MILLIS_MIN = DRUID_METRIC_NAME_PREFIX + ".connection.hold.time.millis.min";
	private static final String METRIC_NAME_CONNECTION_HOLD_TIME_MILLIS_MAX = DRUID_METRIC_NAME_PREFIX + ".connection.hold.time.millis.max";
	private static final String METRIC_NAME_REMOVE_ABANDONED_COUNT = DRUID_METRIC_NAME_PREFIX + ".remove.abandoned.count";
	private static final String METRIC_NAME_CLOB_OPEN_COUNT = DRUID_METRIC_NAME_PREFIX + ".clob.open.count";
	private static final String METRIC_NAME_BLOB_OPEN_COUNT = DRUID_METRIC_NAME_PREFIX + ".blob.open.count";

	private static final String METRIC_NAME_CONNECTION_ACTIVE_COUNT = DRUID_METRIC_NAME_PREFIX + ".connection.active.count";
	private static final String METRIC_NAME_CONNECTION_CONNECT_ALIVE_MILLIS = DRUID_METRIC_NAME_PREFIX + ".connection.connect.alive.millis";
	private static final String METRIC_NAME_CONNECTION_CONNECT_ALIVE_MILLIS_MIN = DRUID_METRIC_NAME_PREFIX + ".connection.connect.alive.millis.min";
	private static final String METRIC_NAME_CONNECTION_CONNECT_ALIVE_MILLIS_MAX = DRUID_METRIC_NAME_PREFIX + ".connection.connect.alive.millis.max";
	/**
	 * connections
	 */
	private static final String METRIC_NAME_CONNECTORS_CONNECT_MAX_TIME = DRUID_METRIC_NAME_PREFIX + ".connections.connect.max.time";
	private static final String METRIC_NAME_CONNECTORS_ALIVE_MAX_TIME = DRUID_METRIC_NAME_PREFIX + ".connections.alive.max.time";
	private static final String METRIC_NAME_CONNECTORS_ALIVE_MIN_TIME = DRUID_METRIC_NAME_PREFIX + ".connections.alive.min.time";
	private static final String METRIC_NAME_CONNECTORS_CONNECT_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.connect.count";
	private static final String METRIC_NAME_CONNECTORS_ACTIVE_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.active.count";
	private static final String METRIC_NAME_CONNECTORS_CLOSE_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.close.count";
	private static final String METRIC_NAME_CONNECTORS_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.error.count";
	private static final String METRIC_NAME_CONNECTORS_CONNECT_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.connect.error.count";
	private static final String METRIC_NAME_CONNECTORS_COMMIT_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.commit.count";
	private static final String METRIC_NAME_CONNECTORS_ROLLBACK_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.rollback.count";
	/**
	 * statement
	 */
	private static final String METRIC_NAME_STATEMENT_CREATE_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.create.count";
	private static final String METRIC_NAME_STATEMENT_PREPARE_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.prepare.count";
	private static final String METRIC_NAME_STATEMENT_PREPARE_CALL_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.prepare.call.count";
	private static final String METRIC_NAME_STATEMENT_CLOSE_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.close.count";
	private static final String METRIC_NAME_STATEMENT_RUNNING_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.running.count";
	private static final String METRIC_NAME_STATEMENT_CONCURRENT_MAX = DRUID_METRIC_NAME_PREFIX + ".statement.concurrent.max";
	private static final String METRIC_NAME_STATEMENT_EXECUTE_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.execute.count";
	private static final String METRIC_NAME_STATEMENT_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.error.count";
	private static final String METRIC_NAME_STATEMENT_NANO_TOTAL = DRUID_METRIC_NAME_PREFIX + ".statement.nano.total";
	private static final String METRIC_NAME_STATEMENT_NANO_MAX = DRUID_METRIC_NAME_PREFIX + ".statement.nano.max";
	private static final String METRIC_NAME_STATEMENT_NANO_MIN = DRUID_METRIC_NAME_PREFIX + ".statement.nano.min";
	private static final String METRIC_NAME_STATEMENT_EXECUTE_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.execute.error.count";
	private static final String METRIC_NAME_STATEMENT_EXECUTE_SUCCESS_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.execute.success.count";
	private static final String METRIC_NAME_STATEMENT_EXECUTE_UPDATE_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.execute.update.count";
	private static final String METRIC_NAME_STATEMENT_EXECUTE_QUERY_COUNT = DRUID_METRIC_NAME_PREFIX + ".statement.execute.query.count";
	private static final String METRIC_NAME_STATEMENT_EXECUTE_MILLIS_TOTAL = DRUID_METRIC_NAME_PREFIX + ".statement.execute.millis.total";
	/**
	 * resultSet
	 */
	private static final String METRIC_NAME_RESULTSET_CONNECT_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.connect.error.count";
	private static final String METRIC_NAME_RESULTSET_COMMIT_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.commit.count";
	private static final String METRIC_NAME_RESULTSET_ROLLBACK_COUNT = DRUID_METRIC_NAME_PREFIX + ".connections.rollback.count";
	/**
	 * Sql
	 */
	private static final String METRIC_NAME_SQL_SKIP_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.skip.count";
	private static final String METRIC_NAME_SQL_EXECUTE_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.execute.count";
	private static final String METRIC_NAME_SQL_EXECUTE_SUCCESS_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.execute.success.count";
	private static final String METRIC_NAME_SQL_EXECUTE_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.execute.error.count";
	private static final String METRIC_NAME_SQL_EXECUTE_MILLIS_TOTAL = DRUID_METRIC_NAME_PREFIX + ".sql.execute.millis.total";
	private static final String METRIC_NAME_SQL_EXECUTE_MILLIS_MAX = DRUID_METRIC_NAME_PREFIX + ".sql.execute.millis.max";
	private static final String METRIC_NAME_SQL_EXECUTE_BATCH_SIZE_TOTAL = DRUID_METRIC_NAME_PREFIX + ".sql.execute.batch.size.total";
	private static final String METRIC_NAME_SQL_EXECUTE_BATCH_SIZE_MAX = DRUID_METRIC_NAME_PREFIX + ".sql.execute.batch.size.max";
	private static final String METRIC_NAME_SQL_IN_TRANSACTION_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.in.transaction.count";
	private static final String METRIC_NAME_SQL_CONCURRENT_MAX = DRUID_METRIC_NAME_PREFIX + ".sql.concurrent.max";
	private static final String METRIC_NAME_SQL_ERROR_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.error.count";

	private static final String METRIC_NAME_SQL_SELECT_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.select.count";
	private static final String METRIC_NAME_SQL_UPDATE_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.update.count";
	private static final String METRIC_NAME_SQL_INSERT_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.insert.count";
	private static final String METRIC_NAME_SQL_DELETE_COUNT = DRUID_METRIC_NAME_PREFIX + ".sql.delete.count";

	private final Map<String, DruidDataSource> druidDataSourceMap;

	public MybatisMetrics(Map<String, DruidDataSource> druidDataSourceMap) {
		this.druidDataSourceMap = druidDataSourceMap;
	}

	@Override
	public void bindTo(MeterRegistry meterRegistry) {
		druidDataSourceMap.forEach((name, dataSource) -> {

			List<Tag> tags = new ArrayList<>(2);
			tags.add(Tag.of(METRIC_CATEGORY, name));

			bindDataSourceMetrics(meterRegistry, dataSource, tags);
			bindDataSourceMetrics(meterRegistry, dataSource, tags);

			JdbcDataSourceStat dsStats = dataSource.getDataSourceStat();

			bindConnectionMetrics(meterRegistry, dsStats.getConnectionStat(), tags);

			bindStatementMetrics(meterRegistry, dsStats.getStatementStat(), tags);

			bindResultSetMetrics(meterRegistry, dsStats.getResultSetStat(), tags);

			bindSqlMetrics(meterRegistry, dsStats , tags);


		});
	}

	/**
	 * bind druid datasource metrics
	 * @param meterRegistry meter registry
	 * @param dataSource druid datasource
	 * @param tags tags
	 */
	private void bindDataSourceMetrics(MeterRegistry meterRegistry, DruidDataSource dataSource, List<Tag> tags) {

		// basic configurations
		bindGauge(meterRegistry, METRIC_NAME_INITIAL_SIZE, "Initial size", dataSource, DruidDataSource::getInitialSize, tags);
		bindGauge(meterRegistry, METRIC_NAME_MIN_IDLE, "Min idle", dataSource, DruidDataSource::getMinIdle, tags);
		bindGauge(meterRegistry, METRIC_NAME_MAX_ACTIVE, "Max active", dataSource, DruidDataSource::getMaxActive, tags);
		bindGauge(meterRegistry, METRIC_NAME_MAX_WAIT, "Max wait", dataSource, DruidDataSource::getMaxWait, tags);

		// connection pool core metrics
		bindGauge(meterRegistry, METRIC_NAME_ACTIVE_COUNT, "Active count", dataSource, DruidDataSource::getActiveCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_ACTIVE_PEAK, "Active peak", dataSource, DruidDataSource::getActivePeak, tags);
		bindGauge(meterRegistry, METRIC_NAME_POOLING_COUNT, "Pooling count", dataSource, DruidDataSource::getPoolingCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_POOLING_PEAK, "Pooling peak", dataSource, DruidDataSource::getPoolingPeak, tags);
		bindGauge(meterRegistry, METRIC_NAME_WAIT_THREAD_COUNT, "Wait thread count", dataSource, DruidDataSource::getWaitThreadCount, tags);

		// connection pool detail metrics
		bindGauge(meterRegistry, METRIC_NAME_NOT_EMPTY_WAIT_COUNT, "Not empty wait count", dataSource, DruidDataSource::getNotEmptyWaitCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_NOT_EMPTY_WAIT_MILLIS, "Not empty wait millis", dataSource, DruidDataSource::getNotEmptyWaitMillis, tags);
		bindGauge(meterRegistry, METRIC_NAME_NOT_EMPTY_THREAD_COUNT, "Not empty thread count", dataSource, DruidDataSource::getNotEmptyWaitThreadCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_LOGIC_CONNECT_COUNT, "Logic connect count", dataSource, DruidDataSource::getConnectCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_LOGIC_CLOSE_COUNT, "Logic close count", dataSource, DruidDataSource::getCloseCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_LOGIC_CONNECT_ERROR_COUNT, "Logic connect error count", dataSource, DruidDataSource::getConnectErrorCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_PHYSICAL_CONNECT_COUNT, "Physical connect count", dataSource, DruidDataSource::getCreateCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_PHYSICAL_CLOSE_COUNT, "Physical close count", dataSource, DruidDataSource::getDestroyCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_PHYSICAL_CONNECT_ERROR_COUNT, "Physical connect error count", dataSource, DruidDataSource::getCreateErrorCount, tags);

		// sql execution core metrics
		bindGauge(meterRegistry, METRIC_NAME_EXECUTE_COUNT, "Execute count", dataSource, DruidDataSource::getExecuteCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_ERROR_COUNT, "Error count", dataSource, DruidDataSource::getErrorCount, tags);

		// transaction metrics
		bindGauge(meterRegistry, METRIC_NAME_START_TRANSACTION_COUNT, "Start transaction count", dataSource, DruidDataSource::getStartTransactionCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_COMMIT_COUNT, "Commit count", dataSource, DruidDataSource::getCommitCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_ROLLBACK_COUNT, "Rollback count", dataSource, DruidDataSource::getRollbackCount, tags);

		// sql execution detail
		bindGauge(meterRegistry, METRIC_NAME_PREPARED_STATEMENT_OPEN_COUNT, "Prepared statement open count", dataSource, DruidDataSource::getPreparedStatementCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_PREPARED_STATEMENT_CLOSED_COUNT, "Prepared statement closed count", dataSource, DruidDataSource::getClosedPreparedStatementCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_PSCACHE_ACCESS_COUNT, "PS cache access count", dataSource, DruidDataSource::getCachedPreparedStatementAccessCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_PSCACHE_HIT_COUNT, "PS cache hit count", dataSource, DruidDataSource::getCachedPreparedStatementHitCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_PSCACHE_MISS_COUNT, "PS cache miss count", dataSource, DruidDataSource::getCachedPreparedStatementMissCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_EXECUTE_QUERY_COUNT, "Execute query count", dataSource, DruidDataSource::getExecuteQueryCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_EXECUTE_UPDATE_COUNT, "Execute update count", dataSource, DruidDataSource::getExecuteUpdateCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_EXECUTE_BATCH_COUNT, "Execute batch count", dataSource, DruidDataSource::getExecuteBatchCount, tags);

		// none core metrics, some are static configurations
		bindGauge(meterRegistry, METRIC_NAME_MAX_WAIT, "Max wait", dataSource, DruidDataSource::getMaxWait, tags);
		bindGauge(meterRegistry, METRIC_NAME_MAX_WAIT_THREAD_COUNT, "Max wait thread count", dataSource, DruidDataSource::getMaxWaitThreadCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_LOGIN_TIMEOUT, "Login timeout", dataSource, DruidDataSource::getLoginTimeout, tags);
		bindGauge(meterRegistry, METRIC_NAME_QUERY_TIMEOUT, "Query timeout", dataSource, DruidDataSource::getQueryTimeout, tags);
		bindGauge(meterRegistry, METRIC_NAME_TRANSACTION_QUERY_TIMEOUT, "Transaction query timeout", dataSource, DruidDataSource::getTransactionQueryTimeout, tags);
		bindGauge(meterRegistry, METRIC_NAME_TRANSACTION_THRESHOLD_MILLIS, "Transaction threshold millis", dataSource, DruidDataSource::getTransactionThresholdMillis, tags);
		bindGauge(meterRegistry, METRIC_NAME_VALIDATION_QUERY_TIMEOUT, "Validation query timeout", dataSource, DruidDataSource::getValidationQueryTimeout, tags);
		bindGauge(meterRegistry, METRIC_NAME_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE, "Max pool prepared statement per connection size", dataSource, DruidDataSource::getMaxPoolPreparedStatementPerConnectionSize, tags);
		bindGauge(meterRegistry, METRIC_NAME_MAX_OPEN_PREPARED_STATEMENTS, "Max open prepared statements", dataSource, DruidDataSource::getMaxOpenPreparedStatements, tags);

	}


	/**
	 * Bind a {@link TimeGauge} to the given {@link JdbcDataSourceStat}.
	 * @param meterRegistry the meter registry
	 * @param dsStats the data source stats
	 * @param tags the tags to use
	 */
	private void bindDataSourceMetrics(MeterRegistry meterRegistry, JdbcDataSourceStat dsStats, List<Tag> tags) {

		bindGauge(meterRegistry, METRIC_NAME_CONNECTION_ACTIVE_COUNT, "Connection Active Count", dsStats, JdbcDataSourceStat::getConnectionActiveCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTION_CONNECT_ALIVE_MILLIS, "Connection Connect Alive Millis", dsStats, JdbcDataSourceStat::getConnectionConnectAliveMillis, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTION_CONNECT_ALIVE_MILLIS_MIN, "Connection Connect Alive Millis Min", dsStats, JdbcDataSourceStat::getConnectionConnectAliveMillisMin, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTION_CONNECT_ALIVE_MILLIS_MAX, "Connection Connect Alive Millis Max", dsStats, JdbcDataSourceStat::getConnectionConnectAliveMillisMax, tags);

	}

	/**
	 * Bind a {@link TimeGauge} to the given {@link JdbcDataSourceStat}.
	 * @param meterRegistry the meter registry
	 * @param connectionStat the connection stat
	 * @param tags the tags to apply to the gauge
	 */
	private void bindConnectionMetrics(MeterRegistry meterRegistry, JdbcConnectionStat connectionStat, List<Tag> tags) {

		bindGauge(meterRegistry, METRIC_NAME_CONNECTION_CONNECT_ALIVE_MILLIS_MAX, "Connection connect max time", connectionStat, JdbcConnectionStat::getConnectMillisMax, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_ALIVE_MAX_TIME, "Connection alive max time", connectionStat, JdbcConnectionStat::getAliveMillisMax, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_ALIVE_MIN_TIME, "Connection alive min time", connectionStat, JdbcConnectionStat::getAliveMillisMin, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_ACTIVE_COUNT, "Connection active count", connectionStat, JdbcConnectionStat::getActiveCount, tags);
  		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_CONNECT_COUNT, "Connection connect count", connectionStat, JdbcConnectionStat::getConnectCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_CLOSE_COUNT, "Connection close count", connectionStat, JdbcConnectionStat::getCloseCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_ERROR_COUNT, "Connection error count", connectionStat, JdbcConnectionStat::getErrorCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_CONNECT_ERROR_COUNT, "Connection connect error count", connectionStat, JdbcConnectionStat::getConnectErrorCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_COMMIT_COUNT, "Connection commit count", connectionStat, JdbcConnectionStat::getCommitCount, tags);
		bindGauge(meterRegistry, METRIC_NAME_CONNECTORS_ROLLBACK_COUNT, "Connection rollback count", connectionStat, JdbcConnectionStat::getRollbackCount, tags);

	}

	private void bindStatementMetrics(MeterRegistry registry, JdbcStatementStat statementStat, List<Tag> tags) {

		bindGauge(registry, METRIC_NAME_STATEMENT_CREATE_COUNT, "Jdbc Statement Create count", statementStat, JdbcStatementStat::getCreateCount, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_RUNNING_COUNT, "Jdbc Statement Running count", statementStat, JdbcStatementStat::getRunningCount, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_ERROR_COUNT, "Jdbc Statement Execute error count", statementStat, JdbcStatementStat::getErrorCount, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_CONCURRENT_MAX, "Jdbc Statement Concurrent max", statementStat, JdbcStatementStat::getConcurrentMax, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_CLOSE_COUNT, "Jdbc Statement Close count", statementStat, JdbcStatementStat::getCloseCount, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_NANO_TOTAL, "Jdbc Statement Nano count", statementStat, JdbcStatementStat::getNanoTotal, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_EXECUTE_COUNT, "Jdbc Statement Execute count", statementStat, JdbcStatementStat::getExecuteCount, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_EXECUTE_SUCCESS_COUNT, "Jdbc Statement Execute success count", statementStat, JdbcStatementStat::getExecuteSuccessCount, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_EXECUTE_MILLIS_TOTAL, "Jdbc Statement Execute millis total", statementStat, JdbcStatementStat::getExecuteMillisTotal, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_PREPARE_COUNT, "Jdbc Statement Prepare count", statementStat, JdbcStatementStat::getPrepareCount, tags);
		bindGauge(registry, METRIC_NAME_STATEMENT_PREPARE_CALL_COUNT, "Jdbc Statement Prepare call count", statementStat, JdbcStatementStat::getPrepareCallCount, tags);
	}

	private void bindResultSetMetrics(MeterRegistry registry, JdbcResultSetStat resultSetStat, List<Tag> tags) {

		bindGauge(registry, METRIC_NAME_RESULTSET_OPENING_COUNT, "Jdbc ResultSet Opening count", resultSetStat, JdbcResultSetStat::getOpeningCount, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_OPENING_MAX, "Jdbc ResultSet Opening max", resultSetStat, JdbcResultSetStat::getOpeningMax, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_OPEN_COUNT, "Jdbc ResultSet Open count", resultSetStat, JdbcResultSetStat::getOpenCount, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_CLOSE_COUNT, "Jdbc ResultSet Close count", resultSetStat, JdbcResultSetStat::getCloseCount, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_ERROR_COUNT, "Jdbc ResultSet Error count", resultSetStat, JdbcResultSetStat::getErrorCount, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_FETCH_ROW_COUNT, "Jdbc ResultSet Fetch row count", resultSetStat, JdbcResultSetStat::getFetchRowCount, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_FETCH_ROW_COUNT, "Jdbc ResultSet Alive Nano Total", resultSetStat, JdbcResultSetStat::getAliveNanoTotal, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_FETCH_ROW_COUNT, "Jdbc ResultSet Alive Millis Total", resultSetStat, JdbcResultSetStat::getAliveMillisTotal, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_ERROR_COUNT, "Jdbc ResultSet Alive Milis Max", resultSetStat, JdbcResultSetStat::getAliveMilisMax, tags);
		bindGauge(registry, METRIC_NAME_RESULTSET_FETCH_ROW_COUNT, "Jdbc ResultSet Alive Milis Min", resultSetStat, JdbcResultSetStat::getAliveMilisMin, tags);

	}

	private void bindSqlMetrics(MeterRegistry registry, JdbcDataSourceStat dsStats, List<Tag> tags) {

		bindGauge(registry, METRIC_NAME_SQL_SKIP_COUNT, "Skip Sql Count", dsStats, JdbcDataSourceStat::getSkipSqlCount, tags);
		dsStats.getSqlStatMap().forEach((sql, sqlStat) -> {
			List<Tag> sqlTags = new ArrayList<>(tags);
			sqlTags.add(Tag.of("sql", sql));
			bindGauge(registry, METRIC_NAME_SQL_EXECUTE_COUNT, "Jdbc Sql Execute count", sqlStat, JdbcSqlStat::getExecuteCount, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_EXECUTE_SUCCESS_COUNT, "Jdbc Sql Execute success count", sqlStat, JdbcSqlStat::getExecuteSuccessCount, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_EXECUTE_ERROR_COUNT, "Jdbc Sql Execute error count", sqlStat, JdbcSqlStat::getErrorCount, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_EXECUTE_MILLIS_TOTAL, "Jdbc Sql Execute millis total", sqlStat, JdbcSqlStat::getExecuteMillisTotal, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_EXECUTE_MILLIS_MAX, "Jdbc Sql Execute millis max", sqlStat, JdbcSqlStat::getExecuteMillisMax, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_EXECUTE_BATCH_SIZE_TOTAL, "Jdbc Sql Execute Batch Size Total", sqlStat, JdbcSqlStat::getExecuteBatchSizeTotal, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_EXECUTE_BATCH_SIZE_MAX, "Jdbc Sql Execute Batch Size Max", sqlStat, JdbcSqlStat::getExecuteBatchSizeMax, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_IN_TRANSACTION_COUNT, "Jdbc Sql In transaction count", sqlStat, JdbcSqlStat::getInTransactionCount, sqlTags);
			bindGauge(registry, METRIC_NAME_SQL_CONCURRENT_MAX, "Jdbc Sql Concurrent max", sqlStat, JdbcSqlStat::getConcurrentMax, sqlTags);
		});

	}

	private <T> void bindGauge(MeterRegistry registry, String metric, String help, T measureObj, ToDoubleFunction<T> measure, Iterable<Tag> tags) {
		Gauge.builder(metric, measureObj, measure)
				.description(help)
				.tags(tags)
				.register(registry);
	}

	private <T> void bindTimeGauge(MeterRegistry registry, String metric, String help, T metricResult,
								   ToDoubleFunction<T> measure, Iterable<Tag> tags) {
		TimeGauge.builder(metric, metricResult, TimeUnit.SECONDS, measure)
				.description(help)
				.tags(tags)
				.register(registry);
	}

	private <T> void bindTimer(MeterRegistry registry, String metric, String help, T measureObj,
							   ToLongFunction<T> countFunc, ToDoubleFunction<T> measure, Iterable<Tag> tags) {
		FunctionTimer.builder(metric, measureObj, countFunc, measure, TimeUnit.SECONDS)
				.description(help)
				.tags(tags)
				.register(registry);
	}


	private <T> void bindCounter(MeterRegistry registry, String name, String help, T measureObj,
								 ToDoubleFunction<T> measure, Iterable<Tag> tags) {
		FunctionCounter.builder(name, measureObj, measure)
				.description(help)
				.tags(tags)
				.register(registry);
	}

}
