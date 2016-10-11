# OpenWIS PoC
## Installation
    feature:repo-add mvn:com.openwis/poc-back-end/1.0.0-SNAPSHOT/xml/features

    feature:install qlack2-util-repack-mysql; \
    feature:install qlack2-util-repack-liquibase
    
    config:edit org.ops4j.datasource-openwis; \
    config:property-set user root; \
    config:property-set password root; \
    config:property-set url jdbc:mysql://localhost/openwis?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&connectionCollation=utf8mb4_unicode_ci&createDatabaseIfNotExist=true; \
    config:property-set dataSourceName openwis-ds; \
    config:property-set osgi.jdbc.driver.name mysql-pool-xa; \
    config:property-set pool.testOnBorrow true; \
    config:property-set jdbc.pool.maxTotal 50; \
    config:property-set jdbc.pool.maxIdle 5; \
    config:property-set jdbc.pool.minIdle 1; \
    config:update
    
    config:edit org.openengsb.labs.liquibase; \
    config:property-set defaultSchema openwis; \
    config:property-set datasource openwis-ds; \
    config:update
    
    config:edit com.eurodyn.qlack2.fuse.search; \
    config:property-set es.hosts 127.0.0.1:9300; \
    config:property-set es.clusterName elasticsearch; \
    config:update
    
    jdbc:execute openwis-ds alter database openwis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
    
    feature:install openwis
    
    