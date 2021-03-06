/* ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 *
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 *
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 *
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * ---------------------------------------
 * HighestDegreeNeighbor.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.routing.util;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class HighestDegreeNeighbor extends RoutingAlgorithm {

	private int ttl;

	public HighestDegreeNeighbor() {
		super("HIGHEST_DEGREE_NEIGHBOR");
		this.ttl = Integer.MAX_VALUE;
	}

	public HighestDegreeNeighbor(int ttl) {
		super("HIGHEST_DEGREE_NEIGHBOR", new Parameter[] { new IntParameter(
				"TTL", ttl) });
		this.ttl = ttl;
	}

	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes());
	}

	private Route route(ArrayList<Integer> route, int current,
			Identifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (this.isEndPoint(current, target)) {
			return new Route(route, true);
		}
		if (route.size() > this.ttl) {
			return new Route(route, false);
		}

		if (nodes[current].getOutDegree() == 0) {
			return new Route(route, false);
		}

		int nextIndex = -1;
		int maxDegree = nodes[current].getOutDegree();
		for (int out : nodes[current].getOutgoingEdges()) {
			if (nodes[out].getOutDegree() > maxDegree) {
				nextIndex = out;
				maxDegree = nodes[out].getOutDegree();
			}
		}

		if (nextIndex == -1) {
			return new Route(route, false);
		}

		return this.route(route, nextIndex, target, rand, nodes);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0", IdentifierSpace.class);
	}

}
