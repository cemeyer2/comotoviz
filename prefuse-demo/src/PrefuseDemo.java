import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.data.io.GraphMLWriter;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;


public class PrefuseDemo {
	public static void main(String[] args) throws DataIOException, IOException
	{
		Graph graph = new GraphMLReader().readGraph("graph.xml");
		/*Graph graph = new Graph();
		graph.getNodeTable().addColumn("netid", String.class);
		graph.getNodeTable().addColumn("pseudonym", String.class);
		graph.getNodeTable().addColumn("isSolution", String.class);
		graph.getEdgeTable().addColumn("weight", String.class);
				
		BufferedReader in = new BufferedReader(new FileReader(new File("vmoss.out")));
		String str = "";
		while((str = in.readLine()) != null)
		{
			String[] split = str.split(",");
			if(split[0].equals("node"))
			{
				Node node = graph.addNode();
				node.set("netid", split[1]);
				node.set("pseudonym", split[2]);
				if(split[1].equals("solution"))
					node.set("isSolution", "t");
				else
					node.set("isSolution", "f");
				
			}
			if(split[0].equals("edge"))
			{
				String match1 = split[1];
				String match2 = split[2];
				Node n1 = null,n2 = null;
				Iterator iter = graph.nodes();
				while(iter.hasNext())
				{
					Node node = (Node)iter.next();
					if(node.get("netid").equals(match1))
						n1 = node;
					else if(node.get("netid").equals(match2))
						n2 = node;
				}
				if(n1 != null && n2 != null)
				{
					Edge edge = graph.addEdge(n1, n2);
					edge.set("weight", split[3]);
				}
			}
		}
		//rmessy code to remove edges between edges that have edges to the solution
		Iterator iter = graph.nodes();
		iter.next();
		while(iter.hasNext())
		{
			Node node = (Node)iter.next();
			Iterator iter2 = node.edges();
			boolean toSolution = false;
			while(iter2.hasNext())
			{
				Edge edge = (Edge)iter2.next();
				Node src = edge.getSourceNode();
				Node tgt = edge.getTargetNode();
				if(src.get("netid").equals("solution") || tgt.get("netid").equals("solution"))
				{
					toSolution = true;
				}
			}
			if(toSolution)
			{
				Iterator edgeIter = node.edges();
				ArrayList<Edge> toRemove = new ArrayList<Edge>();
				while(edgeIter.hasNext())
				{
					Edge edge = (Edge)edgeIter.next();
					Node src = edge.getSourceNode();
					Node tgt = edge.getTargetNode();
					if(!src.get("netid").equals("solution") && !tgt.get("netid").equals("solution"))
					{
						if(!toRemove.contains(edge))
							toRemove.add(edge);
					}
				}
				for(Edge e : toRemove)
				{
					graph.removeEdge(e);
				}
			}
		}*/
		
		Iterator iter = graph.nodes();
		iter.next();
		while(iter.hasNext())
		{
			Node node = (Node)iter.next();
			Iterator iter2 = node.edges();
			boolean toSolution = false;
			while(iter2.hasNext())
			{
				Edge edge = (Edge)iter2.next();
				Node src = edge.getSourceNode();
				Node tgt = edge.getTargetNode();
				if(src.get("netid").equals("[solution]") || tgt.get("netid").equals("[solution]"))
				{
					toSolution = true;
				}
			}
			if(toSolution)
			{
				Iterator edgeIter = node.edges();
				ArrayList<Edge> toRemove = new ArrayList<Edge>();
				while(edgeIter.hasNext())
				{
					Edge edge = (Edge)edgeIter.next();
					Node src = edge.getSourceNode();
					Node tgt = edge.getTargetNode();
					if(!src.get("netid").equals("[solution]") && !tgt.get("netid").equals("[solution]"))
					{
						if(!toRemove.contains(edge))
							toRemove.add(edge);
					}
				}
				for(Edge e : toRemove)
				{
					graph.removeEdge(e);
				}
			}
		}

		Visualization vis = new Visualization();
		VisualGraph vgraph = vis.addGraph("graph", graph);
		vis.setInteractive("graph.edges", null, false);

		LabelRenderer r = new LabelRenderer("netid");
		r.setRoundedCorner(8, 8); // round the corners

		// create a new default renderer factory
		// return our name label renderer as the default for all non-EdgeItems
		// includes straight line edges for EdgeItems by default
		vis.setRendererFactory(new DefaultRendererFactory(r, new EdgeRenderer(Constants.EDGE_TYPE_CURVE)));


		// map nominal data values to colors using our provided palette
		int[] pal = {ColorLib.color(Color.WHITE), ColorLib.color(Color.RED)};
		DataColorAction fill = new DataColorAction("graph.nodes", "isSolution",
                Constants.NOMINAL, VisualItem.FILLCOLOR, pal);
		ColorAction stroke = new ColorAction("graph.nodes", VisualItem.STROKECOLOR, ColorLib.color(Color.BLACK));
		// use black for node text
		ColorAction text = new ColorAction("graph.nodes",
				VisualItem.TEXTCOLOR, ColorLib.gray(0));
		// use light grey for edges
		ColorAction edges = new ColorAction("graph.edges",
				VisualItem.STROKECOLOR, ColorLib.gray(0));
		ColorAction hl = new ColorAction("graph.nodes", VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));

		// create an action list containing all color assignments
		ActionList color = new ActionList();
		color.add(fill);
		color.add(stroke);
		color.add(text);
		color.add(edges);
		color.add(hl);


		// create an action list with an animated layout
		ActionList layout = new ActionList(5000);
		//layout.add(new ForceDirectedLayout("graph"));
		ForceDirectedLayout l = new ForceDirectedLayout("graph");
		layout.add(new ForceDirectedLayout("graph"));
		layout.add(new RepaintAction());

		// add the actions to the visualization
		vis.putAction("color", color);
		vis.putAction("layout", layout);


		// -- 5. the display and interactive controls -------------------------

		Display d = new Display(vis);
		d.setSize(1000, 1000); // set display size
		// drag individual items around
		d.addControlListener(new DragControl());
		// pan with left-click drag on background
		d.addControlListener(new PanControl()); 
		// zoom with right-click drag
		d.addControlListener(new ZoomControl());
		d.addControlListener(new NeighborHighlightControl());

		// -- 6. launch the visualization -------------------------------------

		//create a new window to hold the visualization
		JFrame frame = new JFrame("prefuse example");
		//ensure application exits when window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(d);
		frame.pack();           // layout components in window
		frame.setVisible(true); // show the window

		// assign the colors
		vis.run("color");
		// start up the animated layout
		vis.run("layout");
		
		new GraphMLWriter().writeGraph(graph, "graph.xml");

	}
}
