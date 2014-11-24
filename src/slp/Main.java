package slp;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import slp.Slp.Exp;
import slp.Slp.Exp.Eseq;
import slp.Slp.Exp.Id;
import slp.Slp.Exp.Num;
import slp.Slp.Exp.Op;
import slp.Slp.ExpList;
import slp.Slp.Stm;
import util.Bug;
import control.Control;

public class Main
{
	
  // ///////////////////////////////////////////
  // maximum number of args

	
	int count=0;
	private int maxArgs(Stm.T stm)
	{	
		if (stm instanceof Stm.Compound) {
		      Stm.Compound s = (Stm.Compound) stm;
		      int n1 = maxArgs(s.s1);
		      int n2 = maxArgs(s.s2);

		    } 
		    else if (stm instanceof Stm.Assign) {		    		    	
		    	Stm.Assign a = (Stm.Assign) stm;
		    	Exp.T b = a.exp; 
			    maxArgsExp(b);  
		    }
		    else if (stm instanceof Stm.Print) {
		      Stm.Print c  = (Stm.Print) stm;
		      ExpList.T ep = c.explist;
		     count = count > maxArgsExpList(ep)?count:maxArgsExpList(ep);
		    } else 
		    	new Bug();		 
		return count;
	}
	
	private int maxArgsExpList(ExpList.T explist)
	  {
		int temp_count_mael = 0;
		 if (explist instanceof ExpList.Pair) {			 
		      ExpList.Pair pair = (ExpList.Pair) explist;
		      Exp.T exp = pair.exp;
		      ExpList.T list = pair.list;
		      temp_count_mael+=2;
		      maxArgsExp(exp);		 		      
		      maxArgsExpList(list);
		      
			 return temp_count_mael;
		    } else if (explist instanceof ExpList.Last) {
		      ExpList.Last last = (ExpList.Last) explist;
		      Exp.T exp = last.exp;
		      maxArgsExp(exp);
		      ++temp_count_mael;
		     return temp_count_mael;
		    } else
		      new Bug();
		 return 0;
	  }
	
  private int maxArgsExp(Exp.T exp)
  {
	  if (exp instanceof Eseq) {
	      Eseq e = (Eseq) exp;
	      Stm.T stm = e.stm;
	      Exp.T ee = e.exp;

	      maxArgs(stm);
	      maxArgsExp(ee);
	    } //else
	      //new Bug();
    return -1;
  }

 /* private int maxArgsStm(Stm.T stm)
  {
    if (stm instanceof Stm.Compound) {
      Stm.Compound s = (Stm.Compound) stm;
      int n1 = maxArgsStm(s.s1);
      int n2 = maxArgsStm(s.s2);

      return n1 >= n2 ? n1 : n2;
    } 
    else if (stm instanceof Stm.Assign) {
      new Todo();
      return -1;
    }
    else if (stm instanceof Stm.Print) {
      new Todo();
     return -1;
    } else
      new Bug();
    return 0;
  }*/

  // ////////////////////////////////////////
  // interpreter
  
  HashSet<Table> id = new HashSet<Table>();

  /*private void interpExp(Exp.T exp)
  {
    new Todo();
  }*/

  private int getNumOfId(String name)
  {
	  Iterator it = id.iterator();
	  while(it.hasNext())
	  {
		  Table t = (Table)it.next();
		  if(name.equals(t.id))
		  {
			  return t.value;
		
		  }
		  
	  }
	  return 0;
  }
  
  private void update(String us, int un)
  {
	  
	  Table ut = new Table(us,un);
	  id.add(ut);
	  
  }
  

