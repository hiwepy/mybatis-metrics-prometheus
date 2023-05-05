package org.mybatis.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DL.Wan
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({ MetricsAutoConfiguration.class, DataSourceAutoConfiguration.class, SimpleMetricsExportAutoConfiguration.class })
@ConditionalOnClass({ SqlSessionFactory.class, MeterRegistry.class })
@ConditionalOnBean({ DataSource.class, MeterRegistry.class })
public class MybatisMetricsAutoConfiguration {

	@Bean
	public MybatisMetrics druidMetrics(ObjectProvider<Map<String, DataSource>> dataSourcesProvider) {
		Map<String, DataSource> dataSourceMap = dataSourcesProvider.getIfAvailable(HashMap::new);
		return new MybatisMetrics(druidDataSourceMap);
	}

}
