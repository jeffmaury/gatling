/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.core.action.builder

import com.excilys.ebi.gatling.core.action.{ While, system }
import com.excilys.ebi.gatling.core.config.ProtocolConfigurationRegistry
import com.excilys.ebi.gatling.core.session.Expression
import com.excilys.ebi.gatling.core.structure.ChainBuilder

import akka.actor.{ ActorRef, Props }

/**
 * Builder for WhileBuilder
 *
 * @constructor create a new WhileBuilder
 * @param condition the function that determine the condition
 * @param loopNext chain that will be executed if condition evaluates to true
 * @param counterName the name of the loop counter
 */
class WhileBuilder(condition: Expression[Boolean], loopNext: ChainBuilder, counterName: String) extends ActionBuilder {

	def build(next: ActorRef, protocolConfigurationRegistry: ProtocolConfigurationRegistry) = {
		val whileActor = system.actorOf(Props(new While(condition, counterName, next)))
		val loopContent = loopNext.build(whileActor, protocolConfigurationRegistry)
		whileActor ! loopContent
		whileActor
	}
}