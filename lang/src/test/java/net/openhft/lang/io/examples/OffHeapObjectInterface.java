package net.openhft.lang.io.examples;

public interface OffHeapObjectInterface 
{
	public double getDouble();
	public void setDouble(double doubleValue);
	
	public long getLong();
	public void setLong(long longValue);
	
	public int getInt();
	public void setInt(int intValue, boolean lockCounter);
	
	public char getChar();
	public void setChar(char charValue);
	
	public byte getByte();
	public void setByte(byte byteValue);
	
	public long calculatePosition(int position);
}