  private int interpExp(Exp.T exp)
  {
	  
	  if(exp instanceof Exp.Id)
	  {
		  Exp.Id ei = (Exp.Id) exp;		  
		  return getNumOfId(ei.id);
	  }
	  
	  else if(exp instanceof Exp.Num)
	{
		Exp.Num en = (Exp.Num) exp;		
		return en.num;
		
	}
	  else if(exp instanceof Exp.Eseq)
	  {
		  Exp.Eseq ee = (Exp.Eseq) exp;
		  Stm.T st = ee.stm;
		  Exp.T et = ee.exp;
		  interpStm(st);//here red
		  return interpExp(et);//here 80
	  }
	  else if(exp instanceof Exp.Op)
	  {
		  Exp.Op e = (Exp.Op) exp;
	      Exp.T left = e.left;
	      Exp.T right = e.right;
	      Exp.OP_T op = e.op;	      
	 
	      switch (op) {
	      case ADD:
	    	  int tempn1 = interpExp(left);
	    	  int tempn2 = interpExp(right);
	       return tempn1+tempn2;
	      case SUB:
	    	  tempn1 = interpExp(left);
	    	  tempn2 = interpExp(right);
		       return tempn1-tempn2;
	        
	      case TIMES:
	    	  tempn1 = interpExp(left);
	    	  tempn2 = interpExp(right);
		       return tempn1*tempn2;
	        
	      case DIVIDE:
	    	  tempn1 = interpExp(left);
	    	  tempn2 = interpExp(right);
		       return tempn1/tempn2;
	        
	      default:
	        new Bug();
	      
	      }
	  }
	  return -1;
  }

  private void interpExpList(ExpList.T exp)
  {
	  if(exp instanceof ExpList.Pair)//pair zhiqian yiding shi print?
	  {
		  ExpList.Pair ep = (ExpList.Pair) exp;//lost here
		  Exp.T et = ep.exp;
		  ExpList.T ept = ep.list;	
		  System.out.print(interpExp(et)+" ");// first 8
		  interpExpList(ept);
	  }
	  if(exp instanceof ExpList.Last)
	  {
		  ExpList.Last el = (ExpList.Last) exp;
		  Exp.T et = el.exp;
		 System.out.println(interpExp(et));//first 7
	  }
  }
  
  private void interpStm(Stm.T prog)
  {
    if (prog instanceof Stm.Compound) {
    	Stm.Compound s = (Stm.Compound) prog;
    	interpStm(s.s1);
    	interpStm(s.s2);
      
    } else if (prog instanceof Stm.Assign) {
    	Stm.Assign a = (Stm.Assign) prog;
    	Exp.T et = a.exp;
        update(a.id,interpExp(et));    
        
    } else if (prog instanceof Stm.Print) {
        Stm.Print sp = (Stm.Print) prog;
        ExpList.T explist = sp.explist;
        interpExpList(explist);
       
    } else
      new Bug();
  }
  
  private void printValue()//output values of hashset of id
  {
	  Iterator ie = id.iterator();
	  while(ie.hasNext())
	  {
		  Table t = (Table)ie.next();
		  System.out.println(t.id+"  "+t.value);
		  
	  }
  }

  // ////////////////////////////////////////
  // compile
  HashSet<String> ids;
  StringBuffer buf;

  private void emit(String s)
  {
    buf.append(s);
  }

  private void compileExp(Exp.T exp)
  {
    if (exp instanceof Id) {
      Exp.Id e = (Exp.Id) exp;
      String id = e.id;

      emit("\tmovl\t" + id + ", %eax\n");
    } else if (exp instanceof Num) {
      Exp.Num e = (Exp.Num) exp;
      int num = e.num;

      emit("\tmovl\t$" + num + ", %eax\n");
    } else if (exp instanceof Op) {
      Exp.Op e = (Exp.Op) exp;
      Exp.T left = e.left;
      Exp.T right = e.right;
      Exp.OP_T op = e.op;

      switch (op) {
      case ADD:
        compileExp(left);
        emit("\tpushl\t%eax\n");
        compileExp(right);
        emit("\tpopl\t%edx\n");
        emit("\taddl\t%edx, %eax\n");
        break;
      case SUB:
        compileExp(left);
        emit("\tpushl\t%eax\n");
        compileExp(right);
        emit("\tpopl\t%edx\n");
        emit("\tsubl\t%eax, %edx\n");
        emit("\tmovl\t%edx, %eax\n");
        break;
      case TIMES:
        compileExp(left);
        emit("\tpushl\t%eax\n");
        compileExp(right);
        emit("\tpopl\t%edx\n");
        emit("\timul\t%edx\n");
        break;
      case DIVIDE:
        compileExp(left);
        emit("\tpushl\t%eax\n");
        compileExp(right);
        if((buf.lastIndexOf("0")-1)==buf.lastIndexOf("$"))//no divid10 also error
        {
        	System.out.println("divied by zero!!\nProcess will terminate immediately!");
        	System.exit(1);
        }
        else{
        emit("\tpopl\t%edx\n");
        emit("\tmovl\t%eax, %ecx\n");
        emit("\tmovl\t%edx, %eax\n");
        emit("\tcltd\n");
        emit("\tdiv\t%ecx\n");
        }
        break;
      default:
        new Bug();
      }
    } else if (exp instanceof Eseq) {
      Eseq e = (Eseq) exp;
      Stm.T stm = e.stm;
      Exp.T ee = e.exp;

      compileStm(stm);
      compileExp(ee);
    } else
      new Bug();
  }

