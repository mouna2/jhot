

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import soot.*;
import soot.jimple.Stmt;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import soot.util.Chain;

public class TestSootCallGraph extends SceneTransformer {

	static LinkedList<String> excludeList;
	public static void main(String[] args)	{

		 String mypath="org.jhotdraw"; 
		 String directory= "C:\\Users\\\\mouna\\Downloads\\TraceGeneratorCDG-master\\TraceGeneratorCDG-master\\jhot\\src\\samnples\\net"; 
		 
		 
		
//		String mainclass = "edu.ncsu.csc.itrust";
//		 String mypath="edu.ncsu.csc.itrust"; 
//		 String directory= "C:\\Users\\mouna\\new_workspace\\iTrust\\src"; 
		 
		 

		 
		 
		 
			    excludeJDKLibrary();

//		
			 
			 
			 //set classpath
	    String javapath = System.getProperty("java.class.path");
	    String jredir = System.getProperty("java.home")+"\\lib\\rt.jar";
	    String path = javapath+File.pathSeparator+jredir+File.pathSeparator+directory ;
	 
	    
	    
	    
	    Scene.v().setSootClassPath(path);
	    System.out.println(path);
            //add an intra-procedural analysis phase to Soot
	    TestSootCallGraph analysis = new TestSootCallGraph();
	    PackManager.v().getPack("wjtp").add(new Transform("wjtp.TestSootCallGraph", analysis));



	    //whole program analysis
	    Options.v().set_whole_program(true);

            //load and set main class
	    Options.v().set_app(true);
	    Scene.v().loadDynamicClasses();
	   List<String> mylist = Arrays.asList(directory); 
	   System.out.println(mylist);
	    Options.v().set_process_dir(mylist);
//	    Scene.v().loadDynamicClasses();
        Scene.v().loadNecessaryClasses();
//        Scene.v().loadBasicClasses();
	    
	    System.out.println("Application classes"+Scene.v().getApplicationClasses());
	    System.out.println("classes "+Scene.v().getEntryPoints());
//	    System.out.println("library classes "+Scene.v().getLibraryClasses());
	    System.out.println("dynamic classes"+Scene.v().dynamicClasses());
	    System.out.println("classes"+Scene.v().getClasses());

	    System.out.println("****************");
//	    SootClass appclass = Scene.v().loadClassAndSupport(mainclass); 
//	    appclass.setApplicationClass();
//	    System.out.println("APPCLASS ==="+appclass.isPhantom()+" level "+appclass.resolvingLevel());
//        Scene.v().addBasicClass("Example", SootClass.BODIES);
//
//
//	   
//	    System.out.println("-------------");
//	    
//	    
//	    Scene.v().setMainClass(appclass);
//	    Scene.v().addBasicClass(mainclass);

	    Scene.v().loadNecessaryClasses();
	    Scene.v().loadDynamicClasses(); 
	    Scene.v().loadBasicClasses(); 
	    Scene.v().getActiveHierarchy(); 
	    System.out.println("**********************************");
	    for ( SootClass myclass: Scene.v().getClasses()) {
//	    	System.out.println(myclass.getPackageName());
	    	if(!myclass.toString().contains("java.") && !myclass.toString().contains("jdk.") && !myclass.toString().contains("javax") && !myclass.toString().contains("sun.") && 
	    			myclass.getPackageName().contains(mypath)) {
		    	System.out.println("--------------------------------------");

		    	System.out.println(myclass);
		    	System.out.println("--------------------------------------");
		    	for(SootMethod mymethod: myclass.getMethods()) {
			    	System.out.println("//////////////////////////////////////////////////");
			    	System.out.println(mymethod.getParameterTypes());
			    	System.out.println(mymethod.getReturnType());
		    		System.out.println("METHOD : "+mymethod);
		    		Body body = null; 
		    		try {
			    		 body = mymethod.retrieveActiveBody(); 

		    		}catch(Exception e) {
		    			
		    		}
		    		if(body!=null) {
		    			for(Local local: body.getLocals()) {
		    				if(local.getType().toString().contains(mypath))
				    		System.out.println("local "+local.getName()+" type "+local.getType());
				    	}
		    		}
		   
		    		
		    	}
		    	
				
		    	System.out.println("hey");
	    	}

	    }
	    System.out.println("over");
	    //enable call graph
	    //enableCHACallGraph();
	    //enableSparkCallGraph();

            //start working
//	    PackManager.v().runPacks();
	    System.out.println("over32");
	}
	private static void excludeJDKLibrary()
	{
		 //exclude jdk classes
	    Options.v().set_exclude(excludeList());
		  //this option must be disabled for a sound call graph
	      Options.v().set_no_bodies_for_excluded(true);
	      Options.v().set_allow_phantom_refs(true);
	}
	private static void enableSparkCallGraph() {

		//Enable Spark
	      HashMap<String,String> opt = new HashMap<String,String>();
	      //opt.put("propagator","worklist");
	      //opt.put("simple-edges-bidirectional","false");
	      opt.put("on-fly-cg","true");
	      //opt.put("set-impl","double");
	      //opt.put("double-set-old","hybrid");
	      //opt.put("double-set-new","hybrid");
	      //opt.put("pre_jimplify", "true");
	      SparkTransformer.v().transform("",opt);
	      PhaseOptions.v().setPhaseOption("cg.spark", "enabled:true");
	}

	private static void enableCHACallGraph() {
		CHATransformer.v().transform();
	}

	private static LinkedList<String> excludeList()
	{
		if(excludeList==null)
		{
			excludeList = new LinkedList<String> ();

			excludeList.add("java.");
		    excludeList.add("javax.");
		    excludeList.add("sun.");
		    excludeList.add("sunw.");
		    excludeList.add("com.sun.");
		    excludeList.add("com.ibm.");
		    excludeList.add("com.apple.");
		    excludeList.add("apple.awt.");
		}
		return excludeList;
	}
	@Override
	protected void internalTransform(String phaseName,
			Map options) {

//		int numOfEdges =0;
//	    enableSparkCallGraph();
//		CallGraph callGraph = Scene.v().getCallGraph();
//		for(SootClass sc : Scene.v().getApplicationClasses()){
//			for(SootMethod m : sc.getMethods()){
//
//		Iterator<MethodOrMethodContext> targets = new Targets(
//				 callGraph.edgesOutOf(m));
//
//				 while (targets.hasNext()) {
//
//					 numOfEdges++;
//
//				SootMethod tgt = (SootMethod) targets.next();
//				 System.out.println(m + " may call " + tgt);
//				 }
//			}
//		}
//
//		 System.err.println("Total Edges:" + numOfEdges);

		 
		 
		 
	      
	}
}