package org.mybatis.metrics;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Intercepts(
    value = {
            @Signature(type= Executor.class,
                    method="update",
                    args={MappedStatement.class,Object.class}),
            @Signature(type=Executor.class,
                    method="query",
                    args={MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class,
                            CacheKey.class, BoundSql.class}),
            @Signature(type=Executor.class,
                    method="query",
                    args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})
    }
)
public class MybatisMetricsInterceptor implements Interceptor {

    private static final Stats MYBATIS_STAT = Profiler.Builder
            .builder()
            .type("mybatis")
            .build();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (!System.getProperty("mybatisProfileEnable", "true").equalsIgnoreCase("true")) {
            return invocation.proceed();
        }
        final Object[] args = invocation.getArgs();
        if (args != null && args.length > 0) {
            long begin = System.nanoTime();
            final MappedStatement mappedStatement = (MappedStatement) args[0];
            if (mappedStatement != null) {
                final String methodName = mappedStatement.getId(); //对应的是Mapper中的一个方法，如com.coohua.mapper.UserMapper.selectUser
                final String declaringTypeName = mappedStatement.getResource();  //对应的就是一个Mapper类文件，如UserMapper.java
                //以类名和方法名为标签
                String[] tags = new String[]{"operation", methodName, "class", declaringTypeName};
                try {
                    //每个请求gauge+1
                    MYBATIS_STAT.incConc(tags);
                    return invocation.proceed();
                } catch (Throwable throwable) {
                    //统计错误数
                    MYBATIS_STAT.error(tags);
                    throw throwable;
                } finally {
                    //请求结束gauge-1
                    MYBATIS_STAT.decConc(tags);
                    //统计请求执行时间
                    MYBATIS_STAT.observe(System.nanoTime() - begin, TimeUnit.NANOSECONDS, tags);
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}