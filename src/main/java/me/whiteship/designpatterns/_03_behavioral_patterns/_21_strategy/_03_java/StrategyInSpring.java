package me.whiteship.designpatterns._03_behavioral_patterns._21_strategy._03_java;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.config.AnnotationDrivenBeanDefinitionParser;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

public class StrategyInSpring {

    public static void main(String[] args) {
        // ApplicationContext 라는 Strategy Inteface를 상속받은 Concrete Strategy 들이 많다.
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        ApplicationContext applicationContext1 = new FileSystemXmlApplicationContext();
        ApplicationContext applicationContext2 = new AnnotationConfigApplicationContext();

        // 얘도 마찬가지
        BeanDefinitionParser parser = new AnnotationConfigBeanDefinitionParser();
        BeanDefinitionParser parser1 = new AnnotationDrivenBeanDefinitionParser();

        // 얘도 마찬가지
        PlatformTransactionManager platformTransactionManager = new HibernateTransactionManager();
        PlatformTransactionManager platformTransactionManager1 = new JpaTransactionManager();

        // 얘도 마찬가지
        CacheManager cacheManager = new EhCacheCacheManager();
        CacheManager cacheManager1 = new JCacheCacheManager();
    }
}
