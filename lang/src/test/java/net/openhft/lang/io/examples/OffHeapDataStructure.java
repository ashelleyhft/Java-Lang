/*
 * Copyright 2013 Peter Lawrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openhft.lang.io.examples;

import net.openhft.lang.io.NativeBytes;
/**
 * @author Anshul.Shelley
 */
public final class OffHeapDataStructure implements OffHeapObjectInterface
{
	private static int OFFSET_POSITION=0;
	private final static int DOUBLE_OFFSET = OFFSET_POSITION =0;
	private final static int LONG_OFFSET = OFFSET_POSITION += 8;
	private final static int INTEGER_OFFSET = OFFSET_POSITION += 8;
	private final static int CHAR_OFFSET = OFFSET_POSITION += 4;
	private final static int BYTE_OFFSET = OFFSET_POSITION += 2;
	
	private static int ELEMENT_SIZE = OFFSET_POSITION += 1;

	private long startIndex; 
	private long currentPosition;
	
	public OffHeapDataStructure(int totalElements)
	{
		startIndex =  NativeBytes.UNSAFE.allocateMemory(totalElements * ELEMENT_SIZE );
		//System.out.println("startIndex "+startIndex);
	}
	
	@Override
	public double getDouble() 
	{
		double d = NativeBytes.UNSAFE.getDouble(currentPosition+DOUBLE_OFFSET);
		currentPosition += 8;
		//System.out.println("d "+d);
		return d;
	}

	@Override
	public void setDouble(double doubleValue) 
	{
		NativeBytes.UNSAFE.putDouble(currentPosition+DOUBLE_OFFSET, doubleValue);		
	}

	@Override
	public long getLong() 
	{	
		long l = NativeBytes.UNSAFE.getLong(currentPosition+LONG_OFFSET);
		currentPosition += 8;
		//System.out.println("l "+l);
		return l;
	}

	@Override
	public void setLong(long longValue) 
	{
		NativeBytes.UNSAFE.putLong(currentPosition+LONG_OFFSET, longValue);
	}

	@Override
	public int getInt() 
	{
		int i = NativeBytes.UNSAFE.getInt(currentPosition+INTEGER_OFFSET);
		currentPosition += 4;
		//System.out.println("i "+i);
		return i;
	}

	private int previousIntValue=0;
	
	@Override
	public void setInt(int intValue, boolean lockCounter) 
	{
		if (lockCounter)
		{
			NativeBytes.UNSAFE.compareAndSwapInt(null, currentPosition+INTEGER_OFFSET, previousIntValue, intValue+previousIntValue);
			previousIntValue = intValue + previousIntValue;
			//System.out.println(previousIntValue);
		}
		else 
		{
			NativeBytes.UNSAFE.putInt(currentPosition+INTEGER_OFFSET, intValue);			
		}	
	}

	@Override
	public char getChar() 
	{
		char c = NativeBytes.UNSAFE.getChar(currentPosition+CHAR_OFFSET);
		currentPosition += 2;
		//System.out.println("c "+c);
		return c;
	}

	@Override
	public void setChar(char charValue) 
	{
		NativeBytes.UNSAFE.putChar(currentPosition+CHAR_OFFSET, charValue);		
	}

	@Override
	public byte getByte() 
	{
		byte b = NativeBytes.UNSAFE.getByte(currentPosition+BYTE_OFFSET);
		currentPosition += 1;
		//System.out.println("b "+b);
		return b;
	}

	@Override
	public void setByte(byte byteValue) 
	{
		NativeBytes.UNSAFE.putByte(currentPosition+BYTE_OFFSET, byteValue);
	}

	@Override
	public long calculatePosition(int position) 
	{
		return currentPosition= startIndex + (position * ELEMENT_SIZE * 1L);
	}
}
