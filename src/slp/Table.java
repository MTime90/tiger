package slp;

public class Table {
	String id;
	int value;
	Table tail;
	Table(String i, int v)
	{
		id = i;
		value = v;
		
	}
	Table(String i, int v, Table t)
	{
		id = i;
		value = v;
		tail = t;
		
	}
	
	public int hashCode()
	{
		return id.hashCode()+value*37;
	}
	

}
