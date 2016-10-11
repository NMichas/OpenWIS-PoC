/*
 * Copyright 2014 EUROPEAN DYNAMICS SA <info@eurodyn.com>
 *
 * Licensed under the EUPL, Version 1.1 only (the "License").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.openwis.poc.commands;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import javax.inject.Inject;
import java.sql.SQLException;



@Command(scope = "openwis", name = "test")
@Service
public final class TestCommand implements Action {
//	@Reference
//	private DataSource ds;

	@EndpointInject(uri = "direct:index1.post")
	ProducerTemplate producer;

	@Inject
	@Uri("direct:index1.post")
	ProducerTemplate producer2;

	@Override
	public Object execute() throws SQLException, InterruptedException {
		System.out.println("Test!");
		System.out.println(producer);
		System.out.println(producer2);


		return null;
	}

}
