/*
 *    Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.rank;

import com.exorath.service.commons.mongoProvider.MongoProvider;
import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.commons.tableNameProvider.TableNameProvider;
import com.exorath.service.rank.service.MongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Toon on 4/9/2017.
 */
public class Main {
    private Service service;
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public Main() {
        this.service = new MongoService(MongoProvider.getEnvironmentMongoProvider().getClient(), TableNameProvider.getEnvironmentTableNameProvider("DB_NAME").getTableName());
        LOG.info("Service " + this.service.getClass() + " instantiated");
        Transport.setup(service, PortProvider.getEnvironmentPortProvider());
        LOG.info("HTTP Transport initiated");
    }

    public static void main(String[] args) {
        new Main();
    }

}
