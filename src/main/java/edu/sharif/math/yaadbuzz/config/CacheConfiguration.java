package edu.sharif.math.yaadbuzz.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, edu.sharif.math.yaadbuzz.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, edu.sharif.math.yaadbuzz.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, edu.sharif.math.yaadbuzz.domain.User.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Authority.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.User.class.getName() + ".authorities");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserPerDepartment.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserPerDepartment.class.getName() + ".tagedInMemoeries");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Department.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Department.class.getName() + ".userPerDepartments");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Memory.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Memory.class.getName() + ".comments");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Memory.class.getName() + ".pictures");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Memory.class.getName() + ".tageds");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Comment.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Picture.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserPerDepartment.class.getName() + ".topicAssigneds");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserPerDepartment.class.getName() + ".charateristics");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Topic.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Topic.class.getName() + ".votes");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.TopicVote.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Charateristics.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Department.class.getName() + ".memories");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserPerDepartment.class.getName() + ".charateristicsRepetations");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Topic.class.getName() + ".voters");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserPerDepartment.class.getName() + ".topicsVoteds");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserPerDepartment.class.getName() + ".characteristicsVoteds");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Charateristics.class.getName() + ".charateristicsRepetations");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Charateristics.class.getName() + ".voters");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Comment.class.getName() + ".pictures");
            createCache(cm, edu.sharif.math.yaadbuzz.domain.Memorial.class.getName());
            createCache(cm, edu.sharif.math.yaadbuzz.domain.UserExtra.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
