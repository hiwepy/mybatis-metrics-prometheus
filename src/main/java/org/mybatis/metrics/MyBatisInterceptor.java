package org.mybatis.metrics;

import io.prometheus.client.SimpleTimer;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@SuppressWarnings({"rawtypes"})
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class MyBatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        if (args != null && args.length > 0) {
            final MappedStatement mappedStatement = (MappedStatement) args[0];
            if (mappedStatement != null) {
                final String className = mappedStatement.getId();
                final String command = mappedStatement.getSqlCommandType().name();
                String status = "";
                //以类名和方法名为标签
                String[] labelValues = new String[3];
                labelValues[0] = className;
                labelValues[1] = command;
                labelValues[2] = status;
                SimpleTimer startTimer = new SimpleTimer();
                try {
                    status = MybatisMetricsStatusEnum.success.getCode();
                    return invocation.proceed();
                } catch (Throwable throwable) {
                    status = MybatisMetricsStatusEnum.fail.getCode();
                    throw throwable;
                } finally {
                    labelValues[2] = status;
                    MybatisMetrics.QUERY_MAX.labels(labelValues).set(startTimer.elapsedSeconds());
                    MybatisMetrics.QUERY_SUMMARY.labels(labelValues).observe(startTimer.elapsedSeconds());
                    MybatisMetrics.QUERY_COUNT.labels(labelValues).inc();
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}