  private void compileExpList(ExpList.T explist)
  {
    if (explist instanceof ExpList.Pair) {
      ExpList.Pair pair = (ExpList.Pair) explist;
      Exp.T exp = pair.exp;
      ExpList.T list = pair.list;

      compileExp(exp);
      emit("\tpushl\t%eax\n");
      emit("\tpushl\t$slp_format\n");
      emit("\tcall\tprintf\n");
      emit("\taddl\t$4, %esp\n");
      compileExpList(list);
    } else if (explist instanceof ExpList.Last) {
      ExpList.Last last = (ExpList.Last) explist;
      Exp.T exp = last.exp;

      compileExp(exp);
      emit("\tpushl\t%eax\n");
      emit("\tpushl\t$slp_format\n");
      emit("\tcall\tprintf\n");
      emit("\taddl\t$4, %esp\n");
    } else
      new Bug();
  }

  private void compileStm(Stm.T prog)
  {
    if (prog instanceof Stm.Compound) {
      Stm.Compound s = (Stm.Compound) prog;
      Stm.T s1 = s.s1;
      Stm.T s2 = s.s2;

      compileStm(s1);
      compileStm(s2);
    } else if (prog instanceof Stm.Assign) {
      Stm.Assign s = (Stm.Assign) prog;
      String id = s.id;
      Exp.T exp = s.exp;

      ids.add(id);
      compileExp(exp);
      emit("\tmovl\t%eax, " + id + "\n");
    } else if (prog instanceof Stm.Print) {
      Stm.Print s = (Stm.Print) prog;
      ExpList.T explist = s.explist;

      compileExpList(explist);
      emit("\tpushl\t$newline\n");
      emit("\tcall\tprintf\n");
      emit("\taddl\t$4, %esp\n");
    } else
      new Bug();
  }

  // ////////////////////////////////////////
  public void doit(Stm.T prog)
  {
    // return the maximum number of arguments
    if (Control.ConSlp.action == Control.ConSlp.T.ARGS) {
     // int numArgs = maxArgsStm(prog);
    	int numArgs = maxArgs(prog);
      System.out.println(numArgs);
    }

    // interpret a given program
    if (Control.ConSlp.action == Control.ConSlp.T.INTERP) {
      interpStm(prog);
      //printValue();
    }

    // compile a given SLP program to x86
    if (Control.ConSlp.action == Control.ConSlp.T.COMPILE) {
      ids = new HashSet<String>();
      buf = new StringBuffer();

      compileStm(prog);
      try {
        // FileOutputStream out = new FileOutputStream();
        FileWriter writer = new FileWriter("slp_gen.s");
        writer
            .write("// Automatically generated by the Tiger compiler, do NOT edit.\n\n");
        writer.write("\t.data\n");
        writer.write("slp_format:\n");
        writer.write("\t.string \"%d \"\n");
        writer.write("newline:\n");
        writer.write("\t.string \"\\n\"\n");
        for (String s : this.ids) {
          writer.write(s + ":\n");
          writer.write("\t.int 0\n");
        }
        writer.write("\n\n\t.text\n");
        writer.write("\t.globl main\n");
        writer.write("main:\n");
        writer.write("\tpushl\t%ebp\n");
        writer.write("\tmovl\t%esp, %ebp\n");
        writer.write(buf.toString());
        writer.write("\tleave\n\tret\n\n");
        writer.close();
        Process child = Runtime.getRuntime().exec("gcc slp_gen.s");
        child.waitFor();
        if (!Control.ConSlp.keepasm)
          Runtime.getRuntime().exec("rm -rf slp_gen.s");
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(0);
      }
      // System.out.println(buf.toString());
    }
  }
}
